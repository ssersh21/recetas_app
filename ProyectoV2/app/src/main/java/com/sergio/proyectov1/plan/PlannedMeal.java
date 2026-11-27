package com.sergio.proyectov1.plan;

import java.io.Serializable;

/** Receta asignada a un tiempo de comida de un día. */
public class PlannedMeal implements Serializable {
    private final int recipeId;
    private final String title;
    private final double calories; // por porción

    public PlannedMeal(int recipeId, String title, double calories) {
        this.recipeId = recipeId;
        this.title = title;
        this.calories = calories;
    }

    public int getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public double getCalories() { return calories; }
}
