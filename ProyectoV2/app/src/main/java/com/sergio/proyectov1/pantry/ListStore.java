package com.sergio.proyectov1.pantry;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Lista guardada en el teléfono, una por usuario y por nombre ("pantry" o "shopping").
 * Cuando el servidor tenga los endpoints de alacena y lista de compras, solo hay que
 * cambiar esta clase.
 */
public class ListStore {

    public static final String PANTRY = "pantry";
    public static final String SHOPPING = "shopping";

    private static final Type TYPE = new TypeToken<ArrayList<StockItem>>() {}.getType();

    private final SharedPreferences prefs;
    private final String key;
    private final Gson gson = new Gson();

    public ListStore(Context context, String name) {
        Context app = context.getApplicationContext();
        prefs = app.getSharedPreferences("ListPrefs", Context.MODE_PRIVATE);
        int userId = app.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("user_id", 0);
        key = name + "_" + userId;
    }

    public List<StockItem> load() {
        String json = prefs.getString(key, null);
        if (json == null) return new ArrayList<>();
        try {
            List<StockItem> list = gson.fromJson(json, TYPE);
            return list == null ? new ArrayList<>() : list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void save(List<StockItem> items) {
        prefs.edit().putString(key, gson.toJson(items)).apply();
    }

    /** Agrega el ingrediente; si ya existe con la misma unidad, suma la cantidad. */
    public void add(StockItem item) {
        List<StockItem> items = load();
        for (StockItem existing : items) {
            if (existing.sameAs(item)) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                save(items);
                return;
            }
        }
        items.add(item);
        save(items);
    }
}
