<?php
/**
 * Migración única: cifra con password_hash() las contraseñas que sigan en texto plano.
 * Ejecutar desde consola:  C:\xampp\php\php.exe sql\03_cifrar_contrasenas.php
 * Es seguro ejecutarlo varias veces (las ya cifradas se omiten).
 * Aunque no se ejecute, login.php cifra cada contraseña vieja la primera vez que ese usuario entra.
 */
require_once __DIR__ . '/../conexion.php';

$pdo = obtenerConexion();
$usuarios = $pdo->query("SELECT id_usuario, contrasena FROM usuario")->fetchAll();
$update = $pdo->prepare("UPDATE usuario SET contrasena = :hash WHERE id_usuario = :id");

$cifradas = 0;
foreach ($usuarios as $u) {
    $info = password_get_info($u['contrasena']);
    if ($info['algo'] !== null && $info['algo'] !== 0) {
        continue; // ya está cifrada
    }
    $update->execute([':hash' => password_hash($u['contrasena'], PASSWORD_DEFAULT), ':id' => $u['id_usuario']]);
    $cifradas++;
}

echo "Contraseñas cifradas: $cifradas de " . count($usuarios) . PHP_EOL;
