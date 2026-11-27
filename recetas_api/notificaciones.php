<?php
/**
 * Avisos del usuario (tabla notificacion).
 *
 * GET  ?id_usuario=1            -> avisos sin leer (más recientes primero, máx. 20)
 *      [{"id_notificacion":1,"tipo":"moderacion","titulo":"...","mensaje":"...","id_receta":6,"fecha":"..."}]
 * POST id_usuario, accion=leidas, ids="1,2,3"   -> los marca como leídos
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header('Content-Type: application/json; charset=utf-8');

$esPost = $_SERVER['REQUEST_METHOD'] === 'POST';
$entrada = $esPost ? $_POST : $_GET;
$idUsuario = (int)($entrada['id_usuario'] ?? 0);

if ($idUsuario <= 0) {
    responderJson(400, ["success" => false, "message" => "Usuario no válido"]);
}

try {
    $pdo = obtenerConexion();

    if (!$esPost) {
        $stmt = $pdo->prepare("
            SELECT id_notificacion, tipo, titulo, mensaje, id_receta, fecha
            FROM notificacion
            WHERE id_usuario = ? AND leida = 0
            ORDER BY fecha DESC, id_notificacion DESC
            LIMIT 20
        ");
        $stmt->execute([$idUsuario]);
        $lista = array_map(static fn($f) => [
            "id_notificacion" => (int)$f['id_notificacion'],
            "tipo" => $f['tipo'],
            "titulo" => $f['titulo'],
            "mensaje" => $f['mensaje'],
            "id_receta" => $f['id_receta'] === null ? null : (int)$f['id_receta'],
            "fecha" => $f['fecha']
        ], $stmt->fetchAll());

        if (ob_get_level() > 0) ob_end_clean();
        echo json_encode($lista, JSON_UNESCAPED_UNICODE);
        exit;
    }

    if (($_POST['accion'] ?? '') !== 'leidas') {
        responderJson(400, ["success" => false, "message" => "Acción no válida"]);
    }

    $ids = array_values(array_filter(array_map('intval', explode(',', (string)($_POST['ids'] ?? ''))),
        static fn($i) => $i > 0));
    if ($ids) {
        $marcas = implode(',', array_fill(0, count($ids), '?'));
        $stmt = $pdo->prepare("UPDATE notificacion SET leida = 1 WHERE id_usuario = ? AND id_notificacion IN ($marcas)");
        $stmt->execute(array_merge([$idUsuario], $ids));
    }
    responderJson(200, ["success" => true, "message" => "Avisos marcados como leídos"]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
