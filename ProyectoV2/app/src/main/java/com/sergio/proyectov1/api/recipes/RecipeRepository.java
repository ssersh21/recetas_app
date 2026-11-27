package com.sergio.proyectov1.api.recipes;

import com.sergio.proyectov1.api.ingredients.IngredientApiClient;
import com.sergio.proyectov1.models.RecipeDto;

import java.util.List;

import retrofit2.Callback;

public class RecipeRepository {

    private final RecipeService service;

    public RecipeRepository() {
        service = IngredientApiClient.create(RecipeService.class);
    }

    public void buscarRecetas(String texto, Callback<List<RecipeDto>> callback) {
        service.getRecipes(texto).enqueue(callback);
    }
}
