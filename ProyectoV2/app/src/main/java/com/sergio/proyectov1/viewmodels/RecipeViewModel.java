package com.sergio.proyectov1.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sergio.proyectov1.api.recipes.RecipeRepository;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.models.RecipeDto;
import com.sergio.proyectov1.models.RecipeMapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository repository = new RecipeRepository();

    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void clearError() {
        error.setValue(null);
    }

    /** @param texto texto a buscar en el nombre; "" trae todas. */
    public void cargarRecetas(String texto) {

        repository.buscarRecetas(texto, new Callback<List<RecipeDto>>() {

            @Override
            public void onResponse(Call<List<RecipeDto>> call,
                                   Response<List<RecipeDto>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    recipes.setValue(RecipeMapper.toRecipes(response.body()));
                } else {
                    error.setValue("No se pudieron obtener las recetas");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeDto>> call, Throwable t) {
                error.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
