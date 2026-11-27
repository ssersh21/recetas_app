package com.sergio.proyectov1.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sergio.proyectov1.api.ingredients.IngredientRepository;
import com.sergio.proyectov1.models.Ingredient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientViewModel extends ViewModel {

    private final IngredientRepository repository;

    private final MutableLiveData<List<Ingredient>> ingredients =
            new MutableLiveData<>();

    private final MutableLiveData<List<Ingredient>> condiments =
            new MutableLiveData<>();

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public IngredientViewModel() {
        repository = new IngredientRepository();
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public LiveData<List<Ingredient>> getCondiments() {
        return condiments;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void clearError() {
        error.setValue(null);
    }

    public void buscarIngredientes(String texto) {

        repository.buscarIngredientes(
                texto,
                new Callback<List<Ingredient>>() {

                    @Override
                    public void onResponse(
                            Call<List<Ingredient>> call,
                            Response<List<Ingredient>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            ingredients.setValue(response.body());

                        } else {

                            error.setValue(
                                    "No se pudieron obtener los ingredientes"
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Ingredient>> call,
                            Throwable t
                    ) {

                        error.setValue(
                                "Error de conexión: " + t.getMessage()
                        );
                    }
                }
        );
    }

    public void buscarCondimentos(String texto) {

        repository.buscarCondimentos(
                texto,
                new Callback<List<Ingredient>>() {

                    @Override
                    public void onResponse(
                            Call<List<Ingredient>> call,
                            Response<List<Ingredient>> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            condiments.setValue(response.body());

                        } else {

                            error.setValue(
                                    "No se pudieron obtener los condimentos"
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Ingredient>> call,
                            Throwable t
                    ) {

                        error.setValue(
                                "Error de conexión: " + t.getMessage()
                        );
                    }
                }
        );
    }
}