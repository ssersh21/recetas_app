<?php
// Habilitar reporte de errores para que PHP no responda en blanco si hay fallo de sintaxis o SQL
ini_set('display_errors', 0);
error_reporting(E_ALL);

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, GET, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");
header("Content-Type: application/json; charset=UTF-8");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once __DIR__ . '/conexion.php';

try {
    $pdo = obtenerConexion();
} catch (PDOException $e) {
    ob_end_clean();
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Error de conexión con la base de datos"
    ]);
    exit;
}

$rawInput = file_get_contents("php://input");
$input = json_decode($rawInput, true);

if (!$input || empty($input)) {
    $input = $_POST;
}

if (!$input || !isset($input['id_usuario'])) {
    echo json_encode([
        "success" => false,
        "message" => "No se recibieron datos o falta id_usuario. Entrada recibida: " . substr($rawInput, 0, 100)
    ]);
    exit();
}

$id_usuario = intval($input['id_usuario']);
if ($id_usuario <= 0) {
    echo json_encode(["success" => false, "message" => "id_usuario inválido"]);
    exit();
}

// 3. CAMPOS
$nombre            = trim($input['nombre'] ?? '');
$edad              = intval($input['edad'] ?? 0);
$peso              = floatval($input['peso'] ?? 0);
$altura            = floatval($input['altura'] ?? 0);
$sexo              = trim($input['sexo'] ?? 'Hombre');
$nivel_actividad   = trim($input['nivel_actividad'] ?? '');
$indice_actividad  = intval($input['indice_actividad'] ?? 0);
$objetivo          = trim($input['objetivo'] ?? '');
$indice_meta       = intval($input['indice_meta'] ?? 0);
$nivel_cocina      = trim($input['nivel_cocina'] ?? '');
$tiempo_disponible = intval($input['tiempo_disponible'] ?? 0);
$tiempos_comida    = intval($input['tiempos_comida'] ?? 3);
$tmb               = floatval($input['tmb'] ?? 0);
$tdee              = floatval($input['tdee'] ?? 0);
$calorias_diarias  = floatval($input['calorias_diarias'] ?? 0);

$alergias_raw  = $input['alergias'] ?? [];
$aversiones_raw = $input['aversiones'] ?? [];
$alergias   = is_array($alergias_raw) ? json_encode($alergias_raw, JSON_UNESCAPED_UNICODE) : strval($alergias_raw);
$aversiones = is_array($aversiones_raw) ? json_encode($aversiones_raw, JSON_UNESCAPED_UNICODE) : strval($aversiones_raw);

try {
    // 4. ASEGURAR QUE LA TABLA EXISTE
    $db->exec("CREATE TABLE IF NOT EXISTS perfil_usuario (
        id_perfil INT AUTO_INCREMENT PRIMARY KEY,
        id_usuario INT NOT NULL UNIQUE,
        nombre VARCHAR(120) NULL,
        edad INT DEFAULT 0,
        peso DECIMAL(6,2) DEFAULT 0,
        altura DECIMAL(6,2) DEFAULT 0,
        sexo VARCHAR(20) DEFAULT 'Hombre',
        nivel_actividad VARCHAR(100) NULL,
        indice_actividad INT DEFAULT 0,
        objetivo VARCHAR(100) NULL,
        indice_meta INT DEFAULT 0,
        nivel_cocina VARCHAR(50) NULL,
        tiempo_disponible INT DEFAULT 0,
        tiempos_comida INT DEFAULT 3,
        tmb DECIMAL(8,2) DEFAULT 0,
        tdee DECIMAL(8,2) DEFAULT 0,
        calorias_diarias DECIMAL(8,2) DEFAULT 0,
        alergias TEXT NULL,
        aversiones TEXT NULL,
        fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

    // 5. INSERT / UPDATE
    $sql = "INSERT INTO perfil_usuario 
        (id_usuario, nombre, edad, peso, altura, sexo, nivel_actividad, indice_actividad, objetivo, indice_meta, nivel_cocina, tiempo_disponible, tiempos_comida, tmb, tdee, calorias_diarias, alergias, aversiones)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE 
            nombre = VALUES(nombre),
            edad = VALUES(edad),
            peso = VALUES(peso),
            altura = VALUES(altura),
            sexo = VALUES(sexo),
            nivel_actividad = VALUES(nivel_actividad),
            indice_actividad = VALUES(indice_actividad),
            objetivo = VALUES(objetivo),
            indice_meta = VALUES(indice_meta),
            nivel_cocina = VALUES(nivel_cocina),
            tiempo_disponible = VALUES(tiempo_disponible),
            tiempos_comida = VALUES(tiempos_comida),
            tmb = VALUES(tmb),
            tdee = VALUES(tdee),
            calorias_diarias = VALUES(calorias_diarias),
            alergias = VALUES(alergias),
            aversiones = VALUES(aversiones)";

    $stmt = $db->prepare($sql);
    $stmt->execute([
        $id_usuario, $nombre, $edad, $peso, $altura, $sexo, $nivel_actividad, $indice_actividad,
        $objetivo, $indice_meta, $nivel_cocina, $tiempo_disponible, $tiempos_comida, $tmb, $tdee,
        $calorias_diarias, $alergias, $aversiones
    ]);

    echo json_encode([
        "success" => true,
        "message" => "Perfil guardado con éxito en la base de datos"
    ]);

} catch (PDOException $e) {
    echo json_encode([
        "success" => false,
        "message" => "Error SQL: " . $e->getMessage()
    ]);
}