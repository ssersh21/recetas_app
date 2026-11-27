-- ============================================================
-- Medidas compatibles por alimento
--
-- Antes estas reglas vivían en el código de la app
-- (FoodUnitHelper.java). Ahora se administran desde aquí:
-- para cambiar qué medidas acepta un alimento basta con
-- insertar / borrar filas en ingrediente_unidad o especia_unidad.
--
-- contexto:
--   'alacena' -> medidas para registrar existencias
--   'receta'  -> medidas para usar el alimento en una receta
-- ============================================================

CREATE TABLE IF NOT EXISTS unidad_medida (
    id_unidad      INT(11)      NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(50)  NOT NULL,         -- texto que ve el usuario y se guarda en receta_ingrediente.unidad
    abreviatura    VARCHAR(10)  DEFAULT NULL,
    orden          INT(11)      NOT NULL DEFAULT 0,
    -- Si un alimento todavía no tiene medidas configuradas,
    -- se ofrecen las unidades marcadas como "por defecto" en ese contexto.
    defecto_alacena TINYINT(1)  NOT NULL DEFAULT 0,
    defecto_receta  TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (id_unidad),
    UNIQUE KEY uq_unidad_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS ingrediente_unidad (
    id_ingrediente INT(11) NOT NULL,
    id_unidad      INT(11) NOT NULL,
    contexto       ENUM('alacena','receta') NOT NULL,
    orden          INT(11) NOT NULL DEFAULT 0,          -- posición en la lista (la primera se selecciona por defecto)
    PRIMARY KEY (id_ingrediente, id_unidad, contexto),
    KEY idx_iu_unidad (id_unidad),
    CONSTRAINT fk_iu_ingrediente FOREIGN KEY (id_ingrediente)
        REFERENCES ingrediente (id_ingrediente) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_iu_unidad FOREIGN KEY (id_unidad)
        REFERENCES unidad_medida (id_unidad) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS especia_unidad (
    id_especia INT(11) NOT NULL,
    id_unidad  INT(11) NOT NULL,
    contexto   ENUM('alacena','receta') NOT NULL,
    orden      INT(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_especia, id_unidad, contexto),
    KEY idx_eu_unidad (id_unidad),
    CONSTRAINT fk_eu_especia FOREIGN KEY (id_especia)
        REFERENCES especia (id_especia) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_eu_unidad FOREIGN KEY (id_unidad)
        REFERENCES unidad_medida (id_unidad) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Catálogo de unidades (mismos textos que ya usa la app y que ya
-- están guardados en receta_ingrediente / receta_especia).
INSERT INTO unidad_medida (nombre, abreviatura, orden, defecto_alacena, defecto_receta) VALUES
    ('Piezas',           'pza', 1, 1, 1),
    ('Gramos (gr)',      'gr',  2, 1, 1),
    ('Kilogramos (kg)',  'kg',  3, 1, 1),
    ('Mililitros (ml)',  'ml',  4, 1, 1),
    ('Litros (L)',       'L',   5, 1, 1),
    ('Tazas',            NULL,  6, 0, 1),
    ('Cucharadas',       NULL,  7, 0, 1),
    ('Cucharaditas',     NULL,  8, 0, 1),
    ('Al gusto',         NULL,  9, 0, 0)
ON DUPLICATE KEY UPDATE
    abreviatura = VALUES(abreviatura),
    orden = VALUES(orden),
    defecto_alacena = VALUES(defecto_alacena),
    defecto_receta = VALUES(defecto_receta);
