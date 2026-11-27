<?php

/**
 * Moderación de imágenes con Google Cloud Vision SafeSearch (modelo de tres carriles)
 * y avisos para usuarios y administradores.
 *
 *   Carril 1 (aprobada):  adult y violence <= UNLIKELY y el resto de categorías <= UNLIKELY.
 *   Carril 3 (rechazada): adult o violence >= LIKELY. La imagen NO se guarda.
 *   Carril 2 (revision):  cualquier otro caso (alguna categoría en POSSIBLE, racy LIKELY, etc.).
 *                         La receta queda 'pendiente_revision' y decide un administrador.
 *
 * Si Vision no está configurado o no responde, la imagen va a revisión (nunca se aprueba sola).
 */

if (is_file(__DIR__ . '/config.local.php')) {
    require_once __DIR__ . '/config.local.php';
}
if (!defined('VISION_API_KEY')) define('VISION_API_KEY', '');
if (!defined('VISION_ENDPOINT')) define('VISION_ENDPOINT', 'https://vision.googleapis.com/v1/images:annotate');

const CATEGORIAS_SAFESEARCH = ['adult', 'violence', 'racy', 'medical', 'spoof'];

const NIVELES_SAFESEARCH = [
    'VERY_UNLIKELY' => 1,
    'UNLIKELY'      => 2,
    'POSSIBLE'      => 3,
    'LIKELY'        => 4,
    'VERY_LIKELY'   => 5,
];

const NOMBRES_CATEGORIA = [
    'adult' => 'contenido para adultos',
    'violence' => 'violencia',
    'racy' => 'contenido sugerente',
    'medical' => 'contenido médico',
    'spoof' => 'imagen alterada',
];

/** Nivel numérico de una probabilidad; UNKNOWN o vacío cuenta como POSSIBLE (dudoso). */
function nivelSafeSearch(?string $valor): int
{
    return NIVELES_SAFESEARCH[$valor] ?? NIVELES_SAFESEARCH['POSSIBLE'];
}

/**
 * Llama a SafeSearch con los bytes de la imagen.
 * @return array ['puntajes' => [categoria => nivel] | null, 'error' => string|null]
 */
function analizarSafeSearch(string $bytes): array
{
    if (VISION_API_KEY === '') {
        return ['puntajes' => null, 'error' => 'Moderación automática no configurada (falta VISION_API_KEY)'];
    }
    if (!function_exists('curl_init')) {
        return ['puntajes' => null, 'error' => 'PHP no tiene la extensión curl'];
    }

    $cuerpo = json_encode([
        'requests' => [[
            'image' => ['content' => base64_encode($bytes)],
            'features' => [['type' => 'SAFE_SEARCH_DETECTION']],
        ]],
    ]);

    $ch = curl_init(VISION_ENDPOINT . '?key=' . urlencode(VISION_API_KEY));
    curl_setopt_array($ch, [
        CURLOPT_POST => true,
        CURLOPT_POSTFIELDS => $cuerpo,
        CURLOPT_HTTPHEADER => ['Content-Type: application/json'],
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_CONNECTTIMEOUT => 10,
        CURLOPT_TIMEOUT => 20,
    ]);
    $respuesta = curl_exec($ch);
    $codigo = (int)curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $errorCurl = curl_error($ch);
    curl_close($ch);

    if ($respuesta === false) {
        return ['puntajes' => null, 'error' => 'SafeSearch no respondió: ' . $errorCurl];
    }
    $json = json_decode($respuesta, true);
    $anotacion = $json['responses'][0]['safeSearchAnnotation'] ?? null;
    if ($codigo !== 200 || !is_array($anotacion)) {
        $detalle = $json['error']['message'] ?? ($json['responses'][0]['error']['message'] ?? "HTTP $codigo");
        return ['puntajes' => null, 'error' => 'SafeSearch devolvió un error: ' . $detalle];
    }

    $puntajes = [];
    foreach (CATEGORIAS_SAFESEARCH as $c) {
        $valor = $anotacion[$c] ?? 'UNKNOWN';
        $puntajes[$c] = isset(NIVELES_SAFESEARCH[$valor]) ? $valor : 'UNKNOWN';
    }
    return ['puntajes' => $puntajes, 'error' => null];
}

/**
 * Decide el carril.
 * @param array|null $puntajes  [categoria => 'VERY_UNLIKELY'...] o null si no hubo análisis
 * @return array ['resultado' => aprobada|revision|rechazada, 'motivo' => string|null]
 */
function clasificarImagen(?array $puntajes, ?string $errorAnalisis = null): array
{
    if ($puntajes === null) {
        return ['resultado' => 'revision', 'motivo' => $errorAnalisis ?: 'Sin análisis automático'];
    }

    $n = static fn($c) => nivelSafeSearch($puntajes[$c] ?? null);

    // Carril 3: rechazo automático
    $graves = [];
    foreach (['adult', 'violence'] as $c) {
        if ($n($c) >= NIVELES_SAFESEARCH['LIKELY']) $graves[] = NOMBRES_CATEGORIA[$c];
    }
    if ($graves) {
        return ['resultado' => 'rechazada', 'motivo' => 'Posible ' . implode(' y ', $graves)];
    }

    // Carril 1: aprobación automática
    $dudosas = [];
    foreach (CATEGORIAS_SAFESEARCH as $c) {
        if ($n($c) > NIVELES_SAFESEARCH['UNLIKELY']) $dudosas[] = NOMBRES_CATEGORIA[$c];
    }
    if (!$dudosas) {
        return ['resultado' => 'aprobada', 'motivo' => null];
    }

    // Carril 2: zona gris
    return ['resultado' => 'revision', 'motivo' => 'Revisar: ' . implode(', ', $dudosas)];
}

/**
 * Resultado de moderación de la imagen que se manda al guardar/editar una receta.
 * - Sin imagen: 'aprobada'.
 * - Misma imagen que ya tenía la receta: su resultado guardado.
 * - Imagen nueva: debe haberla subido este mismo usuario con subir_imagen.php.
 * Lanza Exception si la imagen no es válida.
 */
function resultadoModeracion(PDO $pdo, ?string $ruta, int $idUsuario, ?string $rutaActual = null): string
{
    if ($ruta === null) {
        return 'aprobada';
    }
    $stmt = $pdo->prepare("SELECT id_usuario, resultado FROM moderacion_imagen WHERE ruta = ?");
    $stmt->execute([$ruta]);
    $fila = $stmt->fetch();

    if ($ruta === $rutaActual) {
        return $fila ? $fila['resultado'] : 'aprobada';
    }
    if (!$fila || (int)$fila['id_usuario'] !== $idUsuario || $fila['resultado'] === 'rechazada') {
        throw new Exception("La imagen no es válida; súbela de nuevo");
    }
    return $fila['resultado'];
}

/** Liga la imagen moderada con su receta (para la cola del administrador). */
function ligarModeracionReceta(PDO $pdo, ?string $ruta, int $idReceta): void
{
    if ($ruta === null) return;
    $pdo->prepare("UPDATE moderacion_imagen SET id_receta = ? WHERE ruta = ?")->execute([$idReceta, $ruta]);
}

// ------------------------------------------------------------------ Avisos

function notificar(PDO $pdo, int $idUsuario, string $tipo, string $titulo, string $mensaje, ?int $idReceta = null): void
{
    $stmt = $pdo->prepare("
        INSERT INTO notificacion (id_usuario, tipo, titulo, mensaje, id_receta) VALUES (?, ?, ?, ?, ?)
    ");
    $stmt->execute([$idUsuario, $tipo, mb_substr($titulo, 0, 120), mb_substr($mensaje, 0, 500), $idReceta]);
}

function notificarAdministradores(PDO $pdo, string $tipo, string $titulo, string $mensaje, ?int $idReceta = null): void
{
    $admins = $pdo->query("SELECT id_usuario FROM usuario WHERE rol = 'administrador'")->fetchAll(PDO::FETCH_COLUMN);
    foreach ($admins as $id) {
        notificar($pdo, (int)$id, $tipo, $titulo, $mensaje, $idReceta);
    }
}

/** El usuario existe y es administrador. */
function esAdministrador(PDO $pdo, int $idUsuario): bool
{
    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ? AND rol = 'administrador'");
    $stmt->execute([$idUsuario]);
    return (bool)$stmt->fetchColumn();
}
