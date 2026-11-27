-- ============================================================
-- Reglas de unidades DENTRO de la base de datos.
--
-- Aunque alguien inserte directo (phpMyAdmin, otro script, un bug
-- en el API), MySQL rechaza una unidad que no esté configurada para
-- ese alimento en ingrediente_unidad / especia_unidad.
--
-- Igual que el API: si un alimento todavía no tiene medidas
-- configuradas, se aceptan las unidades marcadas como "por defecto".
--
-- Ejecutar con:  mysql -uroot db_recetas < 05_triggers_unidades.sql
-- (después de 01, 02 y 04). Se puede ejecutar varias veces.
-- ============================================================

DROP FUNCTION IF EXISTS unidad_permitida;

DELIMITER $$

-- tipo: 'ingrediente' o 'especia'; contexto: 'alacena' o 'receta'
CREATE FUNCTION unidad_permitida(p_tipo VARCHAR(20), p_id INT, p_contexto VARCHAR(10), p_unidad VARCHAR(50))
RETURNS TINYINT(1)
READS SQL DATA
BEGIN
    DECLARE configuradas INT DEFAULT 0;
    DECLARE coincide INT DEFAULT 0;

    IF p_tipo = 'especia' THEN
        SELECT COUNT(*) INTO configuradas FROM especia_unidad
         WHERE id_especia = p_id AND contexto = p_contexto;
        SELECT COUNT(*) INTO coincide FROM especia_unidad eu
          JOIN unidad_medida u ON u.id_unidad = eu.id_unidad
         WHERE eu.id_especia = p_id AND eu.contexto = p_contexto AND u.nombre = p_unidad;
    ELSE
        SELECT COUNT(*) INTO configuradas FROM ingrediente_unidad
         WHERE id_ingrediente = p_id AND contexto = p_contexto;
        SELECT COUNT(*) INTO coincide FROM ingrediente_unidad iu
          JOIN unidad_medida u ON u.id_unidad = iu.id_unidad
         WHERE iu.id_ingrediente = p_id AND iu.contexto = p_contexto AND u.nombre = p_unidad;
    END IF;

    IF configuradas > 0 THEN
        RETURN coincide > 0;
    END IF;

    -- Sin medidas configuradas: unidades por defecto del contexto
    SELECT COUNT(*) INTO coincide FROM unidad_medida
     WHERE nombre = p_unidad
       AND ((p_contexto = 'alacena' AND defecto_alacena = 1)
         OR (p_contexto = 'receta'  AND defecto_receta = 1));
    RETURN coincide > 0;
END$$

-- ---------------- alacena ----------------
DROP TRIGGER IF EXISTS trg_alacena_unidad_ins$$
CREATE TRIGGER trg_alacena_unidad_ins BEFORE INSERT ON alacena FOR EACH ROW
BEGIN
    IF NOT unidad_permitida('ingrediente', NEW.id_ingrediente, 'alacena', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para este ingrediente en la alacena';
    END IF;
END$$

DROP TRIGGER IF EXISTS trg_alacena_unidad_upd$$
CREATE TRIGGER trg_alacena_unidad_upd BEFORE UPDATE ON alacena FOR EACH ROW
BEGIN
    IF NOT unidad_permitida('ingrediente', NEW.id_ingrediente, 'alacena', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para este ingrediente en la alacena';
    END IF;
END$$

-- ---------------- receta_ingrediente ----------------
DROP TRIGGER IF EXISTS trg_receta_ingrediente_unidad_ins$$
CREATE TRIGGER trg_receta_ingrediente_unidad_ins BEFORE INSERT ON receta_ingrediente FOR EACH ROW
BEGIN
    IF NOT unidad_permitida('ingrediente', NEW.id_ingrediente, 'receta', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para este ingrediente en la receta';
    END IF;
END$$

DROP TRIGGER IF EXISTS trg_receta_ingrediente_unidad_upd$$
CREATE TRIGGER trg_receta_ingrediente_unidad_upd BEFORE UPDATE ON receta_ingrediente FOR EACH ROW
BEGIN
    IF NOT unidad_permitida('ingrediente', NEW.id_ingrediente, 'receta', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para este ingrediente en la receta';
    END IF;
END$$

-- ---------------- receta_especia (unidad puede ser NULL) ----------------
DROP TRIGGER IF EXISTS trg_receta_especia_unidad_ins$$
CREATE TRIGGER trg_receta_especia_unidad_ins BEFORE INSERT ON receta_especia FOR EACH ROW
BEGIN
    IF NEW.unidad IS NOT NULL AND NOT unidad_permitida('especia', NEW.id_especia, 'receta', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para esta especia en la receta';
    END IF;
END$$

DROP TRIGGER IF EXISTS trg_receta_especia_unidad_upd$$
CREATE TRIGGER trg_receta_especia_unidad_upd BEFORE UPDATE ON receta_especia FOR EACH ROW
BEGIN
    IF NEW.unidad IS NOT NULL AND NOT unidad_permitida('especia', NEW.id_especia, 'receta', NEW.unidad) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Unidad no permitida para esta especia en la receta';
    END IF;
END$$

DELIMITER ;
