<?php

/**
 * Consulta de medidas compatibles por alimento.
 * Las reglas viven en la BD (tablas unidad_medida, ingrediente_unidad, especia_unidad);
 * este archivo solo las lee.
 */

const CONTEXTOS_UNIDAD = ['alacena', 'receta'];

/**
 * Devuelve [id => [unidades...]] para varios alimentos de una sola consulta.
 * Si un alimento no tiene medidas configuradas, recibe las unidades
 * marcadas como "por defecto" para ese contexto.
 *
 * @param string $tipo 'ingrediente' o 'especia'
 */
function obtenerUnidadesPorAlimento(PDO $pdo, string $tipo, array $ids, string $contexto): array
{
    [$tablaRel, $colId] = tablaRelacionUnidad($tipo);
    $contexto = in_array($contexto, CONTEXTOS_UNIDAD, true) ? $contexto : 'receta';

    $ids = array_values(array_unique(array_filter(array_map('intval', $ids), fn($i) => $i > 0)));
    $resultado = [];
    if (empty($ids)) {
        return $resultado;
    }

    $marcadores = implode(',', array_fill(0, count($ids), '?'));
    $stmt = $pdo->prepare("
        SELECT r.$colId AS id, u.nombre
        FROM $tablaRel r
        JOIN unidad_medida u ON u.id_unidad = r.id_unidad
        WHERE r.contexto = ? AND r.$colId IN ($marcadores)
        ORDER BY r.orden, u.orden, u.nombre
    ");
    $stmt->execute(array_merge([$contexto], $ids));

    foreach ($stmt->fetchAll(PDO::FETCH_ASSOC) as $fila) {
        $resultado[(int)$fila['id']][] = $fila['nombre'];
    }

    $faltantes = array_diff($ids, array_keys($resultado));
    if (!empty($faltantes)) {
        $porDefecto = obtenerUnidadesPorDefecto($pdo, $contexto);
        foreach ($faltantes as $id) {
            $resultado[$id] = $porDefecto;
        }
    }

    return $resultado;
}

function obtenerUnidades(PDO $pdo, string $tipo, int $id, string $contexto): array
{
    return obtenerUnidadesPorAlimento($pdo, $tipo, [$id], $contexto)[$id] ?? [];
}

function obtenerUnidadesPorDefecto(PDO $pdo, string $contexto): array
{
    $columna = $contexto === 'alacena' ? 'defecto_alacena' : 'defecto_receta';
    return $pdo->query("SELECT nombre FROM unidad_medida WHERE $columna = 1 ORDER BY orden, nombre")
        ->fetchAll(PDO::FETCH_COLUMN);
}

/**
 * Devuelve null si la unidad es válida, o el mensaje de error.
 */
function validarUnidad(PDO $pdo, string $tipo, int $id, string $unidad, string $contexto): ?string
{
    $permitidas = obtenerUnidades($pdo, $tipo, $id, $contexto);
    if (in_array($unidad, $permitidas, true)) {
        return null;
    }

    [, $colId] = tablaRelacionUnidad($tipo);
    $tabla = $tipo === 'especia' ? 'especia' : 'ingrediente';
    $stmt = $pdo->prepare("SELECT nombre FROM $tabla WHERE $colId = ?");
    $stmt->execute([$id]);
    $nombreAlimento = $stmt->fetchColumn() ?: "#$id";

    return "Para '" . $nombreAlimento . "' no se permite '" . $unidad . "'. Unidades válidas: " . implode(', ', $permitidas);
}

function tablaRelacionUnidad(string $tipo): array
{
    return $tipo === 'especia'
        ? ['especia_unidad', 'id_especia']
        : ['ingrediente_unidad', 'id_ingrediente'];
}
