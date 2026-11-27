package com.sergio.proyectov1.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sergio.proyectov1.api.ingredients.IngredientApiClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Medidas compatibles por alimento.
 *
 * Las reglas YA NO están en el código: se configuran en la base de datos
 * (tablas unidad_medida, ingrediente_unidad y especia_unidad) y llegan
 * en la respuesta de ingredientes.php / condimentos.php o desde unidades.php.
 * Esta clase solo consulta, guarda en caché y valida contra esa lista.
 */
public class FoodUnitHelper {

    public static final String TIPO_INGREDIENTE = "ingrediente";
    public static final String TIPO_ESPECIA = "especia";

    public static final String CONTEXTO_ALACENA = "alacena";
    public static final String CONTEXTO_RECETA = "receta";

    /** Mensaje cuando no se pudieron consultar las unidades (no se permite guardar). */
    public static final String UNITS_NOT_LOADED =
            "No se pudieron cargar las unidades de este ingrediente. Revisa tu conexión.";

    public interface UnitsCallback {
        /** Lista vacía = no se pudieron consultar (sin conexión); no hay lista de respaldo. */
        void onUnits(@NonNull List<String> units);
    }

    private static final Map<String, List<String>> cache = new HashMap<>();

    private FoodUnitHelper() {}

    private static String key(String tipo, String contexto, @Nullable String nombre) {
        String n = nombre == null ? "" : nombre.trim().toLowerCase(Locale.ROOT);
        return tipo + "|" + contexto + "|" + n;
    }

    /** Guarda en caché las medidas que vinieron junto con el ingrediente/condimento. */
    public static void remember(String tipo, @Nullable Ingredient ingredient) {
        if (ingredient == null || ingredient.getNombre() == null) return;
        if (!ingredient.getUnidadesAlacena().isEmpty()) {
            cache.put(key(tipo, CONTEXTO_ALACENA, ingredient.getNombre()), ingredient.getUnidadesAlacena());
        }
        if (!ingredient.getUnidadesReceta().isEmpty()) {
            cache.put(key(tipo, CONTEXTO_RECETA, ingredient.getNombre()), ingredient.getUnidadesReceta());
        }
    }

    /**
     * Obtiene las medidas compatibles de un alimento por su nombre.
     * Si ya están en caché responde de inmediato; si no, las pide a unidades.php.
     * Con nombre vacío devuelve las unidades por defecto configuradas en la BD.
     */
    public static void loadUnits(String tipo, String contexto, @Nullable String nombre,
                                 @NonNull UnitsCallback callback) {
        String k = key(tipo, contexto, nombre);
        List<String> cached = cache.get(k);
        if (cached != null) {
            callback.onUnits(cached);
            return;
        }

        String nombreConsulta = nombre == null ? "" : nombre.trim();
        IngredientApiClient.getService()
                .getUnits(tipo, nombreConsulta, contexto)
                .enqueue(new Callback<UnitsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UnitsResponse> call,
                                           @NonNull Response<UnitsResponse> response) {
                        UnitsResponse body = response.body();
                        if (response.isSuccessful() && body != null
                                && body.getUnidades() != null && !body.getUnidades().isEmpty()) {
                            cache.put(k, body.getUnidades());
                            callback.onUnits(body.getUnidades());
                        } else {
                            callback.onUnits(Collections.emptyList());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UnitsResponse> call, @NonNull Throwable t) {
                        callback.onUnits(Collections.emptyList());
                    }
                });
    }

    /**
     * Valida una unidad contra la lista de medidas permitidas del alimento.
     * Devuelve null si es válida o el mensaje de error.
     * Si la lista no se pudo cargar (null o vacía) NO se considera válida.
     */
    @Nullable
    public static String validateUnit(@Nullable String foodName, @Nullable String unit,
                                      @Nullable List<String> allowed) {
        if (foodName == null || foodName.trim().isEmpty()) {
            return null; // el error de "selecciona el ingrediente" lo muestra el formulario
        }
        if (allowed == null || allowed.isEmpty()) {
            return UNITS_NOT_LOADED;
        }
        if (unit == null || unit.trim().isEmpty()) {
            return "Selecciona la unidad";
        }
        if (!allowed.contains(unit)) {
            return "Para '" + foodName.trim() + "' no se permite '" + unit
                    + "'. Unidades válidas: " + String.join(", ", allowed);
        }
        return null;
    }
}
