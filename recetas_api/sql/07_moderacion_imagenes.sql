-- ============================================================
-- Moderación de imágenes (Google Cloud Vision SafeSearch) y avisos.
--
-- Cada imagen que se sube pasa por SafeSearch ANTES de guardarse:
--   aprobada  -> se guarda y la receta se publica
--   revision  -> se guarda, la receta queda 'pendiente_revision' y el
--                administrador decide (zona gris)
--   rechazada -> NO se guarda; solo queda el registro con los puntajes
-- Se puede ejecutar varias veces.
-- ============================================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS moderacion_imagen (
    id_moderacion  INT(11)      NOT NULL AUTO_INCREMENT,
    id_usuario     INT(11)      NOT NULL,
    ruta           VARCHAR(255) DEFAULT NULL,   -- uploads/... (NULL si se rechazó y no se guardó)
    id_receta      INT(11)      DEFAULT NULL,   -- se llena al guardar la receta
    adult          VARCHAR(15)  DEFAULT NULL,   -- VERY_UNLIKELY ... VERY_LIKELY (NULL = sin análisis)
    violence       VARCHAR(15)  DEFAULT NULL,
    racy           VARCHAR(15)  DEFAULT NULL,
    medical        VARCHAR(15)  DEFAULT NULL,
    spoof          VARCHAR(15)  DEFAULT NULL,
    resultado      ENUM('aprobada','revision','rechazada') NOT NULL,
    motivo         VARCHAR(255) DEFAULT NULL,   -- por qué quedó en revisión o por qué la rechazó el admin
    revisado_por   INT(11)      DEFAULT NULL,   -- administrador que decidió (zona gris)
    fecha          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_revision DATETIME     DEFAULT NULL,
    PRIMARY KEY (id_moderacion),
    UNIQUE KEY uq_moderacion_ruta (ruta),
    KEY idx_moderacion_resultado (resultado),
    KEY idx_moderacion_receta (id_receta),
    CONSTRAINT fk_moderacion_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_moderacion_receta FOREIGN KEY (id_receta)
        REFERENCES receta (id_receta) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_moderacion_admin FOREIGN KEY (revisado_por)
        REFERENCES usuario (id_usuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Avisos para el usuario (receta aprobada / rechazada) y para el administrador
-- (nueva imagen en la cola de revisión). La app los muestra al abrirse.
CREATE TABLE IF NOT EXISTS notificacion (
    id_notificacion INT(11)      NOT NULL AUTO_INCREMENT,
    id_usuario      INT(11)      NOT NULL,
    tipo            VARCHAR(30)  NOT NULL,      -- moderacion, revision_pendiente, ...
    titulo          VARCHAR(120) NOT NULL,
    mensaje         VARCHAR(500) NOT NULL,
    id_receta       INT(11)      DEFAULT NULL,
    leida           TINYINT(1)   NOT NULL DEFAULT 0,
    fecha           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_notificacion),
    KEY idx_notificacion_usuario (id_usuario, leida),
    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_notificacion_receta FOREIGN KEY (id_receta)
        REFERENCES receta (id_receta) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
