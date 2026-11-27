<?php
/**
 * Perfil del usuario (tabla perfil_usuario, la crea guardar_perfil.php).
 * GET ?id_usuario=1
 * -> {"success": true, "perfil": {...}} o {"success": false, "message": "..."}
 */
header("Content-Type: application/json; charset=UTF-8");

require_once __DIR__ . '/conexion.php';

$id_usuario = intval($_GET['id_usuario'] ?? 0);

if ($id_usuario <= 0) {
    echo json_encode(["success" => false, "message" => "id_usuario inválido"]);
    exit();
}

try {
    // Antes usaba $conexion / $conn, que conexion.php no define: siempre respondía "Conexión no disponible"
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT * FROM perfil_usuario WHERE id_usuario = ? LIMIT 1");
    $stmt->execute([$id_usuario]);
    $perfil = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($perfil) {
        foreach (['alergias', 'aversiones'] as $campo) {
            if (isset($perfil[$campo]) && is_string($perfil[$campo])) {
                $perfil[$campo] = json_decode($perfil[$campo], true) ?: [];
            }
        }
        echo json_encode(["success" => true, "perfil" => $perfil], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["success" => false, "message" => "No se encontró perfil para este usuario"], JSON_UNESCAPED_UNICODE);
    }
} catch (PDOException $e) {
    // La tabla perfil_usuario se crea al guardar el primer perfil
    if ($e->getCode() === '42S02') {
        echo json_encode(["success" => false, "message" => "No se encontró perfil para este usuario"], JSON_UNESCAPED_UNICODE);
        exit();
    }
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Error de conexión con la base de datos"], JSON_UNESCAPED_UNICODE);
}
