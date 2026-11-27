<?php

/**
 * Lógica compartida por guardar_receta.php y editar_receta.php.
 * Todas las funciones lanzan Exception con un mensaje para el usuario si algo no es válido.
 */

require_once __DIR__ . '/unidades_helper.php';
require_once __DIR__ . '/moderacion_helper.php';

// Reglas de las etiquetas automáticas (valores por UNA porción)
const SALUDABLE_KCAL_MAX = 500;
const ALTA_PROTEINA_G_MIN = 20;
const RAPIDA_MIN_MAX = 30;
const MAX_ETIQUETAS_USUARIO = 5;

const DIFICULTADES = ['principiante', 'intermedio', 'avanzado'];

/** Lee y valida los datos generales. Devuelve un arreglo listo para la tabla receta. */
function leerDatosGenerales(array $data): array
{
    $nombre = trim($data['nombre'] ?? '');
    $porciones = (int)($data['porciones'] ?? 0);
    $tiempo = (int)($data['tiempo_estimado'] ?? 0);
    $dificultad = strtolower(trim($data['nivel_dificultad'] ?? 'principiante'));

    if ($nombre === '' || mb_strlen($nombre) > 150) {
        throw new Exception("El nombre de la receta es obligatorio (máximo 150 caracteres)");
    }
    if ($porciones <= 0 || $porciones > 100) {
        throw new Exception("Las porciones deben estar entre 1 y 100");
    }
    if ($tiempo <= 0 || $tiempo > 1440) {
        throw new Exception("El tiempo debe estar entre 1 y 1440 minutos");
    }
    if (!in_array($dificultad, DIFICULTADES, true)) {
        throw new Exception("Nivel de dificultad no válido");
    }

    return [
        'nombre' => $nombre,
        'porciones' => $porciones,
        'tiempo_estimado' => $tiempo,
        'nivel_dificultad' => $dificultad,
        'descripcion' => trim($data['descripcion'] ?? ''),
        'imagen' => validarRutaImagen($data['imagen'] ?? null),
        'calorias_porcion' => numeroONull($data['calorias_porcion'] ?? null),
        'proteinas_porcion' => numeroONull($data['proteinas_porcion'] ?? null),
        'carbohidratos_porcion' => numeroONull($data['carbohidratos_porcion'] ?? null),
        'grasas_porcion' => numeroONull($data['grasas_porcion'] ?? null),
    ];
}

function numeroONull($valor): ?float
{
    if ($valor === null || $valor === '' || !is_numeric($valor)) {
        return null;
    }
    $n = round((float)$valor, 2);
    return ($n < 0 || $n > 99999) ? null : $n;
}

/** Solo se aceptan imágenes que subió subir_imagen.php (uploads/receta_*.ext). */
function validarRutaImagen($ruta): ?string
{
    $ruta = trim((string)$ruta);
    if ($ruta === '') {
        return null;
    }
    if (!preg_match('#^uploads/receta_[a-f0-9]{32}\.(jpg|png|webp)$#', $ruta)) {
        throw new Exception("La imagen de la receta no es válida");
    }
    if (!is_file(__DIR__ . '/' . $ruta)) {
        throw new Exception("La imagen de la receta no se encontró en el servidor; súbela de nuevo");
    }
    return $ruta;
}

function borrarImagen(?string $ruta): void
{
    if ($ruta && preg_match('#^uploads/receta_[a-f0-9]{32}\.(jpg|png|webp)$#', $ruta)) {
        @unlink(__DIR__ . '/' . $ruta);
    }
}

/** Ingredientes, condimentos, pasos, utensilios y etiquetas de la receta. */
function guardarDetalleReceta(PDO $pdo, int $idReceta, array $data, array $generales): void
{
    $ingredientes = is_array($data['ingredientes'] ?? null) ? $data['ingredientes'] : [];
    $condimentos = is_array($data['condimentos'] ?? null) ? $data['condimentos'] : [];
    $pasos = is_array($data['pasos'] ?? null) ? $data['pasos'] : [];
    $utensilios = is_array($data['utensilios'] ?? null) ? $data['utensilios'] : [];
    $etiquetas = is_array($data['etiquetas'] ?? null) ? $data['etiquetas'] : [];

    if (empty($ingredientes)) {
        throw new Exception("Agrega al menos un ingrediente");
    }

    // ---------- Ingredientes ----------
    $stmt = $pdo->prepare("
        INSERT INTO receta_ingrediente (id_receta, id_ingrediente, cantidad, unidad, tipo)
        VALUES (?, ?, ?, ?, ?)
    ");
    $vistos = [];
    foreach ($ingredientes as $ing) {
        $id = (int)($ing['id_ingrediente'] ?? 0);
        $unidad = trim($ing['unidad'] ?? '');
        $cantidad = (float)($ing['cantidad'] ?? 0);
        $tipo = ($ing['tipo'] ?? '') === 'complementario' ? 'complementario' : 'principal';

        if ($id <= 0) throw new Exception("Se recibió un ingrediente inválido");
        if (isset($vistos[$id])) throw new Exception("Un ingrediente está repetido en la receta");
        $vistos[$id] = true;
        if ($unidad === '') throw new Exception("La unidad de un ingrediente es obligatoria");
        validarCantidad($cantidad, $unidad);

        $error = validarUnidad($pdo, 'ingrediente', $id, $unidad, 'receta');
        if ($error !== null) throw new Exception($error);

        $stmt->execute([$idReceta, $id, $cantidad, $unidad, $tipo]);
    }

    // ---------- Condimentos ----------
    $stmt = $pdo->prepare("
        INSERT INTO receta_especia (id_receta, id_especia, cantidad, unidad) VALUES (?, ?, ?, ?)
    ");
    $vistos = [];
    foreach ($condimentos as $c) {
        $id = (int)($c['id_especia'] ?? ($c['id_condimento'] ?? 0));
        $unidad = trim($c['unidad'] ?? '');
        $cantidad = (float)($c['cantidad'] ?? 0);

        if ($id <= 0) throw new Exception("Se recibió un condimento inválido");
        if (isset($vistos[$id])) throw new Exception("Un condimento está repetido en la receta");
        $vistos[$id] = true;
        if ($unidad === '') throw new Exception("La unidad de un condimento es obligatoria");
        validarCantidad($cantidad, $unidad);

        $error = validarUnidad($pdo, 'especia', $id, $unidad, 'receta');
        if ($error !== null) throw new Exception($error);

        $stmt->execute([$idReceta, $id, $cantidad, $unidad]);
    }

    // ---------- Pasos ----------
    $stmt = $pdo->prepare("INSERT INTO paso (id_receta, numero_paso, descripcion) VALUES (?, ?, ?)");
    $numero = 1;
    foreach ($pasos as $paso) {
        $texto = trim(is_string($paso) ? $paso : ($paso['descripcion'] ?? ''));
        if ($texto === '') continue;
        $stmt->execute([$idReceta, $numero++, $texto]);
    }
    if ($numero === 1) {
        throw new Exception("Agrega al menos un paso");
    }

    // ---------- Utensilios (del catálogo) ----------
    $stmt = $pdo->prepare("
        INSERT IGNORE INTO receta_utensilio (id_receta, id_utensilio)
        SELECT ?, id_utensilio FROM utensilio WHERE id_utensilio = ?
    ");
    foreach ($utensilios as $u) {
        $id = (int)(is_array($u) ? ($u['id_utensilio'] ?? 0) : $u);
        if ($id > 0) $stmt->execute([$idReceta, $id]);
    }

    // ---------- Etiquetas ----------
    guardarEtiquetas($pdo, $idReceta, $etiquetas, $generales);
}

/** "Al gusto" permite cantidad 0; cualquier otra unidad necesita cantidad > 0. */
function validarCantidad(float $cantidad, string $unidad): void
{
    if ($unidad === 'Al gusto') {
        if ($cantidad < 0) throw new Exception("La cantidad no puede ser negativa");
        return;
    }
    if ($cantidad <= 0 || $cantidad > 99999) {
        throw new Exception("La cantidad de cada ingrediente debe ser mayor a 0");
    }
}

/** Etiquetas automáticas (las decide el servidor) + etiquetas que escribió el usuario. */
function guardarEtiquetas(PDO $pdo, int $idReceta, array $etiquetasUsuario, array $g): void
{
    $nombres = [];

    if ($g['calorias_porcion'] !== null && $g['calorias_porcion'] > 0
            && $g['calorias_porcion'] <= SALUDABLE_KCAL_MAX) {
        $nombres[] = 'Saludable';
    }
    if ($g['proteinas_porcion'] !== null && $g['proteinas_porcion'] >= ALTA_PROTEINA_G_MIN) {
        $nombres[] = 'Alta en proteína';
    }
    if ($g['tiempo_estimado'] <= RAPIDA_MIN_MAX) {
        $nombres[] = 'Rápida';
    }

    // Las automáticas no se pueden poner a mano
    $automaticas = $pdo->query("SELECT LOWER(nombre) FROM etiqueta WHERE automatica = 1")->fetchAll(PDO::FETCH_COLUMN);

    $agregadas = 0;
    foreach ($etiquetasUsuario as $e) {
        $e = trim(preg_replace('/\s+/', ' ', (string)$e));
        $e = mb_strtoupper(mb_substr($e, 0, 1)) . mb_substr($e, 1);
        if ($e === '' || mb_strlen($e) > 40) continue;
        if (in_array(mb_strtolower($e), $automaticas, true)) continue;
        if (in_array(mb_strtolower($e), array_map('mb_strtolower', $nombres), true)) continue;
        $nombres[] = $e;
        if (++$agregadas >= MAX_ETIQUETAS_USUARIO) break;
    }

    $crear = $pdo->prepare("INSERT IGNORE INTO etiqueta (nombre, automatica) VALUES (?, 0)");
    $ligar = $pdo->prepare("
        INSERT IGNORE INTO receta_etiqueta (id_receta, id_etiqueta)
        SELECT ?, id_etiqueta FROM etiqueta WHERE nombre = ?
    ");
    foreach ($nombres as $n) {
        $crear->execute([$n]);
        $ligar->execute([$idReceta, $n]);
    }
}

/** Borra todo lo que cuelga de la receta (se usa antes de volver a guardarlo al editar). */
function borrarDetalleReceta(PDO $pdo, int $idReceta): void
{
    foreach (['receta_ingrediente', 'receta_especia', 'paso', 'receta_utensilio', 'receta_etiqueta'] as $tabla) {
        $pdo->prepare("DELETE FROM $tabla WHERE id_receta = ?")->execute([$idReceta]);
    }
}

/** Lee el JSON del cuerpo de la petición. */
function leerJsonEntrada(): array
{
    $data = json_decode(file_get_contents('php://input'), true);
    if (!is_array($data)) {
        responderJson(400, ["success" => false, "mensaje" => "El JSON recibido no es válido"]);
    }
    return $data;
}
