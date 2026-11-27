package com.sergio.proyectov1.feedback;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sergio.proyectov1.api.VolleySingleton;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Calificación y reporte de recetas, guardados en la base de datos
 * (tablas calificacion y reporte_receta) mediante feedback_receta.php.
 * Un usuario tiene una sola calificación y un solo reporte por receta.
 */
public final class RecipeFeedbackStore {

    private static final String URL = VolleySingleton.BASE_URL + "feedback_receta.php";

    /** Estado de la receta para el usuario actual. */
    public static class Feedback {
        public final int myRating;      // 0 = sin calificar
        public final boolean reported;
        public final double average;
        public final int total;

        Feedback(int myRating, boolean reported, double average, int total) {
            this.myRating = myRating;
            this.reported = reported;
            this.average = average;
            this.total = total;
        }
    }

    public interface Callback {
        /** @param feedback puede ser null si no hubo conexión */
        void onResult(boolean success, @NonNull String message, @Nullable Feedback feedback);
    }

    private final Context context;

    public RecipeFeedbackStore(Context context) {
        this.context = context.getApplicationContext();
    }

    private int currentUserId() {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("user_id", 0);
    }

    /** Consulta la calificación propia, si ya la reportó y el promedio. */
    public void load(int recipeId, @NonNull Callback callback) {
        int userId = currentUserId();
        if (!valid(userId, recipeId, callback)) return;
        String url = URL + "?id_usuario=" + userId + "&id_receta=" + recipeId;
        send(Request.Method.GET, url, null, callback);
    }

    public void rate(int recipeId, int stars, @NonNull Callback callback) {
        int userId = currentUserId();
        if (!valid(userId, recipeId, callback)) return;
        Map<String, String> params = new HashMap<>();
        params.put("accion", "calificar");
        params.put("id_usuario", String.valueOf(userId));
        params.put("id_receta", String.valueOf(recipeId));
        params.put("calificacion", String.valueOf(Math.max(1, Math.min(5, stars))));
        send(Request.Method.POST, URL, params, callback);
    }

    public void report(int recipeId, @NonNull String reason, @NonNull Callback callback) {
        int userId = currentUserId();
        if (!valid(userId, recipeId, callback)) return;
        Map<String, String> params = new HashMap<>();
        params.put("accion", "reportar");
        params.put("id_usuario", String.valueOf(userId));
        params.put("id_receta", String.valueOf(recipeId));
        params.put("motivo", reason);
        send(Request.Method.POST, URL, params, callback);
    }

    private boolean valid(int userId, int recipeId, Callback callback) {
        if (userId <= 0) {
            callback.onResult(false, "Inicia sesión para calificar o reportar", null);
            return false;
        }
        if (recipeId <= 0) {
            callback.onResult(false, "Esta receta todavía no está publicada", null);
            return false;
        }
        return true;
    }

    private void send(int method, String url, @Nullable Map<String, String> params, Callback callback) {
        StringRequest request = new StringRequest(method, url,
                response -> deliver(response, callback),
                error -> deliverError(error, callback)) {
            @Override
            protected Map<String, String> getParams() {
                return params != null ? params : new HashMap<>();
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deliverError(VolleyError error, Callback callback) {
        // Los errores 4xx también traen JSON con el mensaje (ej. "Ya habías reportado esta receta")
        if (error.networkResponse != null && error.networkResponse.data != null) {
            deliver(new String(error.networkResponse.data, StandardCharsets.UTF_8), callback);
        } else {
            callback.onResult(false, "No hay conexión con el servidor", null);
        }
    }

    private void deliver(String body, Callback callback) {
        try {
            JSONObject json = new JSONObject(body == null ? "" : body.trim());
            Feedback feedback = json.has("calificacion")
                    ? new Feedback(
                            json.optInt("calificacion", 0),
                            json.optBoolean("reportado", false),
                            json.optDouble("promedio", 0),
                            json.optInt("total", 0))
                    : null;
            callback.onResult(json.optBoolean("success", false),
                    json.optString("message", ""), feedback);
        } catch (Exception e) {
            callback.onResult(false, "Respuesta no válida del servidor", null);
        }
    }
}
