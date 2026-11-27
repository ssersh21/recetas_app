package com.sergio.proyectov1.plan;

import java.io.Serializable;

/** Receta asignada a un tiempo de comida de un día (tabla plan_comida). */
public class PlannedMeal implements Serializable {
    private final int recipeId;
    private final String title;
    private final double calories; // por porción
    private String time;           // "HH:MM" o null
    private boolean cooked;

    public PlannedMeal(int recipeId, String title, double calories) {
        this.recipeId = recipeId;
        this.title = title;
        this.calories = calories;
    }

    public int getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public double getCalories() { return calories; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isCooked() { return cooked; }
    public void setCooked(boolean cooked) { this.cooked = cooked; }
}
