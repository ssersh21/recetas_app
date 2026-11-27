<?php

/**
 * Medidas compatibles de un alimento.
 *
 * GET:
 *   tipo      'ingrediente' (por defecto) o 'especia'
 *   id        id del alimento   (o bien)
 *   nombre    nombre exacto del alimento
 *   contexto  'alacena' o 'receta' (por defecto 'receta')
 *
 * Respuesta: {"id": 135, "nombre": "Aguacate", "contexto": "receta", "unidades": ["Piezas", ...]}
 */

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/unidades_helper.php';

header('Content-Type: application/json; charset=utf-8');

try {
    $pdo = obtenerConexion();

    $tipo = ($_GET['tipo'] ?? 'ingrediente') === 'especia' ? 'especia' : 'ingrediente';
    $contexto = in_array($_GET['contexto'] ?? '', CONTEXTOS_UNIDAD, true) ? $_GET['contexto'] : 'receta';
    $id = isset($_GET['id']) ? (int)$_GET['id'] : 0;
    $nombre = trim($_GET['nombre'] ?? '');

    $tabla = $tipo === 'especia' ? 'especia' : 'ingrediente';
    $colId = $tipo === 'especia' ? 'id_especia' : 'id_ingrediente';

    if ($id > 0) {
        $stmt = $pdo->prepare("SELECT $colId AS id, nombre FROM $tabla WHERE $colId = ?");
        $stmt->execute([$id]);
    } elseif ($nombre !== '') {
        $stmt = $pdo->prepare("SELECT $colId AS id, nombre FROM $tabla WHERE nombre = ?");
        $stmt->execute([$nombre]);
    } else {
        // Sin alimento: unidades por defecto del contexto
        echo json_encode([
            "id" => 0,
            "nombre" => "",
            "contexto" => $contexto,
            "unidades" => obtenerUnidadesPorDefecto($pdo, $contexto)
        ], JSON_UNESCAPED_UNICODE);
        exit;
    }

    $alimento = $stmt->fetch();
    if (!$alimento) {
        // Alimento no registrado: se ofrecen las unidades por defecto
        echo json_encode([
            "id" => 0,
            "nombre" => $nombre,
            "encontrado" => false,
            "contexto" => $contexto,
            "unidades" => obtenerUnidadesPorDefecto($pdo, $contexto)
        ], JSON_UNESCAPED_UNICODE);
        exit;
    }

    echo json_encode([
        "id" => (int)$alimento['id'],
        "nombre" => $alimento['nombre'],
        "contexto" => $contexto,
        "unidades" => obtenerUnidades($pdo, $tipo, (int)$alimento['id'], $contexto)
    ], JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => true, "mensaje" => "Error de conexión a la base de datos"], JSON_UNESCAPED_UNICODE);
}
