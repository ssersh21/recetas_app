package com.sergio.proyectov1.personalization;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.models.ProfileResponse;
import com.sergio.proyectov1.models.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Gestiona el perfil del usuario tanto en almacenamiento local (caché offline)
 * como en la base de datos remota mediante recetas_api (guardar_perfil.php y obtener_perfil.php).
 */
public class ProfileStore {

    private static final String TAG = "ProfileStore";
    private static final String PREFS = "ProfilePrefs";

    public interface ProfileCallback {
        void onSuccess(UserProfile profile);
        void onError(String message);
    }

    public interface SaveCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences session;
    private final Gson gson = new Gson();

    public ProfileStore(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        this.session = this.context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
    }

    public int getUserId() {
        return session.getInt("user_id", 0);
    }

    private String key() {
        return "profile_" + getUserId();
    }

    public boolean exists() {
        return prefs.contains(key());
    }

    /** Carga el perfil desde almacenamiento local (SharedPreferences). */
    public UserProfile load() {
        String json = prefs.getString(key(), null);
        if (json == null) return null;
        try {
            return gson.fromJson(json, UserProfile.class);
        } catch (Exception e) {
            return null;
        }
    }

    /** Guarda el perfil únicamente en almacenamiento local. */
    public void saveLocal(UserProfile profile) {
        if (profile == null) return;
        prefs.edit().putString(key(), gson.toJson(profile)).apply();
    }

    public void clear() {
        prefs.edit().remove(key()).apply();
    }

    public void save(UserProfile profile, @Nullable SaveCallback callback) {
        // Guardar primero en local para disponibilidad inmediata
        saveLocal(profile);

        int userId = getUserId();
        if (userId <= 0) {
            if (callback != null) callback.onError("Usuario no autenticado");
            return;
        }

        try {
            JSONObject body = new JSONObject();
            body.put("id_usuario", userId);
            body.put("nombre", profile.getUsername() != null ? profile.getUsername() : "");
            body.put("edad", profile.getAge());
            body.put("peso", profile.getWeight());
            body.put("altura", profile.getHeight());
            body.put("sexo", profile.getSex() != null ? profile.getSex() : "Hombre");
            body.put("nivel_actividad", profile.getActivityLevel() != null ? profile.getActivityLevel() : "");
            body.put("indice_actividad", profile.getActivityIndex());
            body.put("objetivo", profile.getGoal() != null ? profile.getGoal() : "");
            body.put("indice_meta", profile.getGoalIndex());
            body.put("nivel_cocina", profile.getCookingLevel() != null ? profile.getCookingLevel() : "");
            body.put("tiempo_disponible", profile.getAvailableTimeMinutes());
            body.put("tiempos_comida", profile.getMealTimes());
            body.put("tmb", profile.getTmb());
            body.put("tdee", profile.getTdee());
            body.put("calorias_diarias", profile.getDailyCalorieTarget());

            JSONArray jsonAllergies = new JSONArray();
            if (profile.getAllergies() != null) {
                for (String a : profile.getAllergies()) jsonAllergies.put(a);
            }
            body.put("alergias", jsonAllergies);

            JSONArray jsonDislikes = new JSONArray();
            if (profile.getDislikes() != null) {
                for (String d : profile.getDislikes()) jsonDislikes.put(d);
            }
            body.put("aversiones", jsonDislikes);

            String url = VolleySingleton.BASE_URL + "guardar_perfil.php";

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    body,
                    response -> {
                        boolean success = response.optBoolean("success", true);
                        String msg = response.optString("message", "Perfil sincronizado con la base de datos");
                        Log.d(TAG, "guardar_perfil resp: " + response.toString());
                        if (callback != null) {
                            if (success) {
                                callback.onSuccess(msg);
                            } else {
                                callback.onError(msg);
                            }
                        }
                    },
                    error -> {
                        String errMsg;
                        if (error.networkResponse != null) {
                            // Sí hubo conexión: el servidor respondió con un error (no es problema de IP)
                            errMsg = "El servidor no pudo guardar el perfil (HTTP " + error.networkResponse.statusCode + ")";
                            if (error.networkResponse.data != null) {
                                try {
                                    String responseBody = new String(error.networkResponse.data, java.nio.charset.StandardCharsets.UTF_8);
                                    String serverMsg = new JSONObject(responseBody).optString("message", "");
                                    if (!serverMsg.isEmpty()) errMsg += ": " + serverMsg;
                                } catch (Exception ignored) {}
                            }
                        } else {
                            // No hubo respuesta: servidor apagado, IP equivocada o sin red
                            errMsg = "No se pudo conectar con " + url + ". Revisa que XAMPP esté encendido y la IP en local.properties";
                        }
                        Log.e(TAG, "Error guardando perfil en BD: " + errMsg);
                        if (callback != null) {
                            callback.onError(errMsg);
                        }
                    }
            );

            VolleySingleton.getInstance(context).addToRequestQueue(request);

        } catch (JSONException e) {
            Log.e(TAG, "Error formando JSON del perfil", e);
            if (callback != null) callback.onError("Error al estructurar los datos del perfil");
        }
    }

    /**
     * Obtiene el perfil desde la base de datos remota (obtener_perfil.php?id_usuario=X).
     * Si tiene éxito, actualiza la copia local.
     */
    public void fetchRemoteProfile(ProfileCallback callback) {
        int userId = getUserId();
        if (userId <= 0) {
            callback.onError("Usuario no autenticado");
            return;
        }

        String url = VolleySingleton.BASE_URL + "obtener_perfil.php?id_usuario=" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.optBoolean("success", false);
                        if (!success && !response.has("perfil") && !response.has("edad")) {
                            callback.onError(response.optString("message", "No se encontró el perfil en el servidor"));
                            return;
                        }

                        JSONObject targetJson = response.has("perfil") ? response.getJSONObject("perfil") : response;
                        UserProfile p = gson.fromJson(targetJson.toString(), UserProfile.class);

                        // Si vienen alergias o aversiones en formato JSON Array o string
                        if (targetJson.has("alergias")) {
                            try {
                                JSONArray arr = targetJson.getJSONArray("alergias");
                                ArrayList<String> list = new ArrayList<>();
                                for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
                                p.setAllergies(list);
                            } catch (Exception ignored) {}
                        }

                        if (targetJson.has("aversiones")) {
                            try {
                                JSONArray arr = targetJson.getJSONArray("aversiones");
                                ArrayList<String> list = new ArrayList<>();
                                for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
                                p.setDislikes(list);
                            } catch (Exception ignored) {}
                        }

                        // Guardar en almacenamiento local
                        saveLocal(p);
                        callback.onSuccess(p);

                    } catch (Exception e) {
                        Log.e(TAG, "Error parseando perfil remoto", e);
                        callback.onError("Error al procesar los datos del servidor");
                    }
                },
                error -> {
                    String msg = error.getMessage() != null ? error.getMessage() : "Error de red al obtener perfil";
                    callback.onError(msg);
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
