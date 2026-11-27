package com.sergio.proyectov1.models;
import com.google.gson.annotations.SerializedName;

public class FoodNutrient {
    @SerializedName("nutrientName")
    private String nutrientName;
    @SerializedName("value")
    private float value;
    @SerializedName("unitName")
    private String unitName;

    public String getNutrientName() { return nutrientName; }
    public float getValue() { return value; }
    public String getUnitName() { return unitName; }
}
