package com.sergio.proyectov1.models;

import java.util.List;
import java.util.List;

public class Category {
    private String title;
    private List<Recipe> recipes;

    public Category(String title, List<Recipe> recipes) {
        this.title = title;
        this.recipes = recipes;
    }

    public String getTitle() {
        return title;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}