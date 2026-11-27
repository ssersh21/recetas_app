<?php
/**
 * Calificación y reporte de recetas (tablas calificacion y reporte_receta).
 *
 * GET  ?id_usuario=1&id_receta=5
 *      -> {"success":true, "calificacion":4, "reportado":false, "promedio":4.5, "total":2}
 *
 * POST accion=calificar, id_usuario, id_receta, calificacion (1-5)
 *      Guarda o actualiza la calificación del usuario (una por usuario y receta).
 *
 * POST accion=reportar, id_usuario, id_receta, motivo
 *      Guarda el reporte (uno por usuario y receta).
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header("Content-Type: application/json; charset=UTF-8");

$esPost = $_SERVER["REQUEST_METHOD"] === "POST";
$entrada = $esPost ? $_POST : $_GET;

$idUsuario = (int)($entrada["id_usuario"] ?? 0);
$idReceta = (int)($entrada["id_receta"] ?? 0);

if ($idUsuario <= 0 || $idReceta <= 0) {
    responderJson(400, ["success" => false, "message" => "Usuario o receta no válidos"]);
}

function estadoFeedback(PDO $pdo, int $idUsuario, int $idReceta): array
{
    $stmt = $pdo->prepare("SELECT calificacion FROM calificacion WHERE id_usuario = ? AND id_receta = ?");
    $stmt->execute([$idUsuario, $idReceta]);
    $propia = (int)($stmt->fetchColumn() ?: 0);

    $stmt = $pdo->prepare("SELECT 1 FROM reporte_receta WHERE id_usuario = ? AND id_receta = ?");
    $stmt->execute([$idUsuario, $idReceta]);
    $reportado = (bool)$stmt->fetchColumn();

    $stmt = $pdo->prepare("SELECT COALESCE(AVG(calificacion), 0) AS promedio, COUNT(*) AS total FROM calificacion WHERE id_receta = ?");
    $stmt->execute([$idReceta]);
    $resumen = $stmt->fetch();

    return [
        "calificacion" => $propia,
        "reportado" => $reportado,
        "promedio" => round((float)$resumen["promedio"], 1),
        "total" => (int)$resumen["total"]
    ];
}

try {
    $pdo = obtenerConexion();

    $stmt = $pdo->prepare("SELECT 1 FROM receta WHERE id_receta = ?");
    $stmt->execute([$idReceta]);
    if (!$stmt->fetchColumn()) {
        responderJson(404, ["success" => false, "message" => "La receta no existe"]);
    }
    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if (!$stmt->fetchColumn()) {
        responderJson(404, ["success" => false, "message" => "El usuario no existe"]);
    }

    if (!$esPost) {
        responderJson(200, ["success" => true] + estadoFeedback($pdo, $idUsuario, $idReceta));
    }

    $accion = $_POST["accion"] ?? "";

    if ($accion === "calificar") {
        $valor = (int)($_POST["calificacion"] ?? 0);
        if ($valor < 1 || $valor > 5) {
            responderJson(400, ["success" => false, "message" => "La calificación debe ser de 1 a 5"]);
        }
        $stmt = $pdo->prepare("
            INSERT INTO calificacion (id_usuario, id_receta, calificacion)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE calificacion = VALUES(calificacion), fecha = CURRENT_TIMESTAMP
        ");
        $stmt->execute([$idUsuario, $idReceta, $valor]);

        responderJson(200, ["success" => true, "message" => "Calificación guardada"]
            + estadoFeedback($pdo, $idUsuario, $idReceta));
    }

    if ($accion === "reportar") {
        $motivo = trim($_POST["motivo"] ?? "");
        if ($motivo === "") {
            responderJson(400, ["success" => false, "message" => "Indica el motivo del reporte"]);
        }
        $motivo = mb_substr($motivo, 0, 100);

        try {
            $stmt = $pdo->prepare("INSERT INTO reporte_receta (id_usuario, id_receta, motivo) VALUES (?, ?, ?)");
            $stmt->execute([$idUsuario, $idReceta, $motivo]);
        } catch (PDOException $e) {
            if ($e->getCode() === '23000') {
                responderJson(409, ["success" => false, "message" => "Ya habías reportado esta receta"]
                    + estadoFeedback($pdo, $idUsuario, $idReceta));
            }
            throw $e;
        }

        responderJson(200, ["success" => true, "message" => "Gracias, el reporte fue enviado"]
            + estadoFeedback($pdo, $idUsuario, $idReceta));
    }

    responderJson(400, ["success" => false, "message" => "Acción no válida"]);
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
