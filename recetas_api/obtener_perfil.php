<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");
header("Content-Type: application/json; charset=UTF-8");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

if (file_exists(__DIR__ . "/conexion.php")) {
    require_once __DIR__ . "/conexion.php";
} elseif (file_exists(__DIR__ . "/db.php")) {
    require_once __DIR__ . "/db.php";
} else {
    try {
        $conexion = new PDO("mysql:host=localhost;dbname=recetas_bd;charset=utf8mb4", "root", "");
        $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        echo json_encode(["success" => false, "message" => "Error de conexión: " . $e->getMessage()]);
        exit();
    }
}

$id_usuario = intval($_GET['id_usuario'] ?? 0);

if ($id_usuario <= 0) {
    echo json_encode(["success" => false, "message" => "id_usuario inválido"]);
    exit();
}

try {
    if (isset($conexion) && $conexion instanceof PDO) {
        $stmt = $conexion->prepare("SELECT * FROM perfil_usuario WHERE id_usuario = ? LIMIT 1");
        $stmt->execute([$id_usuario]);
        $perfil = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($perfil) {
            if (isset($perfil['alergias']) && is_string($perfil['alergias'])) {
                $perfil['alergias'] = json_decode($perfil['alergias'], true) ?: [];
            }
            if (isset($perfil['aversiones']) && is_string($perfil['aversiones'])) {
                $perfil['aversiones'] = json_decode($perfil['aversiones'], true) ?: [];
            }
            echo json_encode(["success" => true, "perfil" => $perfil]);
        } else {
            echo json_encode(["success" => false, "message" => "No se encontró perfil para este usuario"]);
        }
    } elseif (isset($conn) && $conn instanceof mysqli) {
        $stmt = $conn->prepare("SELECT * FROM perfil_usuario WHERE id_usuario = ? LIMIT 1");
        $stmt->bind_param("i", $id_usuario);
        $stmt->execute();
        $result = $stmt->get_result();
        $perfil = $result->fetch_assoc();

        if ($perfil) {
            if (isset($perfil['alergias']) && is_string($perfil['alergias'])) {
                $perfil['alergias'] = json_decode($perfil['alergias'], true) ?: [];
            }
            if (isset($perfil['aversiones']) && is_string($perfil['aversiones'])) {
                $perfil['aversiones'] = json_decode($perfil['aversiones'], true) ?: [];
            }
            echo json_encode(["success" => true, "perfil" => $perfil]);
        } else {
            echo json_encode(["success" => false, "message" => "No se encontró perfil para este usuario"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Conexión no disponible"]);
    }
} catch (Exception $e) {
    echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
}
