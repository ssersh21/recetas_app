<?php
/**
 * Utensilios poco comunes (catálogo) y los que tiene cada usuario.
 *
 * GET                    -> catálogo: [{"id_utensilio":1,"nombre":"Freidora de aire","tengo":false}]
 * GET  ?id_usuario=1     -> catálogo con "tengo": true en los que el usuario marcó
 * POST id_usuario, utensilios = "1,5,12"   (reemplaza la lista completa del usuario; "" = ninguno)
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header('Content-Type: application/json; charset=utf-8');

try {
    $pdo = obtenerConexion();

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $idUsuario = (int)($_GET['id_usuario'] ?? 0);
        $stmt = $pdo->prepare("
            SELECT u.id_utensilio, u.nombre,
                   (uu.id_usuario IS NOT NULL) AS tengo
            FROM utensilio u
            LEFT JOIN usuario_utensilio uu
                   ON uu.id_utensilio = u.id_utensilio AND uu.id_usuario = ?
            ORDER BY u.nombre
        ");
        $stmt->execute([$idUsuario]);
        $lista = array_map(static fn($f) => [
            "id_utensilio" => (int)$f['id_utensilio'],
            "nombre" => $f['nombre'],
            "tengo" => (bool)$f['tengo']
        ], $stmt->fetchAll());

        if (ob_get_level() > 0) ob_end_clean();
        echo json_encode($lista, JSON_UNESCAPED_UNICODE);
        exit;
    }

    $idUsuario = (int)($_POST['id_usuario'] ?? 0);
    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if ($idUsuario <= 0 || !$stmt->fetchColumn()) {
        responderJson(401, ["success" => false, "message" => "Inicia sesión para guardar tus utensilios"]);
    }

    $ids = array_values(array_unique(array_filter(
        array_map('intval', explode(',', (string)($_POST['utensilios'] ?? ''))),
        static fn($i) => $i > 0
    )));

    $pdo->beginTransaction();
    $pdo->prepare("DELETE FROM usuario_utensilio WHERE id_usuario = ?")->execute([$idUsuario]);
    $stmt = $pdo->prepare("
        INSERT IGNORE INTO usuario_utensilio (id_usuario, id_utensilio)
        SELECT ?, id_utensilio FROM utensilio WHERE id_utensilio = ?
    ");
    foreach ($ids as $id) {
        $stmt->execute([$idUsuario, $id]);
    }
    $pdo->commit();

    responderJson(200, ["success" => true, "message" => "Utensilios guardados"]);
} catch (PDOException $e) {
    if (isset($pdo) && $pdo->inTransaction()) $pdo->rollBack();
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
