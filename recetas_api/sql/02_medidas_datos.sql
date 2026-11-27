-- ============================================================
-- Medidas compatibles de cada alimento (datos iniciales).
-- Generado a partir de las reglas que tenía FoodUnitHelper.java.
-- Para cambiar un alimento: INSERT / DELETE en ingrediente_unidad o especia_unidad.
-- Se puede ejecutar varias veces (INSERT IGNORE).
-- ============================================================

SET NAMES utf8mb4;

-- ---------------- ingrediente ----------------
-- Aceitunas (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Aceitunas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Aceitunas';
-- Acelga (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Acelga';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Acelga';
-- Aguacate (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Aguacate';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Aguacate';
-- Ajo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ajo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ajo';
-- Ajonjolí (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ajonjolí';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ajonjolí';
-- Alcachofa (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Alcachofa';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Alcachofa';
-- Alcaparras (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Alcaparras';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Alcaparras';
-- Almendras (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Almendras';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Almendras';
-- Apio (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Apio';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Apio';
-- Arándanos (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Arándanos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Arándanos';
-- Arroz blanco (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Arroz blanco';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Arroz blanco';
-- Arroz integral (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Arroz integral';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Arroz integral';
-- Arroz para sushi (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Arroz para sushi';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Arroz para sushi';
-- Atún en lata (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Atún en lata';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Atún en lata';
-- Avellanas (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Avellanas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Avellanas';
-- Avena (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Avena';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Avena';
-- Azúcar (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Azúcar';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Azúcar';
-- Azúcar glass (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Azúcar glass';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Azúcar glass';
-- Baguette (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Baguette';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Baguette';
-- Berenjena (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Berenjena';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Berenjena';
-- Betabel (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Betabel';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Betabel';
-- Bicarbonato de sodio (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Bicarbonato de sodio';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Bicarbonato de sodio';
-- Bistec de res (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Bistec de res';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Bistec de res';
-- Bolillo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Bolillo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Bolillo';
-- Brócoli (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Brócoli';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Brócoli';
-- Cacahuates (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cacahuates';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Cacahuates';
-- Calabacita (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calabacita';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calabacita';
-- Calabaza (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calabaza';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calabaza';
-- Calamar (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calamar';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Calamar';
-- Camarón (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Camarón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Camarón';
-- Camote (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Camote';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Camote';
-- Carne de cerdo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Carne de cerdo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Carne de cerdo';
-- Carne de res (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Carne de res';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Carne de res';
-- Carne molida de cerdo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Carne molida de cerdo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Carne molida de cerdo';
-- Carne molida de res (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Carne molida de res';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Carne molida de res';
-- Carne para guisar (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Carne para guisar';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Carne para guisar';
-- Cebolla (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cebolla';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cebolla';
-- Cebolla morada (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cebolla morada';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cebolla morada';
-- Cereal de maíz (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cereal de maíz';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cereal de maíz';
-- Champiñones (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Champiñones';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Champiñones';
-- Chayote (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chayote';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chayote';
-- Chía (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chía';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chía';
-- Chícharos secos (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chícharos secos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chícharos secos';
-- Chile poblano (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile poblano';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile poblano';
-- Chocolate en polvo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chocolate en polvo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Chocolate en polvo';
-- Chocolate para repostería (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chocolate para repostería';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Chocolate para repostería';
-- Chorizo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chorizo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chorizo';
-- Chuleta de cerdo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chuleta de cerdo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Chuleta de cerdo';
-- Ciruela (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ciruela';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ciruela';
-- Clara de huevo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Clara de huevo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Clara de huevo';
-- Coco (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Coco';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Coco';
-- Cocoa en polvo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cocoa en polvo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cocoa en polvo';
-- Col morada (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Col morada';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Col morada';
-- Coliflor (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Coliflor';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Coliflor';
-- Costilla de cerdo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Costilla de cerdo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Costilla de cerdo';
-- Costilla de res (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Costilla de res';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Costilla de res';
-- Crema de leche (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Crema de leche';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Crema de leche';
-- Cuscús (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cuscús';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cuscús';
-- Durazno (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Durazno';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Durazno';
-- Ejotes (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ejotes';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ejotes';
-- Elote (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Elote';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Elote';
-- Espárragos (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Espárragos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Espárragos';
-- Espinaca (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Espinaca';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Espinaca';
-- Fideos (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Fideos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Fideos';
-- Filete de pescado (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Filete de pescado';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Filete de pescado';
-- Frambuesa (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Frambuesa';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Frambuesa';
-- Fresa (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Fresa';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Fresa';
-- Frijol bayo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Frijol bayo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Frijol bayo';
-- Frijol negro (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Frijol negro';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Frijol negro';
-- Frijol pinto (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Frijol pinto';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Frijol pinto';
-- Garbanzos (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Garbanzos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Garbanzos';
-- Gelatina sin sabor (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Gelatina sin sabor';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Gelatina sin sabor';
-- Germinados de soya (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Germinados de soya';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Germinados de soya';
-- Granos de elote (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Granos de elote';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Granos de elote';
-- Grenetina (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Grenetina';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Grenetina';
-- Guayaba (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Guayaba';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Guayaba';
-- Habas (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Habas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Habas';
-- Harina de avena (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Harina de avena';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Harina de avena';
-- Harina de maíz (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Harina de maíz';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Harina de maíz';
-- Harina de trigo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Harina de trigo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Harina de trigo';
-- Harina integral (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Harina integral';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Harina integral';
-- Huevo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Huevo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Huevo';
-- Jamón (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jamón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Jamón';
-- Jícama (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jícama';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jícama';
-- Jitomate (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jitomate';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jitomate';
-- Kiwi (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Kiwi';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Kiwi';
-- Lasaña (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lasaña';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lasaña';
-- Leche condensada (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Leche condensada';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Leche condensada';
-- Leche descremada (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Leche descremada';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Leche descremada';
-- Leche entera (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Leche entera';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Leche entera';
-- Leche evaporada (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Leche evaporada';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Leche evaporada';
-- Lechuga (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lechuga';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lechuga';
-- Lentejas (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lentejas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Lentejas';
-- Levadura (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Levadura';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Levadura';
-- Levadura seca (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Levadura seca';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Levadura seca';
-- Limón (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Limón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Limón';
-- Linaza (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Linaza';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Linaza';
-- Lomo de cerdo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Lomo de cerdo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Lomo de cerdo';
-- Maicena (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Maicena';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Maicena';
-- Mandarina (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mandarina';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mandarina';
-- Mango (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mango';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mango';
-- Mantequilla (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mantequilla';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Mantequilla';
-- Manzana (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Manzana';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Manzana';
-- Media crema (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Media crema';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Media crema';
-- Melón (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Melón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Melón';
-- Miel (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Miel';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Miel';
-- Moras (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Moras';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Moras';
-- Muslo de pollo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Muslo de pollo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Muslo de pollo';
-- Naranja (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Naranja';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Naranja';
-- Nopal (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Nopal';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Nopal';
-- Nueces (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Nueces';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Nueces';
-- Pan de caja (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan de caja';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan de caja';
-- Pan integral (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan integral';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan integral';
-- Pan molido (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan molido';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pan molido';
-- Papa (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Papa';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Papa';
-- Papaya (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Papaya';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Papaya';
-- Pasas (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pasas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pasas';
-- Pasta de coditos (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pasta de coditos';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pasta de coditos';
-- Pasta de espagueti (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pasta de espagueti';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pasta de espagueti';
-- Pasta de macarrones (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pasta de macarrones';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pasta de macarrones';
-- Pechuga de pavo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pechuga de pavo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pechuga de pavo';
-- Pechuga de pollo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pechuga de pollo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pechuga de pollo';
-- Pepino (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pepino';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pepino';
-- Pera (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pera';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pera';
-- Pierna de pollo (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pierna de pollo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pierna de pollo';
-- Piloncillo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Piloncillo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Piloncillo';
-- Pimiento morrón (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pimiento morrón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pimiento morrón';
-- Piña (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Piña';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Piña';
-- Pistaches (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pistaches';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pistaches';
-- Plátano (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Plátano';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Plátano';
-- Pollo entero (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pollo entero';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Pollo entero';
-- Poro (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Poro';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Poro';
-- Pulpo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pulpo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Pulpo';
-- Queso cheddar (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Queso cheddar';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso cheddar';
-- Queso crema (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Queso crema';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso crema';
-- Queso fresco (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Queso fresco';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso fresco';
-- Queso manchego (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Queso manchego';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso manchego';
-- Queso mozzarella (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Queso mozzarella';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso mozzarella';
-- Queso panela (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Queso panela';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Queso panela';
-- Quinoa (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Quinoa';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Quinoa';
-- Rábano (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Rábano';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Rábano';
-- Repollo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Repollo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Repollo';
-- Requesón (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Requesón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Requesón';
-- Salchicha (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Salchicha';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Salchicha';
-- Salmón (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Salmón';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Salmón';
-- Sandía (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Sandía';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Sandía';
-- Sardinas (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Sardinas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Sardinas';
-- Semillas de calabaza (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de calabaza';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de calabaza';
-- Semillas de girasol (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de girasol';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Semillas de girasol';
-- Soya (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Soya';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Soya';
-- Tamarindo (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tamarindo';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tamarindo';
-- Tocino (BULK_SOLID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tocino';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Tocino';
-- Tofu (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tofu';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tofu';
-- Tomate verde (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tomate verde';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tomate verde';
-- Toronja (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Toronja';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Toronja';
-- Tortilla de harina (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tortilla de harina';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tortilla de harina';
-- Tortilla de maíz (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tortilla de maíz';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Tortilla de maíz';
-- Uvas (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Uvas';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Uvas';
-- Yogur griego (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Yogur griego';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Yogur griego';
-- Yogur natural (LIQUID)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)')
  WHERE t.nombre = 'Yogur natural';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Mililitros (ml)', 'Litros (L)', 'Tazas', 'Cucharadas', 'Cucharaditas')
  WHERE t.nombre = 'Yogur natural';
-- Zanahoria (PIECE)
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Zanahoria';
INSERT IGNORE INTO ingrediente_unidad (id_ingrediente, id_unidad, contexto, orden)
  SELECT t.id_ingrediente, u.id_unidad, 'receta', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM ingrediente t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Zanahoria';

-- ---------------- especia ----------------
-- Ajo en polvo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ajo en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Ajo en polvo';
-- Albahaca seca (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Albahaca seca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Albahaca seca';
-- Anís en grano (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Anís en grano';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Anís en grano';
-- Anís estrella (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Anís estrella';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Anís estrella';
-- Azafrán (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Azafrán';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Azafrán';
-- Canela (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Canela';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Canela';
-- Canela en rama (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Canela en rama';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Canela en rama';
-- Cardamomo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cardamomo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Cardamomo';
-- Cebolla en polvo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cebolla en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Cebolla en polvo';
-- Chile ancho seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile ancho seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Chile ancho seco';
-- Chile chipotle seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile chipotle seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Chile chipotle seco';
-- Chile de árbol seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile de árbol seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Chile de árbol seco';
-- Chile en polvo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Chile en polvo';
-- Chile guajillo seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Chile guajillo seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Chile guajillo seco';
-- Cilantro seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cilantro seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Cilantro seco';
-- Clavo de olor (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Clavo de olor';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Clavo de olor';
-- Comino (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Comino';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Comino';
-- Cúrcuma (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Cúrcuma';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Cúrcuma';
-- Curry en polvo (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Curry en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Curry en polvo';
-- Eneldo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Eneldo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Eneldo';
-- Estragón (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Estragón';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Estragón';
-- Hierbabuena seca (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Hierbabuena seca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Hierbabuena seca';
-- Hojuelas de chile (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Hojuelas de chile';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Hojuelas de chile';
-- Jengibre en polvo (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Jengibre en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Jengibre en polvo';
-- Laurel (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Laurel';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Laurel';
-- Mejorana (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Mejorana';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Mejorana';
-- Menta seca (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Menta seca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Menta seca';
-- Nuez moscada (BULK_SOLID)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Nuez moscada';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Nuez moscada';
-- Orégano seco (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Orégano seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Orégano seco';
-- Paprika (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Paprika';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Paprika';
-- Paprika ahumada (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Paprika ahumada';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Paprika ahumada';
-- Perejil seco (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Perejil seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Perejil seco';
-- Pimentón (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Pimentón';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Pimentón';
-- Pimienta blanca (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Pimienta blanca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Pimienta blanca';
-- Pimienta cayena (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Pimienta cayena';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Pimienta cayena';
-- Pimienta negra (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Pimienta negra';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Pimienta negra';
-- Ralladura de limón seca (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ralladura de limón seca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Ralladura de limón seca';
-- Ralladura de naranja seca (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Ralladura de naranja seca';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Ralladura de naranja seca';
-- Romero seco (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Romero seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Romero seco';
-- Sal (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Sal';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Sal';
-- Salvia (PIECE)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Piezas', 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Piezas', 'Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Salvia';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Salvia';
-- Semillas de cilantro (BULK_SOLID)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de cilantro';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Semillas de cilantro';
-- Semillas de hinojo (BULK_SOLID)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de hinojo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Semillas de hinojo';
-- Semillas de mostaza (BULK_SOLID)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Kilogramos (kg)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Kilogramos (kg)')
  WHERE t.nombre = 'Semillas de mostaza';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Semillas de mostaza';
-- Tomillo seco (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Tomillo seco';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Tomillo seco';
-- Vainilla en polvo (CONDIMENT)
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'alacena', FIELD(u.nombre, 'Gramos (gr)', 'Piezas') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Gramos (gr)', 'Piezas')
  WHERE t.nombre = 'Vainilla en polvo';
INSERT IGNORE INTO especia_unidad (id_especia, id_unidad, contexto, orden)
  SELECT t.id_especia, u.id_unidad, 'receta', FIELD(u.nombre, 'Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)') FROM especia t JOIN unidad_medida u ON u.nombre IN ('Al gusto', 'Cucharaditas', 'Cucharadas', 'Gramos (gr)')
  WHERE t.nombre = 'Vainilla en polvo';

