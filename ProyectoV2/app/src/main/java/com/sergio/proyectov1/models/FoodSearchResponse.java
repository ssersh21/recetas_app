package com.sergio.proyectov1.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodSearchResponse {
    @SerializedName("totalHits")
    private int totalHits;
    @SerializedName("foods")
    private List<FoodItem> foods;

    public int getTotalHits() { return totalHits; }
    public List<FoodItem> getFoods() { return foods; }
}
