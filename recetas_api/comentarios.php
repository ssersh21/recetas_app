<?php
/**
 * Comentarios de una receta (tabla comentario).
 *
 * GET  ?id_receta=5
 *      -> [{"id_comentario":1,"id_usuario":2,"autor":"Ana","comentario":"...","fecha":"2026-11-27 10:00:00"}]
 * POST accion=crear     id_usuario, id_receta, comentario (máx. 500 caracteres)
 * POST accion=eliminar  id_usuario, id_comentario   (solo el autor)
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header('Content-Type: application/json; charset=utf-8');

const MAX_COMENTARIO = 500;

try {
    $pdo = obtenerConexion();

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $idReceta = (int)($_GET['id_receta'] ?? 0);
        if ($idReceta <= 0) {
            responderJson(400, ["success" => false, "message" => "Receta no válida"]);
        }
        $stmt = $pdo->prepare("
            SELECT c.id_comentario, c.id_usuario, u.nombre AS autor, c.comentario, c.fecha
            FROM comentario c
            JOIN usuario u ON u.id_usuario = c.id_usuario
            WHERE c.id_receta = ?
            ORDER BY c.fecha DESC, c.id_comentario DESC
            LIMIT 200
        ");
        $stmt->execute([$idReceta]);
        $lista = array_map(static fn($f) => [
            "id_comentario" => (int)$f['id_comentario'],
            "id_usuario" => (int)$f['id_usuario'],
            "autor" => $f['autor'],
            "comentario" => $f['comentario'],
            "fecha" => $f['fecha']
        ], $stmt->fetchAll());

        if (ob_get_level() > 0) ob_end_clean();
        echo json_encode($lista, JSON_UNESCAPED_UNICODE);
        exit;
    }

    $accion = $_POST['accion'] ?? '';
    $idUsuario = (int)($_POST['id_usuario'] ?? 0);

    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if ($idUsuario <= 0 || !$stmt->fetchColumn()) {
        responderJson(401, ["success" => false, "message" => "Inicia sesión para comentar"]);
    }

    if ($accion === 'crear') {
        $idReceta = (int)($_POST['id_receta'] ?? 0);
        $crudo = (string)($_POST['comentario'] ?? '');
        if (!mb_check_encoding($crudo, 'UTF-8')) {
            responderJson(400, ["success" => false, "message" => "El comentario tiene caracteres no válidos"]);
        }
        $texto = trim(preg_replace('/\s+/u', ' ', $crudo));

        if ($texto === '') {
            responderJson(400, ["success" => false, "message" => "Escribe un comentario"]);
        }
        if (mb_strlen($texto) > MAX_COMENTARIO) {
            responderJson(400, ["success" => false, "message" => "El comentario puede tener máximo " . MAX_COMENTARIO . " caracteres"]);
        }
        $stmt = $pdo->prepare("SELECT 1 FROM receta WHERE id_receta = ?");
        $stmt->execute([$idReceta]);
        if (!$stmt->fetchColumn()) {
            responderJson(404, ["success" => false, "message" => "La receta no existe"]);
        }

        $stmt = $pdo->prepare("INSERT INTO comentario (id_usuario, id_receta, comentario) VALUES (?, ?, ?)");
        $stmt->execute([$idUsuario, $idReceta, $texto]);
        responderJson(201, ["success" => true, "message" => "Comentario publicado",
            "id_comentario" => (int)$pdo->lastInsertId()]);
    }

    if ($accion === 'eliminar') {
        $idComentario = (int)($_POST['id_comentario'] ?? 0);
        $stmt = $pdo->prepare("DELETE FROM comentario WHERE id_comentario = ? AND id_usuario = ?");
        $stmt->execute([$idComentario, $idUsuario]);
        if ($stmt->rowCount() === 0) {
            responderJson(403, ["success" => false, "message" => "Solo puedes borrar tus propios comentarios"]);
        }
        responderJson(200, ["success" => true, "message" => "Comentario eliminado"]);
    }

    responderJson(400, ["success" => false, "message" => "Acción no válida"]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
