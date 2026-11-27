package com.sergio.proyectov1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfile implements Serializable {
    @SerializedName(value = "username", alternate = {"nombre", "nombre_usuario"})
    private String username;

    @SerializedName(value = "email", alternate = {"correo"})
    private String email;

    @SerializedName(value = "age", alternate = {"edad"})
    private int age;

    @SerializedName(value = "weight", alternate = {"peso"})
    private double weight;

    @SerializedName(value = "height", alternate = {"altura", "estatura"})
    private double height;

    @SerializedName(value = "sex", alternate = {"sexo", "genero"})
    private String sex; // "Hombre" or "Mujer"

    @SerializedName(value = "activityLevel", alternate = {"nivel_actividad", "activity_level"})
    private String activityLevel;

    @SerializedName(value = "cookingLevel", alternate = {"nivel_cocina", "cooking_level"})
    private String cookingLevel;

    @SerializedName(value = "availableTimeMinutes", alternate = {"tiempo_disponible", "available_time_minutes"})
    private int availableTimeMinutes;

    @SerializedName(value = "allergies", alternate = {"alergias"})
    private List<String> allergies = new ArrayList<>();

    @SerializedName(value = "dislikes", alternate = {"aversiones", "no_me_gusta"})
    private List<String> dislikes = new ArrayList<>();

    @SerializedName(value = "goal", alternate = {"meta", "objetivo"})
    private String goal; // "Bajar de peso", "Ganar músculo", "Comer saludable", "Modo explorador"

    @SerializedName(value = "mealTimes", alternate = {"tiempos_comida", "comidas_dia", "meal_times"})
    private int mealTimes; // 3 or 5

    @SerializedName(value = "activityIndex", alternate = {"activity_index", "indice_actividad"})
    private int activityIndex;

    @SerializedName(value = "goalIndex", alternate = {"goal_index", "indice_meta"})
    private int goalIndex;

    // Results of calculations
    @SerializedName(value = "tmb", alternate = {"bmr"})
    private double tmb;

    @SerializedName(value = "tdee")
    private double tdee;

    @SerializedName(value = "dailyCalorieTarget", alternate = {"calorias_diarias", "daily_calorie_target", "meta_calorias"})
    private double dailyCalorieTarget;

    public UserProfile() {}

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getCookingLevel() { return cookingLevel; }
    public void setCookingLevel(String cookingLevel) { this.cookingLevel = cookingLevel; }

    public int getAvailableTimeMinutes() { return availableTimeMinutes; }
    public void setAvailableTimeMinutes(int availableTimeMinutes) { this.availableTimeMinutes = availableTimeMinutes; }

    public List<String> getAllergies() { return allergies != null ? allergies : new ArrayList<>(); }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<String> getDislikes() { return dislikes != null ? dislikes : new ArrayList<>(); }
    public void setDislikes(List<String> dislikes) { this.dislikes = dislikes; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public int getMealTimes() { return mealTimes; }
    public void setMealTimes(int mealTimes) { this.mealTimes = mealTimes; }

    public int getActivityIndex() { return activityIndex; }
    public void setActivityIndex(int activityIndex) { this.activityIndex = activityIndex; }

    public int getGoalIndex() { return goalIndex; }
    public void setGoalIndex(int goalIndex) { this.goalIndex = goalIndex; }

    public double getTmb() { return tmb; }
    public void setTmb(double tmb) { this.tmb = tmb; }

    public double getTdee() { return tdee; }
    public void setTdee(double tdee) { this.tdee = tdee; }

    public double getDailyCalorieTarget() { return dailyCalorieTarget; }
    public void setDailyCalorieTarget(double dailyCalorieTarget) { this.dailyCalorieTarget = dailyCalorieTarget; }
}
