<?php
/**
 * Inicio de sesión.
 * POST: correo, contrasena
 * Respuesta: {"success": true, "message": "...", "usuario": {...}}
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

$correo = trim($_POST["correo"] ?? "");
$contrasena = $_POST["contrasena"] ?? ($_POST["password"] ?? "");

if ($correo === "" || $contrasena === "") {
    responderJson(400, ["success" => false, "message" => "Ingresa el correo y la contraseña"]);
}

try {
    $pdo = obtenerConexion();

    // Se busca solo por correo; la contraseña se compara con password_verify()
    $stmt = $pdo->prepare("SELECT * FROM usuario WHERE correo = :correo LIMIT 1");
    $stmt->execute([":correo" => $correo]);
    $fila = $stmt->fetch();

    if (!$fila || !verificarContrasena($pdo, $fila, $contrasena)) {
        responderJson(401, ["success" => false, "message" => "Correo o contraseña incorrectos"]);
    }

    responderJson(200, [
        "success" => true,
        "message" => "Inicio de sesión correcto",
        "usuario" => usuarioParaApp($fila)
    ]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
