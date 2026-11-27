package com.sergio.proyectov1.api.nutrition;

import com.sergio.proyectov1.models.FoodSearchResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class NutritionRepository {
    private final NutritionService service;
    // La clave se define en local.properties (USDA_API_KEY), no en el código
    private final String apiKey = com.sergio.proyectov1.BuildConfig.USDA_API_KEY;

    public NutritionRepository() {
        this.service = NutritionApiClient.getService();
    }

    public void searchFoods(String query, Callback<FoodSearchResponse> callback) {
        service.searchFoods(apiKey, query).enqueue(callback);
    }
}
