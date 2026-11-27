package com.sergio.proyectov1.plan;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sergio.proyectov1.api.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Plan alimenticio guardado en la base de datos (plan.php → tabla plan_comida):
 * fecha (yyyy-MM-dd) -> tiempo de comida -> receta.
 * Las respuestas llegan en el hilo principal.
 */
public class PlanStore {

    /** Tiempos de comida según la estructura elegida (RQNF3.5). */
    public static String[] slots(int mealTimes) {
        return mealTimes == 5
                ? new String[]{"Desayuno", "Colación matutina", "Comida", "Colación vespertina", "Cena"}
                : new String[]{"Desayuno", "Comida", "Cena"};
    }

    public interface DayCallback {
        /** @param day null si no se pudo consultar; error trae el motivo */
        void onResult(@Nullable Map<String, PlannedMeal> day, @Nullable String error);
    }

    public interface ResultCallback {
        void onResult(boolean ok, @NonNull String message);
    }

    private final Context context;

    public PlanStore(Context context) {
        this.context = context.getApplicationContext();
    }

    private int userId() {
        return ApiHelper.userId(context);
    }

    public void getDay(String date, @NonNull DayCallback callback) {
        if (userId() <= 0) {
            callback.onResult(null, "Inicia sesión para ver tu plan");
            return;
        }
        migrateLocalPlan(() ->
                ApiHelper.get(context, "plan.php?id_usuario=" + userId() + "&fecha=" + date, (ok, message, body) -> {
                    if (!ok || body == null) {
                        callback.onResult(null, message);
                        return;
                    }
                    try {
                        JSONArray array = new JSONArray(body);
                        Map<String, PlannedMeal> day = new HashMap<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            PlannedMeal meal = new PlannedMeal(o.getInt("id_receta"), o.getString("receta"),
                                    o.optDouble("calorias_porcion", 0));
                            meal.setTime(o.isNull("hora") ? null : o.optString("hora"));
                            meal.setCooked(o.optBoolean("cocinada"));
                            day.put(o.getString("tiempo"), meal);
                        }
                        callback.onResult(day, null);
                    } catch (Exception e) {
                        callback.onResult(null, "Respuesta no válida del servidor");
                    }
                }));
    }

    public void set(String date, String slot, int recipeId, @Nullable String time, @NonNull ResultCallback callback) {
        Map<String, String> p = base("asignar", date, slot);
        p.put("id_receta", String.valueOf(recipeId));
        if (time != null) p.put("hora", time);
        post(p, callback);
    }

    public void remove(String date, String slot, @NonNull ResultCallback callback) {
        post(base("quitar", date, slot), callback);
    }

    public void setCooked(String date, String slot, boolean cooked, @NonNull ResultCallback callback) {
        Map<String, String> p = base("cocinada", date, slot);
        p.put("cocinada", cooked ? "1" : "0");
        post(p, callback);
    }

    private Map<String, String> base(String action, String date, String slot) {
        Map<String, String> p = new HashMap<>();
        p.put("accion", action);
        p.put("id_usuario", String.valueOf(userId()));
        p.put("fecha", date);
        p.put("tiempo", slot);
        return p;
    }

    private void post(Map<String, String> params, ResultCallback callback) {
        if (userId() <= 0) {
            callback.onResult(false, "Inicia sesión para usar tu plan");
            return;
        }
        ApiHelper.post(context, "plan.php", params, (ok, message, body) -> callback.onResult(ok, message));
    }

    // ------------------------------------------------------------------
    // Migración única: el plan que antes se guardaba en el teléfono se sube a la BD
    // ------------------------------------------------------------------

    private static final Type OLD_TYPE =
            new TypeToken<HashMap<String, HashMap<String, PlannedMeal>>>() {}.getType();

    private void migrateLocalPlan(Runnable then) {
        SharedPreferences prefs = context.getSharedPreferences("PlanPrefs", Context.MODE_PRIVATE);
        String key = "plan_" + userId();
        String json = prefs.getString(key, null);
        if (json == null) {
            then.run();
            return;
        }
        Map<String, HashMap<String, PlannedMeal>> old;
        try {
            old = new Gson().fromJson(json, OLD_TYPE);
        } catch (Exception e) {
            old = null;
        }
        // Se borra antes de subir para no repetir la migración si algo falla a medias
        prefs.edit().remove(key).apply();
        if (old == null || old.isEmpty()) {
            then.run();
            return;
        }
        int[] pending = {0};
        for (Map.Entry<String, HashMap<String, PlannedMeal>> day : old.entrySet()) {
            for (Map.Entry<String, PlannedMeal> slot : day.getValue().entrySet()) {
                if (slot.getValue() == null || slot.getValue().getRecipeId() <= 0) continue;
                pending[0]++;
                set(day.getKey(), slot.getKey(), slot.getValue().getRecipeId(), null, (ok, msg) -> {
                    if (--pending[0] == 0) then.run();
                });
            }
        }
        if (pending[0] == 0) then.run();
    }
}
