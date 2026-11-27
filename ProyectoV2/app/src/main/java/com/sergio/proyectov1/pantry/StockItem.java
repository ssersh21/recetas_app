package com.sergio.proyectov1.pantry;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/** Un ingrediente de la alacena o de la lista de compras. */
public class StockItem implements Serializable {
    private String name;
    private double quantity;
    private String unit;

    /** id del ingrediente en la BD (0 = registro viejo sin ingrediente de la BD). */
    private int ingredientId;
    /** id del renglón en la tabla alacena (solo en la alacena). */
    private int pantryId;
    /** Medidas permitidas en la alacena para este ingrediente (vienen del servidor). */
    private transient List<String> allowedUnits;

    public StockItem(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public StockItem(int ingredientId, String name, double quantity, String unit) {
        this(name, quantity, unit);
        this.ingredientId = ingredientId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public int getPantryId() { return pantryId; }
    public void setPantryId(int pantryId) { this.pantryId = pantryId; }

    public List<String> getAllowedUnits() {
        return allowedUnits != null ? allowedUnits : Collections.emptyList();
    }
    public void setAllowedUnits(List<String> allowedUnits) { this.allowedUnits = allowedUnits; }

    /** Mismo ingrediente en la misma unidad (sin importar mayúsculas). */
    public boolean sameAs(StockItem other) {
        boolean sameIngredient = ingredientId > 0 && other.ingredientId > 0
                ? ingredientId == other.ingredientId
                : name.trim().equalsIgnoreCase(other.name.trim());
        return sameIngredient
                && (unit == null ? other.unit == null : unit.equals(other.unit));
    }
}
