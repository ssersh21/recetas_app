<?php

require_once __DIR__ . '/conexion.php';
require_once __DIR__ . '/unidades_helper.php';

header('Content-Type: application/json; charset=utf-8');


// ============================================================
// CONFIGURACIÓN DE BASE DE DATOS
// ============================================================

try {

    $pdo = obtenerConexion();

} catch (PDOException $e) {

    http_response_code(500);

    echo json_encode([
        "success" => false,
        "mensaje" => "No se pudo conectar con la base de datos"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


// ============================================================
// SOLO ACEPTAR POST
// ============================================================

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {

    http_response_code(405);

    echo json_encode([
        "success" => false,
        "mensaje" => "Método no permitido. Utiliza POST."
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


// ============================================================
// RECIBIR JSON
// ============================================================

$json = file_get_contents("php://input");

$data = json_decode($json, true);


if (!$data || !is_array($data)) {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "El JSON recibido no es válido"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


// ============================================================
// DATOS GENERALES
// ============================================================

$idUsuario = isset($data['id_usuario'])
    ? intval($data['id_usuario'])
    : 0;

$nombre = isset($data['nombre'])
    ? trim($data['nombre'])
    : "";

$porciones = isset($data['porciones'])
    ? intval($data['porciones'])
    : 0;

$tiempo = isset($data['tiempo_estimado'])
    ? intval($data['tiempo_estimado'])
    : 0;

$nivelDificultad = isset($data['nivel_dificultad'])
    ? trim($data['nivel_dificultad'])
    : "principiante";

$descripcion = isset($data['descripcion'])
    ? trim($data['descripcion'])
    : "";

$imagen = isset($data['imagen'])
    ? trim($data['imagen'])
    : null;


// ============================================================
// VALIDACIONES
// ============================================================

if ($idUsuario <= 0) {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "El usuario no es válido"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


if ($nombre === "") {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "El nombre de la receta es obligatorio"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


if ($porciones <= 0) {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "Las porciones deben ser mayores a 0"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


if ($tiempo <= 0) {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "El tiempo debe ser mayor a 0"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


// Validar dificultad
$dificultadesPermitidas = [
    "principiante",
    "intermedio",
    "avanzado"
];

if (!in_array($nivelDificultad, $dificultadesPermitidas)) {

    http_response_code(400);

    echo json_encode([
        "success" => false,
        "mensaje" => "Nivel de dificultad no válido"
    ], JSON_UNESCAPED_UNICODE);

    exit;
}


// ============================================================
// ARRAYS
// ============================================================

$ingredientes = isset($data['ingredientes'])
    && is_array($data['ingredientes'])
    ? $data['ingredientes']
    : [];

$condimentos = isset($data['condimentos'])
    && is_array($data['condimentos'])
    ? $data['condimentos']
    : [];

$pasos = isset($data['pasos'])
    && is_array($data['pasos'])
    ? $data['pasos']
    : [];

$utensilios = isset($data['utensilios'])
    && is_array($data['utensilios'])
    ? $data['utensilios']
    : [];


// ============================================================
// TRANSACCIÓN
// ============================================================

try {

    $pdo->beginTransaction();


    // ========================================================
    // 1. INSERTAR RECETA
    // ========================================================

    $sqlReceta = "
        INSERT INTO receta
        (
            id_usuario,
            nombre,
            porciones,
            tiempo_estimado,
            nivel_dificultad,
            imagen,
            descripcion,
            estado,
            visibilidad
        )
        VALUES
        (
            :id_usuario,
            :nombre,
            :porciones,
            :tiempo_estimado,
            :nivel_dificultad,
            :imagen,
            :descripcion,
            'publicada',
            'publico'
        )
    ";

    $stmtReceta = $pdo->prepare($sqlReceta);

    $stmtReceta->execute([
        ':id_usuario' => $idUsuario,
        ':nombre' => $nombre,
        ':porciones' => $porciones,
        ':tiempo_estimado' => $tiempo,
        ':nivel_dificultad' => $nivelDificultad,
        ':imagen' => $imagen,
        ':descripcion' => $descripcion
    ]);


    // Obtener ID de la receta recién creada
    $idReceta = $pdo->lastInsertId();


    // ========================================================
    // 2. INSERTAR INGREDIENTES
    // ========================================================

    $sqlIngrediente = "
        INSERT INTO receta_ingrediente
        (
            id_receta,
            id_ingrediente,
            cantidad,
            unidad,
            tipo
        )
        VALUES
        (
            :id_receta,
            :id_ingrediente,
            :cantidad,
            :unidad,
            :tipo
        )
    ";

    $stmtIngrediente = $pdo->prepare($sqlIngrediente);


    foreach ($ingredientes as $ingrediente) {

        $idIngrediente = isset($ingrediente['id_ingrediente'])
            ? intval($ingrediente['id_ingrediente'])
            : 0;

        $cantidad = isset($ingrediente['cantidad'])
            ? floatval($ingrediente['cantidad'])
            : 0;

        $unidad = isset($ingrediente['unidad'])
            ? trim($ingrediente['unidad'])
            : "";

        $tipo = isset($ingrediente['tipo'])
            ? trim($ingrediente['tipo'])
            : "principal";


        if ($idIngrediente <= 0) {
            throw new Exception(
                "Se recibió un ingrediente inválido"
            );
        }

        if ($cantidad <= 0) {
            throw new Exception(
                "La cantidad de un ingrediente debe ser mayor a 0"
            );
        }

        if ($unidad === "") {
            throw new Exception(
                "La unidad de un ingrediente es obligatoria"
            );
        }

        // La unidad debe ser compatible con el ingrediente (tabla ingrediente_unidad)
        $errorUnidad = validarUnidad($pdo, "ingrediente", $idIngrediente, $unidad, "receta");
        if ($errorUnidad !== null) {
            throw new Exception($errorUnidad);
        }


        // Normalizar tipo
        if (
            $tipo !== "principal" &&
            $tipo !== "complementario"
        ) {
            $tipo = "principal";
        }


        $stmtIngrediente->execute([
            ':id_receta' => $idReceta,
            ':id_ingrediente' => $idIngrediente,
            ':cantidad' => $cantidad,
            ':unidad' => $unidad,
            ':tipo' => $tipo
        ]);
    }


    // ========================================================
    // 3. INSERTAR CONDIMENTOS / ESPECIAS
    // ========================================================

    $sqlCondimento = "
        INSERT INTO receta_especia
        (
            id_receta,
            id_especia,
            cantidad,
            unidad
        )
        VALUES
        (
            :id_receta,
            :id_especia,
            :cantidad,
            :unidad
        )
    ";

    $stmtCondimento = $pdo->prepare($sqlCondimento);


    foreach ($condimentos as $condimento) {

        $idEspecia = isset($condimento['id_especia'])
            ? intval($condimento['id_especia'])
            : 0;

        // También permitimos id_condimento
        // por compatibilidad con la app Android
        if ($idEspecia <= 0 && isset($condimento['id_condimento'])) {
            $idEspecia = intval($condimento['id_condimento']);
        }


        $cantidad = isset($condimento['cantidad'])
            ? floatval($condimento['cantidad'])
            : 0;

        $unidad = isset($condimento['unidad'])
            ? trim($condimento['unidad'])
            : "";


        if ($idEspecia <= 0) {
            throw new Exception(
                "Se recibió un condimento inválido"
            );
        }

        if ($cantidad <= 0) {
            throw new Exception(
                "La cantidad de un condimento debe ser mayor a 0"
            );
        }

        if ($unidad === "") {
            throw new Exception(
                "La unidad de un condimento es obligatoria"
            );
        }

        // La unidad debe ser compatible con la especia (tabla especia_unidad)
        $errorUnidad = validarUnidad($pdo, "especia", $idEspecia, $unidad, "receta");
        if ($errorUnidad !== null) {
            throw new Exception($errorUnidad);
        }


        $stmtCondimento->execute([
            ':id_receta' => $idReceta,
            ':id_especia' => $idEspecia,
            ':cantidad' => $cantidad,
            ':unidad' => $unidad
        ]);
    }


    // ========================================================
    // 4. INSERTAR PASOS
    // ========================================================

    $sqlPaso = "
        INSERT INTO paso
        (
            id_receta,
            numero_paso,
            descripcion
        )
        VALUES
        (
            :id_receta,
            :numero_paso,
            :descripcion
        )
    ";

    $stmtPaso = $pdo->prepare($sqlPaso);


    $numeroPaso = 1;


    foreach ($pasos as $paso) {

        // Permitir recibir simplemente:
        // "Cortar las verduras"

        if (is_string($paso)) {

            $descripcionPaso = trim($paso);

        } else {

            $descripcionPaso = isset($paso['descripcion'])
                ? trim($paso['descripcion'])
                : "";
        }


        if ($descripcionPaso === "") {
            continue;
        }


        $stmtPaso->execute([
            ':id_receta' => $idReceta,
            ':numero_paso' => $numeroPaso,
            ':descripcion' => $descripcionPaso
        ]);


        $numeroPaso++;
    }


    // ========================================================
    // 5. INSERTAR UTENSILIOS
    // ========================================================

    $sqlUtensilio = "
        INSERT INTO receta_utensilio
        (
            id_receta,
            id_utensilio
        )
        VALUES
        (
            :id_receta,
            :id_utensilio
        )
    ";

    $stmtUtensilio = $pdo->prepare($sqlUtensilio);


    foreach ($utensilios as $utensilio) {

        // Puede llegar:
        // 5
        //
        // o:
        // {"id_utensilio":5}

        if (is_array($utensilio)) {

            $idUtensilio = isset($utensilio['id_utensilio'])
                ? intval($utensilio['id_utensilio'])
                : 0;

        } else {

            $idUtensilio = intval($utensilio);
        }


        if ($idUtensilio <= 0) {
            continue;
        }


        $stmtUtensilio->execute([
            ':id_receta' => $idReceta,
            ':id_utensilio' => $idUtensilio
        ]);
    }


    // ========================================================
    // CONFIRMAR TRANSACCIÓN
    // ========================================================

    $pdo->commit();


    // ========================================================
    // RESPUESTA
    // ========================================================

    echo json_encode([
        "success" => true,
        "mensaje" => "Receta guardada correctamente",
        "id_receta" => intval($idReceta)
    ], JSON_UNESCAPED_UNICODE);


} catch (Exception $e) {

    // Si algo falla, deshacer TODO
    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }


    http_response_code(500);


    echo json_encode([
        "success" => false,
        "mensaje" => $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}

?>
