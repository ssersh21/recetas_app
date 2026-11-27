<?php
/**
 * Sube la imagen de una receta.
 * POST multipart/form-data: id_usuario, imagen (archivo JPG, PNG o WEBP, máx. 5 MB)
 * Respuesta: {"success": true, "imagen": "uploads/receta_<32 hex>.jpg"}
 *
 * La ruta devuelta se manda después en "imagen" a guardar_receta.php / editar_receta.php.
 * (Aquí es donde se conectará la moderación con Google Cloud Vision SafeSearch.)
 */
ob_start();
error_reporting(0);
ini_set('display_errors', 0);

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/auth_helper.php';

header('Content-Type: application/json; charset=utf-8');

const TAMANO_MAXIMO = 5 * 1024 * 1024;
const TIPOS_PERMITIDOS = [
    'image/jpeg' => 'jpg',
    'image/png'  => 'png',
    'image/webp' => 'webp',
];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responderJson(405, ["success" => false, "message" => "Método no permitido"]);
}

$idUsuario = (int)($_POST['id_usuario'] ?? 0);
$archivo = $_FILES['imagen'] ?? null;

if (!$archivo || ($archivo['error'] ?? UPLOAD_ERR_NO_FILE) !== UPLOAD_ERR_OK) {
    $codigo = $archivo['error'] ?? UPLOAD_ERR_NO_FILE;
    $mensaje = in_array($codigo, [UPLOAD_ERR_INI_SIZE, UPLOAD_ERR_FORM_SIZE], true)
        ? "La imagen pesa demasiado (máximo 5 MB)"
        : "No se recibió ninguna imagen";
    responderJson(400, ["success" => false, "message" => $mensaje]);
}
if ($archivo['size'] > TAMANO_MAXIMO) {
    responderJson(400, ["success" => false, "message" => "La imagen pesa demasiado (máximo 5 MB)"]);
}

// El tipo se revisa por el contenido del archivo, no por el nombre que manda el cliente
$tipo = (new finfo(FILEINFO_MIME_TYPE))->file($archivo['tmp_name']);
if (!isset(TIPOS_PERMITIDOS[$tipo]) || @getimagesize($archivo['tmp_name']) === false) {
    responderJson(400, ["success" => false, "message" => "Solo se permiten imágenes JPG, PNG o WEBP"]);
}

try {
    $pdo = obtenerConexion();
    $stmt = $pdo->prepare("SELECT 1 FROM usuario WHERE id_usuario = ?");
    $stmt->execute([$idUsuario]);
    if ($idUsuario <= 0 || !$stmt->fetchColumn()) {
        responderJson(401, ["success" => false, "message" => "Inicia sesión para subir imágenes"]);
    }
} catch (PDOException $e) {
    responderJson(500, ["success" => false, "message" => "Error de conexión con la base de datos"]);
}

$carpeta = __DIR__ . '/uploads';
if (!is_dir($carpeta) && !mkdir($carpeta, 0755, true)) {
    responderJson(500, ["success" => false, "message" => "No se pudo guardar la imagen"]);
}

$nombre = 'receta_' . bin2hex(random_bytes(16)) . '.' . TIPOS_PERMITIDOS[$tipo];
if (!move_uploaded_file($archivo['tmp_name'], "$carpeta/$nombre")) {
    responderJson(500, ["success" => false, "message" => "No se pudo guardar la imagen"]);
}

responderJson(200, ["success" => true, "message" => "Imagen subida", "imagen" => "uploads/$nombre"]);
