<?php

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/unidades_helper.php';

header('Content-Type: application/json; charset=utf-8');

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    echo json_encode([]);
    exit;
}

$buscar = isset($_GET['buscar']) ? trim($_GET['buscar']) : "";

$tables = ['especia', 'especias', 'condimento', 'condimentos'];
$condimentos = [];

foreach ($tables as $table) {
    try {
        if ($buscar === "") {
            $sql = "SELECT * FROM `$table` ORDER BY 1 ASC LIMIT 50";
            $stmt = $pdo->prepare($sql);
        } else {
            $sql = "SELECT * FROM `$table` WHERE `nombre` LIKE :buscar ORDER BY `nombre` ASC LIMIT 50";
            $stmt = $pdo->prepare($sql);
            $stmt->bindValue(':buscar', '%' . $buscar . '%', PDO::PARAM_STR);
        }

        $stmt->execute();
        $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

        if (!empty($rows)) {
            foreach ($rows as $row) {
                // Obtener ID (primera columna o id_condimento / id_especia / id)
                $id = 0;
                if (isset($row['id_condimento'])) $id = $row['id_condimento'];
                elseif (isset($row['id_especia'])) $id = $row['id_especia'];
                elseif (isset($row['id_ingrediente'])) $id = $row['id_ingrediente'];
                elseif (isset($row['id'])) $id = $row['id'];
                else $id = reset($row);

                // Obtener Nombre
                $nombre = isset($row['nombre']) ? $row['nombre'] : (isset($row['nombre_especia']) ? $row['nombre_especia'] : end($row));

                $condimentos[] = [
                    "id_condimento" => (int)$id,
                    "id_ingrediente" => (int)$id,
                    "nombre" => (string)$nombre
                ];
            }
            $tablaEncontrada = $table;
            break; // Se encontraron resultados en esta tabla
        }
    } catch (Exception $e) {
        continue;
    }
}

// Medidas compatibles de cada especia (configuradas en la BD)
if (isset($tablaEncontrada) && $tablaEncontrada === 'especia' && !empty($condimentos)) {
    try {
        $ids = array_column($condimentos, 'id_condimento');
        $unidadesAlacena = obtenerUnidadesPorAlimento($pdo, 'especia', $ids, 'alacena');
        $unidadesReceta = obtenerUnidadesPorAlimento($pdo, 'especia', $ids, 'receta');
        foreach ($condimentos as &$c) {
            $c['unidades_alacena'] = $unidadesAlacena[$c['id_condimento']] ?? [];
            $c['unidades_receta'] = $unidadesReceta[$c['id_condimento']] ?? [];
        }
        unset($c);
    } catch (Exception $e) {
        // Si las tablas de medidas no existen, se devuelve la lista sin unidades
    }
}

echo json_encode($condimentos, JSON_UNESCAPED_UNICODE);
?>
