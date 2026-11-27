package com.sergio.proyectov1.api.recipes;

import com.sergio.proyectov1.models.RecipeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("recetas.php")
    Call<List<RecipeDto>> getRecipes(
            @Query("buscar") String buscar
    );

    @GET("recetas.php")
    Call<List<RecipeDto>> getAdminRecipes(
            @Query("admin") int admin,
            @Query("buscar") String buscar
    );

    @GET("recetas.php")
    Call<List<RecipeDto>> getReportedRecipes(
            @Query("admin") int admin,
            @Query("min_reportes") int minReportes
    );
}
