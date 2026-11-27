<?php
/**
 * Copia este archivo como config.local.php y pon tus valores.
 * config.local.php NO se sube a Git (tiene claves).
 */

// Clave de Google Cloud Vision (API "Cloud Vision API" habilitada en tu proyecto).
// Si se deja vacía, todas las imágenes se mandan a revisión del administrador.
const VISION_API_KEY = '';

// Normalmente no se cambia.
const VISION_ENDPOINT = 'https://vision.googleapis.com/v1/images:annotate';
