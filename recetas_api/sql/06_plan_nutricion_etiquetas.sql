-- ============================================================
-- 1) Plan de comidas en la BD (antes vivía en el teléfono)
-- 2) Valores nutricionales por porción guardados en la receta
--    (los calcula la app con USDA al guardar; el servidor los usa
--    para poner etiquetas automáticas y el Plan para sumar calorías)
-- 3) Etiquetas automáticas
-- Se puede ejecutar varias veces.
-- ============================================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS plan_comida (
    id_plan    INT(11)     NOT NULL AUTO_INCREMENT,
    id_usuario INT(11)     NOT NULL,
    fecha      DATE        NOT NULL,
    tiempo     VARCHAR(30) NOT NULL,           -- Desayuno, Comida, Cena, colaciones
    hora       TIME        DEFAULT NULL,
    id_receta  INT(11)     NOT NULL,
    cocinada   TINYINT(1)  NOT NULL DEFAULT 0, -- historial de recetas cocinadas
    PRIMARY KEY (id_plan),
    UNIQUE KEY uq_plan_tiempo (id_usuario, fecha, tiempo),
    KEY idx_plan_receta (id_receta),
    CONSTRAINT fk_plan_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_plan_receta FOREIGN KEY (id_receta)
        REFERENCES receta (id_receta) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Valores por UNA porción (NULL = no se pudo calcular)
ALTER TABLE receta
    ADD COLUMN IF NOT EXISTS calorias_porcion      DECIMAL(8,2) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS proteinas_porcion     DECIMAL(8,2) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS carbohidratos_porcion DECIMAL(8,2) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS grasas_porcion        DECIMAL(8,2) DEFAULT NULL;

-- automatica = 1: la pone el sistema y el usuario no puede quitarla
ALTER TABLE etiqueta
    ADD COLUMN IF NOT EXISTS automatica TINYINT(1) NOT NULL DEFAULT 0;

INSERT INTO etiqueta (nombre, automatica) VALUES
    ('Saludable', 1),
    ('Alta en proteína', 1),
    ('Rápida', 1)
ON DUPLICATE KEY UPDATE automatica = 1;
