package com.sergio.proyectov1.pantry;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sergio.proyectov1.api.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Alacena guardada en la base de datos (tabla alacena) mediante alacena.php.
 * El servidor valida que la unidad sea compatible con el ingrediente.
 */
public class PantryRepository {

    private static final String URL = VolleySingleton.BASE_URL + "alacena.php";

    public interface ListCallback {
        void onResult(@Nullable List<StockItem> items, @Nullable String error);
    }

    public interface ResultCallback {
        void onResult(boolean success, @NonNull String message);
    }

    private final Context context;

    public PantryRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private int userId() {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("user_id", 0);
    }

    public void load(@NonNull ListCallback callback) {
        if (userId() <= 0) {
            callback.onResult(null, "Inicia sesión para ver tu alacena");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.GET, URL + "?id_usuario=" + userId(),
                response -> {
                    try {
                        JSONArray array = new JSONArray(response.trim());
                        List<StockItem> items = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            StockItem item = new StockItem(o.getInt("id_ingrediente"), o.getString("nombre"),
                                    o.getDouble("cantidad"), o.getString("unidad"));
                            item.setPantryId(o.getInt("id_alacena"));
                            List<String> units = new ArrayList<>();
                            JSONArray u = o.optJSONArray("unidades");
                            if (u != null) for (int j = 0; j < u.length(); j++) units.add(u.getString(j));
                            item.setAllowedUnits(units);
                            items.add(item);
                        }
                        callback.onResult(items, null);
                    } catch (Exception e) {
                        callback.onResult(null, "Respuesta no válida del servidor");
                    }
                },
                error -> callback.onResult(null, messageFrom(error)));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void add(int ingredientId, double quantity, String unit, @NonNull ResultCallback callback) {
        Map<String, String> p = base("agregar");
        p.put("id_ingrediente", String.valueOf(ingredientId));
        p.put("cantidad", String.format(Locale.US, "%.2f", quantity));
        p.put("unidad", unit);
        post(p, callback);
    }

    public void update(int pantryId, int ingredientId, double quantity, String unit, @NonNull ResultCallback callback) {
        Map<String, String> p = base("actualizar");
        p.put("id_alacena", String.valueOf(pantryId));
        p.put("id_ingrediente", String.valueOf(ingredientId));
        p.put("cantidad", String.format(Locale.US, "%.2f", quantity));
        p.put("unidad", unit);
        post(p, callback);
    }

    public void delete(int pantryId, @NonNull ResultCallback callback) {
        Map<String, String> p = base("eliminar");
        p.put("id_alacena", String.valueOf(pantryId));
        post(p, callback);
    }

    private Map<String, String> base(String action) {
        Map<String, String> p = new HashMap<>();
        p.put("accion", action);
        p.put("id_usuario", String.valueOf(userId()));
        return p;
    }

    private void post(Map<String, String> params, ResultCallback callback) {
        if (userId() <= 0) {
            callback.onResult(false, "Inicia sesión para usar tu alacena");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> deliver(response, callback),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        deliver(new String(error.networkResponse.data, StandardCharsets.UTF_8), callback);
                    } else {
                        callback.onResult(false, "No hay conexión con el servidor");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deliver(String body, ResultCallback callback) {
        try {
            JSONObject json = new JSONObject(body == null ? "" : body.trim());
            callback.onResult(json.optBoolean("success", false), json.optString("message", ""));
        } catch (Exception e) {
            callback.onResult(false, "Respuesta no válida del servidor");
        }
    }

    private String messageFrom(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                JSONObject json = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                String m = json.optString("message", "");
                if (!m.isEmpty()) return m;
            } catch (Exception ignored) {}
        }
        return "No hay conexión con el servidor";
    }
}
