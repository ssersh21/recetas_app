package com.sergio.proyectov1.api.ingredients;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente Retrofit compartido por todas las llamadas a recetas_api.
 * Cambia BASE_URL por la IP de tu computadora (o 10.0.2.2 en el emulador).
 */
public class IngredientApiClient {

    // Se define en local.properties (API_BASE_URL)
    private static final String BASE_URL = com.sergio.proyectov1.api.VolleySingleton.BASE_URL;

    private static Retrofit retrofit;

    private static Retrofit getRetrofit() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }

    public static IngredientService getService() {
        return getRetrofit().create(IngredientService.class);
    }

    public static <T> T create(Class<T> service) {
        return getRetrofit().create(service);
    }
}
