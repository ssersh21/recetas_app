<?php
/**
 * Elimina la cuenta del usuario y todos sus datos.
 * POST: id_usuario, password (o contrasena)
 * Respuesta: {"success": true|false, "message": "..."}
 *
 * Las llaves foráneas de la BD tienen ON DELETE CASCADE hacia usuario y receta,
 * así que al borrar el usuario también se borran su perfil, alacena, utensilios,
 * recetas (con pasos, ingredientes, etc.), comentarios, calificaciones y reportes.
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header("Content-Type: application/json; charset=UTF-8");

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    responderJson(405, ["success" => false, "message" => "Método no permitido"]);
}

$idUsuario = (int)($_POST["id_usuario"] ?? 0);
$contrasena = $_POST["password"] ?? ($_POST["contrasena"] ?? "");

if ($idUsuario <= 0 || $contrasena === "") {
    responderJson(400, ["success" => false, "message" => "Faltan datos para eliminar la cuenta"]);
}

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT * FROM usuario WHERE id_usuario = :id LIMIT 1");
    $stmt->execute([':id' => $idUsuario]);
    $usuario = $stmt->fetch();

    if (!$usuario || !verificarContrasena($pdo, $usuario, $contrasena)) {
        responderJson(401, ["success" => false, "message" => "La contraseña no es correcta"]);
    }

    $pdo->beginTransaction();

    // perfil_usuario la crea guardar_perfil.php y no tiene llave foránea, se borra a mano
    try {
        $pdo->prepare("DELETE FROM perfil_usuario WHERE id_usuario = :id")->execute([':id' => $idUsuario]);
    } catch (PDOException $e) {
        // La tabla puede no existir todavía
    }

    $pdo->prepare("DELETE FROM usuario WHERE id_usuario = :id")->execute([':id' => $idUsuario]);

    $pdo->commit();

    responderJson(200, ["success" => true, "message" => "Tu cuenta y tus datos fueron eliminados"]);
} catch (PDOException $e) {
    if (isset($pdo) && $pdo->inTransaction()) {
        $pdo->rollBack();
    }
    responderJson(500, ["success" => false, "message" => "No se pudo eliminar la cuenta"]);
}
