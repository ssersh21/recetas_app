package com.sergio.proyectov1.api.ingredients;

import com.sergio.proyectov1.models.Ingredient;
import com.sergio.proyectov1.models.UnitsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IngredientService {

    @GET("ingredientes.php")
    Call<List<Ingredient>> getIngredients(
            @Query("buscar") String buscar
    );

    @GET("condimentos.php")
    Call<List<Ingredient>> getCondiments(
            @Query("buscar") String buscar
    );

    /** Medidas compatibles de un alimento por nombre. tipo: ingrediente|especia, contexto: alacena|receta */
    @GET("unidades.php")
    Call<UnitsResponse> getUnits(
            @Query("tipo") String tipo,
            @Query("nombre") String nombre,
            @Query("contexto") String contexto
    );
}