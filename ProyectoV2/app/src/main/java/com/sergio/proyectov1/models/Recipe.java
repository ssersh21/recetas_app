package com.sergio.proyectov1.models;

import java.io.Serializable;

public class Recipe implements Serializable {
    // Id en la base de datos (0 si la receta aún no viene del servidor)
    private int id;
    private String title;
    private String time;
    private int imageRes;
    private String portions;
    private String ingredients;
    private String steps;

    // Datos que vienen de la base de datos
    private String difficulty;      // principiante | intermedio | avanzado
    private String description;
    private String imageUrl;
    private double ratingAverage;
    private int ratingCount;
    
    // Autor, etiquetas y utensilios (vienen de recetas.php)
    private int ownerId;
    private String author;
    private java.util.List<String> tags = new java.util.ArrayList<>();
    private java.util.List<String> utensils = new java.util.ArrayList<>();
    /** Datos completos tal como llegaron del servidor (para editar la receta). */
    private RecipeDto source;

    // Macronutrientes TOTALES de la receta (RecipeDetailFragment los divide entre las porciones)
    private double calories;
    private double protein;
    private double carbs;
    private double fats;

    public Recipe(String title, String time, int imageRes, String portions, String ingredients, String steps) {
        this.title = title;
        this.time = time;
        this.imageRes = imageRes;
        this.portions = portions;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public java.util.List<String> getTags() { return tags; }
    public void setTags(java.util.List<String> tags) { this.tags = tags; }
    public boolean hasTag(String tag) {
        for (String t : tags) if (t.equalsIgnoreCase(tag)) return true;
        return false;
    }

    public java.util.List<String> getUtensils() { return utensils; }
    public void setUtensils(java.util.List<String> utensils) { this.utensils = utensils; }

    public RecipeDto getSource() { return source; }
    public void setSource(RecipeDto source) { this.source = source; }

    /** Calorías de UNA porción (0 si no se han calculado). */
    public double getCaloriesPerPortion() {
        int p = getPortionsCount();
        return p > 0 ? calories / p : calories;
    }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getRatingAverage() { return ratingAverage; }
    public int getRatingCount() { return ratingCount; }
    public void setRating(double average, int count) {
        this.ratingAverage = average;
        this.ratingCount = count;
    }

    /** Minutos de preparación, sacados del texto "35 min" (0 si no hay). */
    public int getTimeMinutes() {
        try {
            return Integer.parseInt(time.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getPortions() {
        return portions;
    }

    public int getPortionsCount() {
        try {
            return Integer.parseInt(portions.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 1;
        }
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFats() { return fats; }
    public void setFats(double fats) { this.fats = fats; }

    public String getDietaryLabel() {
        if (calories == 0) return "";
        StringBuilder labels = new StringBuilder();
        if (calories < 300) labels.append("Baja en Calorías ");
        if (protein > 20) labels.append("Alta en Proteína ");
        if (carbs < 10) labels.append("Baja en Carbos ");
        return labels.toString().trim();
    }
}
