<?php
/**
 * Cola de revisión de imágenes (solo administradores). Carril 2 de la moderación.
 *
 * GET  ?id_admin=4
 *      -> [{"id_moderacion":1,"imagen":"uploads/...","id_receta":6,"receta":"...","autor":"...",
 *           "adult":"UNLIKELY","violence":"POSSIBLE","racy":"...","medical":"...","spoof":"...",
 *           "motivo":"Revisar: violencia","fecha":"..."}]
 *
 * POST id_admin, id_moderacion, accion:
 *   aprobar   -> la receta se publica y se avisa al autor
 *   rechazar  -> motivo obligatorio; se borra la imagen, la receta queda 'rechazada' y el autor recibe el motivo
 *   resubir   -> se borra la imagen y la receta queda como borrador hasta que el autor suba otra
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';
require_once __DIR__ . '/receta_helper.php';

header('Content-Type: application/json; charset=utf-8');

$esPost = $_SERVER['REQUEST_METHOD'] === 'POST';
$entrada = $esPost ? $_POST : $_GET;
$idAdmin = (int)($entrada['id_admin'] ?? 0);

try {
    $pdo = obtenerConexion();

    if (!esAdministrador($pdo, $idAdmin)) {
        responderJson(403, ["success" => false, "message" => "Solo un administrador puede revisar imágenes"]);
    }

    if (!$esPost) {
        $stmt = $pdo->query("
            SELECT m.id_moderacion, m.ruta, m.id_receta, r.nombre AS receta, u.nombre AS autor,
                   m.adult, m.violence, m.racy, m.medical, m.spoof, m.motivo, m.fecha
            FROM moderacion_imagen m
            JOIN receta r ON r.id_receta = m.id_receta AND r.imagen = m.ruta
            JOIN usuario u ON u.id_usuario = m.id_usuario
            WHERE m.resultado = 'revision' AND r.estado = 'pendiente_revision'
            ORDER BY m.fecha ASC
        ");
        $lista = array_map(static fn($f) => [
            "id_moderacion" => (int)$f['id_moderacion'],
            "imagen" => $f['ruta'],
            "id_receta" => (int)$f['id_receta'],
            "receta" => $f['receta'],
            "autor" => $f['autor'],
            "adult" => $f['adult'],
            "violence" => $f['violence'],
            "racy" => $f['racy'],
            "medical" => $f['medical'],
            "spoof" => $f['spoof'],
            "motivo" => $f['motivo'],
            "fecha" => $f['fecha']
        ], $stmt->fetchAll());

        if (ob_get_level() > 0) ob_end_clean();
        echo json_encode($lista, JSON_UNESCAPED_UNICODE);
        exit;
    }

    $accion = $_POST['accion'] ?? '';
    $idModeracion = (int)($_POST['id_moderacion'] ?? 0);
    $motivo = trim($_POST['motivo'] ?? '');

    $stmt = $pdo->prepare("
        SELECT m.id_moderacion, m.ruta, m.resultado, r.id_receta, r.id_usuario, r.nombre, r.imagen
        FROM moderacion_imagen m
        JOIN receta r ON r.id_receta = m.id_receta
        WHERE m.id_moderacion = ?
    ");
    $stmt->execute([$idModeracion]);
    $item = $stmt->fetch();

    if (!$item || $item['resultado'] !== 'revision' || $item['imagen'] !== $item['ruta']) {
        responderJson(404, ["success" => false, "message" => "Esa imagen ya no está en la cola de revisión"]);
    }

    $idReceta = (int)$item['id_receta'];
    $autor = (int)$item['id_usuario'];
    $nombre = $item['nombre'];

    if ($accion === 'aprobar') {
        $pdo->beginTransaction();
        $pdo->prepare("UPDATE moderacion_imagen SET resultado = 'aprobada', revisado_por = ?, fecha_revision = NOW() WHERE id_moderacion = ?")
            ->execute([$idAdmin, $idModeracion]);
        $pdo->prepare("UPDATE receta SET estado = 'publicada' WHERE id_receta = ?")->execute([$idReceta]);
        notificar($pdo, $autor, 'moderacion', 'Receta publicada',
            "Tu receta \"$nombre\" fue revisada y ya está publicada.", $idReceta);
        $pdo->commit();
        responderJson(200, ["success" => true, "message" => "Imagen aprobada; la receta ya está publicada"]);
    }

    if ($accion === 'rechazar' || $accion === 'resubir') {
        if ($accion === 'rechazar' && $motivo === '') {
            responderJson(400, ["success" => false, "message" => "Escribe el motivo del rechazo"]);
        }
        $motivo = mb_substr($motivo !== '' ? $motivo : 'Se necesita otra imagen', 0, 255);
        $nuevoEstado = $accion === 'rechazar' ? 'rechazada' : 'borrador';

        $pdo->beginTransaction();
        $pdo->prepare("UPDATE moderacion_imagen SET resultado = 'rechazada', motivo = ?, revisado_por = ?, fecha_revision = NOW() WHERE id_moderacion = ?")
            ->execute([$motivo, $idAdmin, $idModeracion]);
        $pdo->prepare("UPDATE receta SET estado = ?, imagen = NULL WHERE id_receta = ?")
            ->execute([$nuevoEstado, $idReceta]);
        $mensaje = $accion === 'rechazar'
            ? "Tu receta \"$nombre\" no se publicó: $motivo. Puedes editarla y subir otra imagen."
            : "Para publicar tu receta \"$nombre\" sube una imagen diferente (edítala). Motivo: $motivo";
        notificar($pdo, $autor, 'moderacion',
            $accion === 'rechazar' ? 'Receta no publicada' : 'Sube otra imagen', $mensaje, $idReceta);
        $pdo->commit();

        borrarImagen($item['ruta']);
        responderJson(200, ["success" => true, "message" => $accion === 'rechazar'
            ? "Imagen rechazada; se avisó al autor"
            : "Se pidió otra imagen al autor"]);
    }

    responderJson(400, ["success" => false, "message" => "Acción no válida"]);
} catch (PDOException $e) {
    if (isset($pdo) && $pdo->inTransaction()) $pdo->rollBack();
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}
