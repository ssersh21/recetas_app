package com.sergio.proyectov1.api.nutrition;

import com.sergio.proyectov1.models.FoodSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NutritionService {
    @GET("fdc/v1/foods/search")
    Call<FoodSearchResponse> searchFoods(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );
}
