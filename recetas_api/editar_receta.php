<?php
/**
 * Edita una receta propia.
 * POST (JSON): los mismos campos que guardar_receta.php + "id_receta".
 * Solo el dueño de la receta puede editarla. Se reemplazan ingredientes,
 * condimentos, pasos, utensilios y etiquetas por los que se envían.
 * Si se cambia la imagen, la anterior se borra del servidor.
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
$idReceta = (int)($data['id_receta'] ?? 0);

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "mensaje" => "No se pudo conectar con la base de datos"]);
}

try {
    $stmt = $pdo->prepare("SELECT id_usuario, imagen FROM receta WHERE id_receta = ?");
    $stmt->execute([$idReceta]);
    $actual = $stmt->fetch();

    if (!$actual) {
        responderJson(404, ["success" => false, "mensaje" => "La receta no existe", "message" => "La receta no existe"]);
    }
    if ($idUsuario <= 0 || (int)$actual['id_usuario'] !== $idUsuario) {
        responderJson(403, ["success" => false, "mensaje" => "Solo puedes editar tus propias recetas",
            "message" => "Solo puedes editar tus propias recetas"]);
    }

    $g = leerDatosGenerales($data);

    $pdo->beginTransaction();

    $stmt = $pdo->prepare("
        UPDATE receta SET nombre = ?, porciones = ?, tiempo_estimado = ?, nivel_dificultad = ?,
               imagen = ?, descripcion = ?,
               calorias_porcion = ?, proteinas_porcion = ?, carbohidratos_porcion = ?, grasas_porcion = ?
        WHERE id_receta = ? AND id_usuario = ?
    ");
    $stmt->execute([
        $g['nombre'], $g['porciones'], $g['tiempo_estimado'], $g['nivel_dificultad'],
        $g['imagen'], $g['descripcion'],
        $g['calorias_porcion'], $g['proteinas_porcion'], $g['carbohidratos_porcion'], $g['grasas_porcion'],
        $idReceta, $idUsuario
    ]);

    borrarDetalleReceta($pdo, $idReceta);
    guardarDetalleReceta($pdo, $idReceta, $data, $g);

    $pdo->commit();

    if ($actual['imagen'] && $actual['imagen'] !== $g['imagen']) {
        borrarImagen($actual['imagen']);
    }

    responderJson(200, [
        "success" => true,
        "mensaje" => "Receta actualizada",
        "message" => "Receta actualizada",
        "id_receta" => $idReceta
    ]);
} catch (Exception $e) {
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }
    $mensaje = $e instanceof PDOException ? "No se pudo actualizar la receta" : $e->getMessage();
    responderJson(400, ["success" => false, "mensaje" => $mensaje, "message" => $mensaje]);
}
