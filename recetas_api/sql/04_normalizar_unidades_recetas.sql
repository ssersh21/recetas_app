-- ============================================================
-- Normaliza las unidades de las recetas existentes al catálogo
-- unidad_medida (las recetas de ejemplo usaban abreviaturas como
-- 'g', 'pieza', 'taza', 'dientes', 'latas', 'hoja').
-- Ejecutar ANTES de 05_triggers_unidades.sql. Se puede ejecutar varias veces.
-- ============================================================

SET NAMES utf8mb4;

-- Medidas que faltaban y que sí tienen sentido para estos alimentos
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT i.id_ingrediente, u.id_unidad, 'receta', 4
  FROM ingrediente i JOIN unidad_medida u ON u.nombre = 'Tazas'
  WHERE i.nombre = 'Granos de elote';

INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT e.id_especia, u.id_unidad, 'receta', 5
  FROM especia e JOIN unidad_medida u ON u.nombre = 'Piezas'
  WHERE e.nombre = 'Laurel';

-- Abreviaturas -> nombre oficial
UPDATE receta_ingrediente SET unidad = 'Gramos (gr)'     WHERE unidad IN ('g', 'gr', 'gramos');
UPDATE receta_ingrediente SET unidad = 'Kilogramos (kg)' WHERE unidad IN ('kg', 'kilogramos');
UPDATE receta_ingrediente SET unidad = 'Mililitros (ml)' WHERE unidad IN ('ml', 'mililitros');
UPDATE receta_ingrediente SET unidad = 'Litros (L)'      WHERE unidad IN ('l', 'litros');
UPDATE receta_ingrediente SET unidad = 'Tazas'           WHERE unidad IN ('taza', 'tazas');
UPDATE receta_ingrediente SET unidad = 'Cucharadas'      WHERE unidad IN ('cucharada', 'cucharadas');
UPDATE receta_ingrediente SET unidad = 'Cucharaditas'    WHERE unidad IN ('cucharadita', 'cucharaditas');
-- Dientes de ajo, filetes, latas: se cuentan como piezas
UPDATE receta_ingrediente SET unidad = 'Piezas'
  WHERE unidad IN ('pieza', 'piezas', 'diente', 'dientes', 'filete', 'filetes', 'lata', 'latas');

UPDATE receta_especia SET unidad = 'Gramos (gr)'  WHERE unidad IN ('g', 'gr', 'gramos');
UPDATE receta_especia SET unidad = 'Cucharadas'   WHERE unidad IN ('cucharada', 'cucharadas');
UPDATE receta_especia SET unidad = 'Cucharaditas' WHERE unidad IN ('cucharadita', 'cucharaditas');
UPDATE receta_especia SET unidad = 'Al gusto'     WHERE unidad IN ('al gusto');
-- Hojas de laurel
UPDATE receta_especia SET unidad = 'Piezas'       WHERE unidad IN ('pieza', 'piezas', 'hoja', 'hojas');
