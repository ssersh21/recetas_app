<?php

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/unidades_helper.php';

header("Content-Type: application/json; charset=UTF-8");

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    http_response_code(500);

    echo json_encode([
        "error" => true,
        "mensaje" => "Error de conexión a la base de datos"
    ]);

    exit;
}

$busqueda = isset($_GET["buscar"])
    ? trim($_GET["buscar"])
    : "";

if ($busqueda === "") {

    $sql = "
        SELECT
            id_ingrediente,
            nombre
        FROM ingrediente
        ORDER BY nombre
        LIMIT 50
    ";

    $stmt = $pdo->prepare($sql);

} else {

    $sql = "
        SELECT
            id_ingrediente,
            nombre
        FROM ingrediente
        WHERE nombre LIKE :buscar
        ORDER BY nombre
        LIMIT 50
    ";

    $stmt = $pdo->prepare($sql);
    $stmt->bindValue(":buscar", "%" . $busqueda . "%", PDO::PARAM_STR);
}

$stmt->execute();
$filas = $stmt->fetchAll();

// Medidas compatibles de cada ingrediente (configuradas en la BD)
$ids = array_column($filas, "id_ingrediente");
$unidadesAlacena = obtenerUnidadesPorAlimento($pdo, "ingrediente", $ids, "alacena");
$unidadesReceta = obtenerUnidadesPorAlimento($pdo, "ingrediente", $ids, "receta");

$ingredientes = array_map(static function ($fila) use ($unidadesAlacena, $unidadesReceta) {
    $id = (int)$fila["id_ingrediente"];
    return [
        "id_ingrediente" => $id,
        "nombre" => $fila["nombre"],
        "unidades_alacena" => $unidadesAlacena[$id] ?? [],
        "unidades_receta" => $unidadesReceta[$id] ?? []
    ];
}, $filas);

echo json_encode($ingredientes, JSON_UNESCAPED_UNICODE);
?>
