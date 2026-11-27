<?php
/**
 * Plan de comidas del usuario (tabla plan_comida).
 *
 * GET  ?id_usuario=1&fecha=2026-11-27
 *      -> [{"id_plan":1,"fecha":"2026-11-27","tiempo":"Comida","hora":"14:00","id_receta":6,
 *           "receta":"Pollo...","calorias_porcion":350.5,"cocinada":false}]
 * GET  ?id_usuario=1&historial=1      -> recetas marcadas como cocinadas (más recientes primero)
 *
 * POST accion=asignar   id_usuario, fecha, tiempo, id_receta, hora (opcional HH:MM)
 *      (si ya había una receta en ese tiempo de comida, se reemplaza)
 * POST accion=quitar    id_usuario, fecha, tiempo
 * POST accion=cocinada  id_usuario, fecha, tiempo, cocinada (1|0)
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header('Content-Type: application/json; charset=utf-8');

const TIEMPOS = ['Desayuno', 'Colación matutina', 'Comida', 'Colación vespertina', 'Cena'];

function fechaValida(string $f): bool
{
    $d = DateTime::createFromFormat('Y-m-d', $f);
    return $d !== false && $d->format('Y-m-d') === $f;
}

function filaPlan(array $f): array
{
    return [
        "id_plan" => (int)$f['id_plan'],
        "fecha" => $f['fecha'],
        "tiempo" => $f['tiempo'],
        "hora" => $f['hora'] ? substr($f['hora'], 0, 5) : null,
        "id_receta" => (int)$f['id_receta'],
        "receta" => $f['receta'],
        "calorias_porcion" => $f['calorias_porcion'] === null ? 0.0 : (float)$f['calorias_porcion'],
        "cocinada" => (bool)$f['cocinada']
    ];
}

$esPost = $_SERVER['REQUEST_METHOD'] === 'POST';
$entrada = $esPost ? $_POST : $_GET;
$idUsuario = (int)($entrada['id_usuario'] ?? 0);

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if ($idUsuario <= 0 || !$stmt->fetchColumn()) {
        responderJson(401, ["success" => false, "message" => "Inicia sesión para usar tu plan"]);
    }

    $select = "
        SELECT p.id_plan, p.fecha, p.tiempo, p.hora, p.id_receta, r.nombre AS receta,
               r.calorias_porcion, p.cocinada
        FROM plan_comida p
        JOIN receta r ON r.id_receta = p.id_receta
    ";

    if (!$esPost) {
        if (!empty($_GET['historial'])) {
            $stmt = $pdo->prepare("$select WHERE p.id_usuario = ? AND p.cocinada = 1 ORDER BY p.fecha DESC LIMIT 100");
            $stmt->execute([$idUsuario]);
        } else {
            $fecha = $_GET['fecha'] ?? '';
            if (!fechaValida($fecha)) {
                responderJson(400, ["success" => false, "message" => "Fecha no válida"]);
            }
            $stmt = $pdo->prepare("$select WHERE p.id_usuario = ? AND p.fecha = ?");
            $stmt->execute([$idUsuario, $fecha]);
        }
        if (ob_get_level() > 0) ob_end_clean();
        echo json_encode(array_map('filaPlan', $stmt->fetchAll()), JSON_UNESCAPED_UNICODE);
        exit;
    }

    $accion = $_POST['accion'] ?? '';
    $fecha = $_POST['fecha'] ?? '';
    $tiempo = trim($_POST['tiempo'] ?? '');

    if (!fechaValida($fecha)) {
        responderJson(400, ["success" => false, "message" => "Fecha no válida"]);
    }
    if (!in_array($tiempo, TIEMPOS, true)) {
        responderJson(400, ["success" => false, "message" => "Tiempo de comida no válido"]);
    }

    if ($accion === 'asignar') {
        $idReceta = (int)($_POST['id_receta'] ?? 0);
        $hora = trim($_POST['hora'] ?? '');
        if ($hora !== '' && !preg_match('/^([01]\d|2[0-3]):[0-5]\d$/', $hora)) {
            responderJson(400, ["success" => false, "message" => "Hora no válida (usa HH:MM)"]);
        }
        $stmt = $pdo->prepare("SELECT 1 FROM receta WHERE id_receta = ?");
        $stmt->execute([$idReceta]);
        if (!$stmt->fetchColumn()) {
            responderJson(404, ["success" => false, "message" => "La receta no existe"]);
        }

        $stmt = $pdo->prepare("
            INSERT INTO plan_comida (id_usuario, fecha, tiempo, hora, id_receta, cocinada)
            VALUES (?, ?, ?, ?, ?, 0)
            ON DUPLICATE KEY UPDATE id_receta = VALUES(id_receta), hora = VALUES(hora), cocinada = 0
        ");
        $stmt->execute([$idUsuario, $fecha, $tiempo, $hora === '' ? null : $hora, $idReceta]);
        responderJson(200, ["success" => true, "message" => "Receta agregada al plan"]);
    }

    if ($accion === 'quitar') {
        $stmt = $pdo->prepare("DELETE FROM plan_comida WHERE id_usuario = ? AND fecha = ? AND tiempo = ?");
        $stmt->execute([$idUsuario, $fecha, $tiempo]);
        responderJson(200, ["success" => true, "message" => "Receta quitada del plan"]);
    }

    if ($accion === 'cocinada') {
        $valor = !empty($_POST['cocinada']) && $_POST['cocinada'] !== '0' ? 1 : 0;
        $stmt = $pdo->prepare("UPDATE plan_comida SET cocinada = ? WHERE id_usuario = ? AND fecha = ? AND tiempo = ?");
        $stmt->execute([$valor, $idUsuario, $fecha, $tiempo]);
        responderJson(200, ["success" => true, "message" => $valor ? "Marcada como cocinada" : "Marcada como pendiente"]);
    }

    responderJson(400, ["success" => false, "message" => "Acción no válida"]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
