<?php
/**
 * Registro de usuarios.
 * POST: nombre, correo, contrasena (nombre_usuario es opcional)
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

$nombre = trim($_POST["nombre"] ?? "");
if ($nombre === "") {
    $nombre = trim($_POST["nombre_usuario"] ?? "");
}
$correo = strtolower(trim($_POST["correo"] ?? ""));
$contrasena = $_POST["contrasena"] ?? ($_POST["password"] ?? "");

if ($nombre === "" || $correo === "" || $contrasena === "") {
    responderJson(400, ["success" => false, "message" => "Nombre, correo y contraseña son obligatorios"]);
}
if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
    responderJson(400, ["success" => false, "message" => "El correo no es válido"]);
}
if (mb_strlen($contrasena) < 6) {
    responderJson(400, ["success" => false, "message" => "La contraseña debe tener al menos 6 caracteres"]);
}
if (mb_strlen($nombre) > 100 || mb_strlen($correo) > 150) {
    responderJson(400, ["success" => false, "message" => "El nombre o el correo son demasiado largos"]);
}

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE correo = :correo LIMIT 1");
    $stmt->execute([':correo' => $correo]);
    if ($stmt->fetchColumn()) {
        responderJson(409, ["success" => false, "message" => "Ya existe una cuenta con ese correo"]);
    }

    // Todas las cuentas nuevas son de rol "usuario"; los administradores se asignan en la BD
    $stmt = $pdo->prepare("
        INSERT INTO usuario (nombre, correo, contrasena, rol)
        VALUES (:nombre, :correo, :contrasena, 'usuario')
    ");
    $stmt->execute([
        ':nombre' => $nombre,
        ':correo' => $correo,
        ':contrasena' => password_hash($contrasena, PASSWORD_DEFAULT)
    ]);

    $id = (int)$pdo->lastInsertId();

    responderJson(201, [
        "success" => true,
        "message" => "Cuenta creada correctamente",
        "usuario" => usuarioParaApp([
            "id_usuario" => $id,
            "nombre" => $nombre,
            "correo" => $correo,
            "rol" => "usuario"
        ])
    ]);
} catch (PDOException $e) {
    // 23000 = correo duplicado (por si dos registros llegan al mismo tiempo)
    if ($e->getCode() === '23000') {
        responderJson(409, ["success" => false, "message" => "Ya existe una cuenta con ese correo"]);
    }
    responderJson(500, ["success" => false, "message" => "No se pudo crear la cuenta"]);
}
