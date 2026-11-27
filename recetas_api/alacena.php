<?php
/**
 * Alacena del usuario (tabla alacena). Las unidades se validan contra
 * ingrediente_unidad (contexto 'alacena'); además la BD tiene un trigger que
 * rechaza cualquier unidad no permitida aunque se inserte por otro medio.
 *
 * GET  ?id_usuario=1
 *      -> [{"id_alacena":1,"id_ingrediente":92,"nombre":"Jitomate","cantidad":2,"unidad":"Piezas","unidades":[...]}]
 *
 * POST accion=agregar     id_usuario, id_ingrediente, cantidad, unidad
 *      Si el ingrediente ya está en la alacena se suma (convirtiendo gr<->kg y ml<->L).
 * POST accion=actualizar  id_usuario, id_alacena, id_ingrediente, cantidad, unidad
 * POST accion=eliminar    id_usuario, id_alacena
 *
 * Las respuestas POST son {"success":true|false,"message":"..."}.
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';
require_once __DIR__ . '/unidades_helper.php';

header("Content-Type: application/json; charset=UTF-8");

const CANTIDAD_MAXIMA = 99999;

/** Factor a la unidad base (gramos o mililitros); null si no es convertible. */
function factorBase(string $unidad): ?array
{
    $tabla = [
        'Gramos (gr)'     => ['masa', 1],
        'Kilogramos (kg)' => ['masa', 1000],
        'Mililitros (ml)' => ['volumen', 1],
        'Litros (L)'      => ['volumen', 1000],
    ];
    return $tabla[$unidad] ?? null;
}

/** Convierte $cantidad de $desde a $hacia; null si no se puede. */
function convertir(float $cantidad, string $desde, string $hacia): ?float
{
    if ($desde === $hacia) {
        return $cantidad;
    }
    $a = factorBase($desde);
    $b = factorBase($hacia);
    if ($a === null || $b === null || $a[0] !== $b[0]) {
        return null;
    }
    return $cantidad * $a[1] / $b[1];
}

function nombreIngrediente(PDO $pdo, int $id): ?string
{
    $stmt = $pdo->prepare("SELECT nombre FROM ingrediente WHERE id_ingrediente = ?");
    $stmt->execute([$id]);
    $nombre = $stmt->fetchColumn();
    return $nombre === false ? null : $nombre;
}

/** Valida ingrediente, cantidad y unidad. Termina la petición si algo está mal. */
function validarRenglon(PDO $pdo, int $idIngrediente, float $cantidad, string $unidad): void
{
    if ($idIngrediente <= 0 || nombreIngrediente($pdo, $idIngrediente) === null) {
        responderJson(400, ["success" => false, "message" => "Selecciona un ingrediente de la lista"]);
    }
    if ($cantidad <= 0 || $cantidad > CANTIDAD_MAXIMA) {
        responderJson(400, ["success" => false, "message" => "La cantidad debe ser mayor a 0"]);
    }
    if ($unidad === "") {
        responderJson(400, ["success" => false, "message" => "Selecciona la unidad"]);
    }
    $error = validarUnidad($pdo, 'ingrediente', $idIngrediente, $unidad, 'alacena');
    if ($error !== null) {
        responderJson(400, ["success" => false, "message" => $error]);
    }
}

$esPost = $_SERVER["REQUEST_METHOD"] === "POST";
$entrada = $esPost ? $_POST : $_GET;
$idUsuario = (int)($entrada["id_usuario"] ?? 0);

if ($idUsuario <= 0) {
    responderJson(400, ["success" => false, "message" => "Usuario no válido"]);
}

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if (!$stmt->fetchColumn()) {
        responderJson(404, ["success" => false, "message" => "El usuario no existe"]);
    }

    // ---------------------------------------------------------------- GET
    if (!$esPost) {
        $stmt = $pdo->prepare("
            SELECT a.id_alacena, a.id_ingrediente, i.nombre, a.cantidad, a.unidad
            FROM alacena a
            JOIN ingrediente i ON i.id_ingrediente = a.id_ingrediente
            WHERE a.id_usuario = ?
            ORDER BY i.nombre
        ");
        $stmt->execute([$idUsuario]);
        $filas = $stmt->fetchAll();

        $unidades = obtenerUnidadesPorAlimento($pdo, 'ingrediente', array_column($filas, 'id_ingrediente'), 'alacena');

        $resultado = array_map(static function ($f) use ($unidades) {
            $id = (int)$f['id_ingrediente'];
            return [
                "id_alacena" => (int)$f['id_alacena'],
                "id_ingrediente" => $id,
                "nombre" => $f['nombre'],
                "cantidad" => (float)$f['cantidad'],
                "unidad" => $f['unidad'],
                "unidades" => $unidades[$id] ?? []
            ];
        }, $filas);

        if (ob_get_level() > 0) {
            ob_end_clean();
        }
        echo json_encode($resultado, JSON_UNESCAPED_UNICODE);
        exit;
    }

    // ---------------------------------------------------------------- POST
    $accion = $_POST["accion"] ?? "";
    $idIngrediente = (int)($_POST["id_ingrediente"] ?? 0);
    $cantidad = round((float)str_replace(',', '.', $_POST["cantidad"] ?? "0"), 2);
    $unidad = trim($_POST["unidad"] ?? "");
    $idAlacena = (int)($_POST["id_alacena"] ?? 0);

    if ($accion === "agregar") {
        validarRenglon($pdo, $idIngrediente, $cantidad, $unidad);

        $stmt = $pdo->prepare("SELECT id_alacena, cantidad, unidad FROM alacena WHERE id_usuario = ? AND id_ingrediente = ?");
        $stmt->execute([$idUsuario, $idIngrediente]);
        $existente = $stmt->fetch();

        if (!$existente) {
            $stmt = $pdo->prepare("INSERT INTO alacena (id_usuario, id_ingrediente, cantidad, unidad) VALUES (?, ?, ?, ?)");
            $stmt->execute([$idUsuario, $idIngrediente, $cantidad, $unidad]);
            responderJson(201, ["success" => true, "message" => "Ingrediente agregado a tu alacena"]);
        }

        // Ya existe: se suma en la unidad que ya tenía
        $convertida = convertir($cantidad, $unidad, $existente['unidad']);
        if ($convertida === null) {
            responderJson(409, [
                "success" => false,
                "message" => "Ya tienes " . nombreIngrediente($pdo, $idIngrediente) . " registrado en "
                    . $existente['unidad'] . ". Edítalo en lugar de agregarlo de nuevo."
            ]);
        }
        $total = round((float)$existente['cantidad'] + $convertida, 2);
        if ($total > CANTIDAD_MAXIMA) {
            responderJson(400, ["success" => false, "message" => "La cantidad total es demasiado grande"]);
        }
        $stmt = $pdo->prepare("UPDATE alacena SET cantidad = ? WHERE id_alacena = ?");
        $stmt->execute([$total, $existente['id_alacena']]);
        responderJson(200, ["success" => true, "message" => "Se sumó a lo que ya tenías en la alacena"]);
    }

    if ($accion === "actualizar") {
        validarRenglon($pdo, $idIngrediente, $cantidad, $unidad);

        $stmt = $pdo->prepare("SELECT 1 FROM alacena WHERE id_alacena = ? AND id_usuario = ?");
        $stmt->execute([$idAlacena, $idUsuario]);
        if (!$stmt->fetchColumn()) {
            responderJson(404, ["success" => false, "message" => "Ese ingrediente ya no está en tu alacena"]);
        }

        try {
            $stmt = $pdo->prepare("UPDATE alacena SET id_ingrediente = ?, cantidad = ?, unidad = ? WHERE id_alacena = ? AND id_usuario = ?");
            $stmt->execute([$idIngrediente, $cantidad, $unidad, $idAlacena, $idUsuario]);
        } catch (PDOException $e) {
            if ($e->getCode() === '23000') {
                responderJson(409, ["success" => false, "message" => "Ese ingrediente ya está en tu alacena"]);
            }
            throw $e;
        }
        responderJson(200, ["success" => true, "message" => "Alacena actualizada"]);
    }

    if ($accion === "eliminar") {
        $stmt = $pdo->prepare("DELETE FROM alacena WHERE id_alacena = ? AND id_usuario = ?");
        $stmt->execute([$idAlacena, $idUsuario]);
        responderJson(200, ["success" => true, "message" => "Ingrediente eliminado"]);
    }

    responderJson(400, ["success" => false, "message" => "Acción no válida"]);
} catch (PDOException $e) {
    // 45000 = el trigger de la BD rechazó la unidad
    if ($e->getCode() === '45000') {
        responderJson(400, ["success" => false, "message" => "Unidad no permitida para este ingrediente"]);
    }
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
