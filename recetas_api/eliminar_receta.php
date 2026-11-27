<?php
/**
 * Elimina una receta propia (el administrador usa baja_receta.php).
 * POST: id_usuario, id_receta
 * Las llaves foráneas borran en cascada ingredientes, pasos, comentarios,
 * calificaciones, reportes, etiquetas y planes que la usaban.
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';
require_once __DIR__ . '/receta_helper.php';

header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responderJson(405, ["success" => false, "message" => "Método no permitido"]);
}

$idUsuario = (int)($_POST['id_usuario'] ?? 0);
$idReceta = (int)($_POST['id_receta'] ?? 0);

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT id_usuario, imagen FROM receta WHERE id_receta = ?");
    $stmt->execute([$idReceta]);
    $receta = $stmt->fetch();

    if (!$receta) {
        responderJson(404, ["success" => false, "message" => "La receta no existe"]);
    }
    if ($idUsuario <= 0 || (int)$receta['id_usuario'] !== $idUsuario) {
        responderJson(403, ["success" => false, "message" => "Solo puedes eliminar tus propias recetas"]);
    }

    $pdo->prepare("DELETE FROM receta WHERE id_receta = ? AND id_usuario = ?")->execute([$idReceta, $idUsuario]);
    borrarImagen($receta['imagen']);

    responderJson(200, ["success" => true, "message" => "Receta eliminada"]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "No se pudo eliminar la receta"]);
}
