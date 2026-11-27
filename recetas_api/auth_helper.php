<?php

/**
 * Funciones compartidas de autenticación.
 * Las contraseñas se guardan con password_hash() (bcrypt), nunca en texto plano.
 */

function responderJson(int $codigo, array $datos): void
{
    if (ob_get_level() > 0) {
        ob_end_clean();
    }
    http_response_code($codigo);
    echo json_encode($datos, JSON_UNESCAPED_UNICODE);
    exit;
}

/**
 * Verifica la contraseña de un usuario.
 * Si la contraseña guardada todavía está en texto plano (cuentas viejas),
 * la acepta una vez y la reemplaza por su versión cifrada.
 */
function verificarContrasena(PDO $pdo, array $usuario, string $contrasena): bool
{
    $guardada = (string)$usuario['contrasena'];
    $info = password_get_info($guardada);

    if ($info['algo'] !== null && $info['algo'] !== 0) {
        if (!password_verify($contrasena, $guardada)) {
            return false;
        }
        if (password_needs_rehash($guardada, PASSWORD_DEFAULT)) {
            actualizarHash($pdo, (int)$usuario['id_usuario'], $contrasena);
        }
        return true;
    }

    // Cuenta antigua con contraseña en texto plano
    if (hash_equals($guardada, $contrasena)) {
        actualizarHash($pdo, (int)$usuario['id_usuario'], $contrasena);
        return true;
    }
    return false;
}

function actualizarHash(PDO $pdo, int $idUsuario, string $contrasena): void
{
    $stmt = $pdo->prepare("UPDATE usuario SET contrasena = :hash WHERE id_usuario = :id");
    $stmt->execute([':hash' => password_hash($contrasena, PASSWORD_DEFAULT), ':id' => $idUsuario]);
}

/** Formato de usuario que espera la app (AuthResponse.Usuario). */
function usuarioParaApp(array $fila): array
{
    return [
        "id_usuario" => (int)$fila["id_usuario"],
        "nombre" => $fila["nombre"],
        "nombre_usuario" => $fila["nombre"],
        "correo" => $fila["correo"],
        // La app reconoce al administrador con el valor "admin"
        "rol" => ($fila["rol"] ?? "usuario") === "administrador" ? "admin" : "usuario"
    ];
}
