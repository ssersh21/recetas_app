package com.sergio.proyectov1.plan;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Plan alimenticio guardado en el teléfono: fecha (yyyy-MM-dd) -> tiempo de comida -> receta.
 * Es el punto único de acceso; la sincronización con el servidor (RQNF6.1.3) se agregaría aquí.
 */
public class PlanStore {

    /** Tiempos de comida según la estructura elegida (RQNF3.5). */
    public static String[] slots(int mealTimes) {
        return mealTimes == 5
                ? new String[]{"Desayuno", "Colación matutina", "Comida", "Colación vespertina", "Cena"}
                : new String[]{"Desayuno", "Comida", "Cena"};
    }

    private static final Type TYPE =
            new TypeToken<HashMap<String, HashMap<String, PlannedMeal>>>() {}.getType();

    private final SharedPreferences prefs;
    private final String key;
    private final Gson gson = new Gson();

    public PlanStore(Context context) {
        Context app = context.getApplicationContext();
        prefs = app.getSharedPreferences("PlanPrefs", Context.MODE_PRIVATE);
        int userId = app.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("user_id", 0);
        key = "plan_" + userId;
    }

    private Map<String, HashMap<String, PlannedMeal>> loadAll() {
        String json = prefs.getString(key, null);
        if (json == null) return new HashMap<>();
        try {
            Map<String, HashMap<String, PlannedMeal>> all = gson.fromJson(json, TYPE);
            return all == null ? new HashMap<>() : all;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Map<String, PlannedMeal> getDay(String date) {
        Map<String, HashMap<String, PlannedMeal>> all = loadAll();
        HashMap<String, PlannedMeal> day = all.get(date);
        return day == null ? new HashMap<>() : day;
    }

    public void set(String date, String slot, PlannedMeal meal) {
        Map<String, HashMap<String, PlannedMeal>> all = loadAll();
        HashMap<String, PlannedMeal> day = all.get(date);
        if (day == null) {
            day = new HashMap<>();
            all.put(date, day);
        }
        day.put(slot, meal);
        prefs.edit().putString(key, gson.toJson(all)).apply();
    }

    public void remove(String date, String slot) {
        Map<String, HashMap<String, PlannedMeal>> all = loadAll();
        HashMap<String, PlannedMeal> day = all.get(date);
        if (day == null) return;
        day.remove(slot);
        if (day.isEmpty()) all.remove(date);
        prefs.edit().putString(key, gson.toJson(all)).apply();
    }
}
