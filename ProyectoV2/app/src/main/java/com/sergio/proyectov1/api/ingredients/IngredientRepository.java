package com.sergio.proyectov1.api.ingredients;

import com.sergio.proyectov1.models.Ingredient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class IngredientRepository {

    private final IngredientService service;

    public IngredientRepository() {
        service = IngredientApiClient.getService();
    }

    public void buscarIngredientes(
            String texto,
            Callback<List<Ingredient>> callback
    ) {

        service.getIngredients(texto).enqueue(callback);
    }

    public void buscarCondimentos(
            String texto,
            Callback<List<Ingredient>> callback
    ) {

        service.getCondiments(texto).enqueue(callback);
    }
}