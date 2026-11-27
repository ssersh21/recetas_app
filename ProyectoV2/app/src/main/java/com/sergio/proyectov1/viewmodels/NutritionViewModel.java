package com.sergio.proyectov1.viewmodels;

import com.sergio.proyectov1.api.nutrition.NutritionRepository;
import com.sergio.proyectov1.models.FoodItem;
import com.sergio.proyectov1.models.FoodNutrient;
import com.sergio.proyectov1.models.FoodSearchResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutritionViewModel extends ViewModel {
    private final NutritionRepository repository;
    private final MutableLiveData<List<FoodItem>> foodItems = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<RecipeNutrition> totalNutrition = new MutableLiveData<>();

    public static class RecipeNutrition {
        public double calories, protein, fats, carbs;
    }

    public static class IngredientData {
        public String name;
        public double quantity; // in grams or ml
        public boolean isToTaste;

        public IngredientData(String name, double quantity, boolean isToTaste) {
            this.name = name;
            this.quantity = quantity;
            this.isToTaste = isToTaste;
        }
    }

    public NutritionViewModel() {
        this.repository = new NutritionRepository();
    }

    public LiveData<RecipeNutrition> getTotalNutrition() {
        return totalNutrition;
    }

    public void calculateTotalNutrition(List<IngredientData> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return;

        isLoading.setValue(true);
        RecipeNutrition summary = new RecipeNutrition();
        final int[] completedRequests = {0};
        final int totalRequests = ingredients.size();

        for (IngredientData ing : ingredients) {
            // If it's "al gusto", we skip it or assign a small value. 
            // The user said: "o no considerarlo en el calculo". Let's skip it if quantity is 0 or isToTaste is true.
            if (ing.isToTaste || ing.quantity <= 0) {
                checkCompletion(completedRequests, totalRequests, summary);
                continue;
            }

            repository.searchFoods(ing.name, new Callback<FoodSearchResponse>() {
                @Override
                public void onResponse(Call<FoodSearchResponse> call, Response<FoodSearchResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getFoods().isEmpty()) {
                        FoodItem item = response.body().getFoods().get(0);
                        // Standardize: USDA often returns per 100g. 
                        double multiplier = ing.quantity / 100.0;
                        
                        for (FoodNutrient n : item.getFoodNutrients()) {
                            String nutrientName = n.getNutrientName().toLowerCase();
                            if (nutrientName.contains("energy")) summary.calories += n.getValue() * multiplier;
                            else if (nutrientName.contains("protein")) summary.protein += n.getValue() * multiplier;
                            else if (nutrientName.contains("total lipid")) summary.fats += n.getValue() * multiplier;
                            else if (nutrientName.contains("carbohydrate")) summary.carbs += n.getValue() * multiplier;
                        }
                    }
                    checkCompletion(completedRequests, totalRequests, summary);
                }

                @Override
                public void onFailure(Call<FoodSearchResponse> call, Throwable t) {
                    checkCompletion(completedRequests, totalRequests, summary);
                }
            });
        }
    }

    private void checkCompletion(int[] completedRequests, int totalRequests, RecipeNutrition summary) {
        completedRequests[0]++;
        if (completedRequests[0] == totalRequests) {
            isLoading.setValue(false);
            totalNutrition.setValue(summary);
        }
    }

    public LiveData<List<FoodItem>> getFoodItems() {
        return foodItems;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchFood(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }

        isLoading.setValue(true);
        repository.searchFoods(query, new Callback<FoodSearchResponse>() {
            @Override
            public void onResponse(Call<FoodSearchResponse> call, Response<FoodSearchResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    foodItems.setValue(response.body().getFoods());
                } else {
                    error.setValue("Error al buscar alimentos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FoodSearchResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Error de red: " + t.getMessage());
            }
        });
    }
}
