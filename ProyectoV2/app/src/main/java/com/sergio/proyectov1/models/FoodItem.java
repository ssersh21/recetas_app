package com.sergio.proyectov1.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodItem {
    @SerializedName("fdcId")
    private int fdcId;
    @SerializedName("description")
    private String description;
    @SerializedName("brandOwner")
    private String brandOwner;
    @SerializedName("foodNutrients")
    private List<FoodNutrient> foodNutrients;

    public int getFdcId() { return fdcId; }
    public String getDescription() { return description; }
    public String getBrandOwner() { return brandOwner; }
    public List<FoodNutrient> getFoodNutrients() { return foodNutrients; }
}
