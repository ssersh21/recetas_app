<?php

require_once __DIR__ . '/conexion.php';

header("Content-Type: application/json; charset=UTF-8");

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Error de conexión con la base de datos"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Método no permitido"]);
    exit;
}

$id_receta = isset($_POST["id_receta"]) ? (int)$_POST["id_receta"] : 0;

if ($id_receta <= 0) {
    http_response_code(400);
    echo json_encode(["success" => false, "message" => "ID de receta no válido"]);
    exit;
}

try {
    $sql = "DELETE FROM receta WHERE id_receta = :id";
    $stmt = $pdo->prepare($sql);
    $stmt->execute([":id" => $id_receta]);

    echo json_encode([
        "success" => true,
        "message" => "Receta dada de baja correctamente"
    ], JSON_UNESCAPED_UNICODE);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Error al dar de baja la receta: " . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}
?>
