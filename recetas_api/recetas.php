<?php

/**
 * Lista las recetas con sus ingredientes, condimentos y pasos.
 * Soporta consulta pública y consulta de administración.
 *
 * Parámetros opcionales (GET):
 *   buscar         texto a buscar en el nombre de la receta
 *   id             devuelve solo la receta con ese id
 *   admin          si es 1, omite los filtros de estado/visibilidad
 *   min_reportes   devuelve solo recetas con reportes >= valor (ej. 5)
 *   limite         máximo de recetas (por defecto 50, máximo 100)
 */

require_once __DIR__ . '/conexion.php';

header('Content-Type: application/json; charset=utf-8');

try {

    $pdo = obtenerConexion();

    $buscar = trim($_GET['buscar'] ?? '');
    $idReceta = isset($_GET['id']) ? (int)$_GET['id'] : 0;
    $isAdmin = isset($_GET['admin']) && $_GET['admin'] == '1';
    $minReportes = isset($_GET['min_reportes']) ? (int)$_GET['min_reportes'] : 0;
    $limite = (int)($_GET['limite'] ?? 50);
    $limite = max(1, min(100, $limite));

    // Los reportes de los usuarios se guardan en reporte_receta (feedback_receta.php)
    $joinReporte = "LEFT JOIN reporte_receta rep ON rep.id_receta = r.id_receta";
    $colReportes = "COUNT(DISTINCT rep.id_reporte) AS total_reportes";

    $sql = "
        SELECT
            r.id_receta,
            r.nombre,
            r.porciones,
            r.tiempo_estimado,
            r.nivel_dificultad,
            r.imagen,
            r.descripcion,
            COALESCE(AVG(c.calificacion), 0) AS calificacion_promedio,
            COUNT(DISTINCT c.id_calificacion) AS total_calificaciones,
            $colReportes
        FROM receta r
        LEFT JOIN calificacion c ON c.id_receta = r.id_receta
        $joinReporte
        WHERE 1=1
    ";

    $params = [];

    if (!$isAdmin) {
        $sql .= " AND (r.estado = 'publicada' OR r.estado IS NULL) AND (r.visibilidad = 'publico' OR r.visibilidad IS NULL)";
    }

    if ($idReceta > 0) {
        $sql .= " AND r.id_receta = :id";
        $params[':id'] = $idReceta;
    }

    if ($buscar !== '') {
        $sql .= " AND r.nombre LIKE :buscar";
        $params[':buscar'] = '%' . $buscar . '%';
    }

    $sql .= " GROUP BY r.id_receta";

    if ($minReportes > 0) {
        $sql .= " HAVING total_reportes >= $minReportes";
    }

    $sql .= " ORDER BY r.id_receta DESC LIMIT $limite";

    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $recetas = $stmt->fetchAll();

    if (count($recetas) === 0) {
        echo json_encode([], JSON_UNESCAPED_UNICODE);
        exit;
    }

    $ids = array_map(fn($r) => (int)$r['id_receta'], $recetas);
    $marcas = implode(',', array_fill(0, count($ids), '?'));

    // Ingredientes
    $ingredientes = [];
    try {
        $stmt = $pdo->prepare("
            SELECT ri.id_receta, i.id_ingrediente, i.nombre,
                   ri.cantidad, ri.unidad, ri.tipo
            FROM receta_ingrediente ri
            JOIN ingrediente i ON i.id_ingrediente = ri.id_ingrediente
            WHERE ri.id_receta IN ($marcas)
            ORDER BY CASE WHEN ri.tipo = 'principal' THEN 0 ELSE 1 END, i.nombre
        ");
        $stmt->execute($ids);
        foreach ($stmt->fetchAll() as $f) {
            $ingredientes[(int)$f['id_receta']][] = [
                "id_ingrediente" => (int)$f['id_ingrediente'],
                "nombre" => $f['nombre'],
                "cantidad" => (float)$f['cantidad'],
                "unidad" => $f['unidad'],
                "tipo" => $f['tipo']
            ];
        }
    } catch (Exception $e) {}

    // Condimentos
    $condimentos = [];
    try {
        $stmt = $pdo->prepare("
            SELECT re.id_receta, e.id_especia, e.nombre,
                   re.cantidad, re.unidad
            FROM receta_especia re
            JOIN especia e ON e.id_especia = re.id_especia
            WHERE re.id_receta IN ($marcas)
            ORDER BY e.nombre
        ");
        $stmt->execute($ids);
        foreach ($stmt->fetchAll() as $f) {
            $condimentos[(int)$f['id_receta']][] = [
                "id_especia" => (int)$f['id_especia'],
                "nombre" => $f['nombre'],
                "cantidad" => $f['cantidad'] === null ? 0.0 : (float)$f['cantidad'],
                "unidad" => $f['unidad'] ?? ""
            ];
        }
    } catch (Exception $e) {}

    // Pasos
    $pasos = [];
    try {
        $stmt = $pdo->prepare("
            SELECT id_receta, numero_paso, descripcion
            FROM paso
            WHERE id_receta IN ($marcas)
            ORDER BY id_receta, numero_paso
        ");
        $stmt->execute($ids);
        foreach ($stmt->fetchAll() as $f) {
            $pasos[(int)$f['id_receta']][] = [
                "numero_paso" => (int)$f['numero_paso'],
                "descripcion" => $f['descripcion']
            ];
        }
    } catch (Exception $e) {}

    // Respuesta
    $respuesta = [];

    foreach ($recetas as $r) {
        $id = (int)$r['id_receta'];

        $respuesta[] = [
            "id_receta" => $id,
            "nombre" => $r['nombre'],
            "porciones" => (int)$r['porciones'],
            "tiempo_estimado" => (int)$r['tiempo_estimado'],
            "nivel_dificultad" => $r['nivel_dificultad'] ?? "Intermedio",
            "imagen" => $r['imagen'] ?? "",
            "descripcion" => $r['descripcion'] ?? "",
            "calificacion_promedio" => round((float)$r['calificacion_promedio'], 1),
            "total_calificaciones" => (int)$r['total_calificaciones'],
            "total_reportes" => isset($r['total_reportes']) ? (int)$r['total_reportes'] : 0,
            "ingredientes" => $ingredientes[$id] ?? [],
            "condimentos" => $condimentos[$id] ?? [],
            "pasos" => $pasos[$id] ?? []
        ];
    }

    echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {

    http_response_code(500);

    echo json_encode([
        "error" => true,
        "mensaje" => "Error al consultar las recetas: " . $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}
?>
