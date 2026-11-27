<?php
/**
 * Crea una receta.
 * POST (JSON):
 * {
 *   "id_usuario": 1, "nombre": "...", "porciones": 4, "tiempo_estimado": 30,
 *   "nivel_dificultad": "principiante", "descripcion": "...",
 *   "imagen": "uploads/receta_xxx.jpg",              (de subir_imagen.php, opcional)
 *   "calorias_porcion": 350, "proteinas_porcion": 25,  (opcionales, los calcula la app)
 *   "carbohidratos_porcion": 30, "grasas_porcion": 10,
 *   "ingredientes": [{"id_ingrediente": 1, "cantidad": 2, "unidad": "Piezas", "tipo": "principal"}],
 *   "condimentos":  [{"id_especia": 3, "cantidad": 1, "unidad": "Cucharaditas"}],
 *   "pasos": ["Picar...", "Freír..."],
 *   "utensilios": [5, 12],
 *   "etiquetas": ["Mexicana", "Vegetariana"]
 * }
 * Respuesta: {"success": true, "mensaje": "...", "id_receta": 18}
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';
require_once __DIR__ . '/receta_helper.php';

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responderJson(405, ["success" => false, "mensaje" => "Método no permitido. Utiliza POST."]);
}

$data = leerJsonEntrada();
$idUsuario = (int)($data['id_usuario'] ?? 0);

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "mensaje" => "No se pudo conectar con la base de datos"]);
}

try {
    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if ($idUsuario <= 0 || !$stmt->fetchColumn()) {
        throw new Exception("El usuario no es válido");
    }

    $g = leerDatosGenerales($data);

    // Si la imagen quedó en la zona gris de SafeSearch, la receta espera a un administrador
    $moderacion = resultadoModeracion($pdo, $g['imagen'], $idUsuario);
    $estado = $moderacion === 'revision' ? 'pendiente_revision' : 'publicada';

    $pdo->beginTransaction();

    $stmt = $pdo->prepare("
        INSERT INTO receta (id_usuario, nombre, porciones, tiempo_estimado, nivel_dificultad,
                            imagen, descripcion, estado, visibilidad,
                            calorias_porcion, proteinas_porcion, carbohidratos_porcion, grasas_porcion)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'publico', ?, ?, ?, ?)
    ");
    $stmt->execute([
        $idUsuario, $g['nombre'], $g['porciones'], $g['tiempo_estimado'], $g['nivel_dificultad'],
        $g['imagen'], $g['descripcion'], $estado,
        $g['calorias_porcion'], $g['proteinas_porcion'], $g['carbohidratos_porcion'], $g['grasas_porcion']
    ]);
    $idReceta = (int)$pdo->lastInsertId();

    guardarDetalleReceta($pdo, $idReceta, $data, $g);
    ligarModeracionReceta($pdo, $g['imagen'], $idReceta);

    if ($estado === 'pendiente_revision') {
        notificarAdministradores($pdo, 'revision_pendiente', 'Imagen por revisar',
            "La receta \"{$g['nombre']}\" tiene una imagen en revisión.", $idReceta);
    }

    $pdo->commit();

    $mensaje = $estado === 'pendiente_revision'
        ? "Tu receta está en revisión, te notificaremos pronto"
        : "Receta guardada correctamente";
    responderJson(200, [
        "success" => true,
        "mensaje" => $mensaje,
        "message" => $mensaje,
        "estado" => $estado,
        "id_receta" => $idReceta
    ]);
} catch (Exception $e) {
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }
    $mensaje = $e instanceof PDOException ? "No se pudo guardar la receta" : $e->getMessage();
    responderJson(400, ["success" => false, "mensaje" => $mensaje, "message" => $mensaje]);
}
