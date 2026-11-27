package com.sergio.proyectov1.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sergio.proyectov1.api.ingredients.IngredientApiClient;
import com.sergio.proyectov1.api.recipes.RecipeService;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.models.RecipeDto;
import com.sergio.proyectov1.models.RecipeMapper;
import com.sergio.proyectov1.recipes.RecipeDetailFragment;
import com.sergio.proyectov1.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAllRecipesFragment extends Fragment implements AdminRecipeAdapter.OnAdminActionListener {

    private RecyclerView rvAllRecipes;
    private EditText etSearch;
    private AdminRecipeAdapter adapter;
    private final List<Recipe> allRecipes = new ArrayList<>();

    public AdminAllRecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_all_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAllRecipes = view.findViewById(R.id.rv_admin_all_recipes);
        etSearch = view.findViewById(R.id.et_admin_search_recipe);

        rvAllRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminRecipeAdapter(this, false);
        rvAllRecipes.setAdapter(adapter);

        loadAllRecipes("");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadAllRecipes(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAllRecipes(String query) {
        RecipeService service = IngredientApiClient.create(RecipeService.class);
        service.getAdminRecipes(1, query).enqueue(new Callback<List<RecipeDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeDto>> call, @NonNull Response<List<RecipeDto>> response) {
                if (!isAdded()) return;
                allRecipes.clear();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    allRecipes.addAll(RecipeMapper.toRecipes(response.body()));
                }
                adapter.setRecipes(allRecipes);
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeDto>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                adapter.setRecipes(allRecipes);
            }
        });
    }

    @Override
    public void onDeleteClick(Recipe recipe) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Dar de baja receta")
                .setMessage("¿Estás seguro de dar de baja la receta '" + recipe.getTitle() + "' directamente?")
                .setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                    if (recipe.getId() > 0) {
                        AdminReportedRecipesFragment.BajaService service = IngredientApiClient.create(AdminReportedRecipesFragment.BajaService.class);
                        service.darDeBaja(recipe.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {}

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
                        });
                    }
                    allRecipes.remove(recipe);
                    adapter.removeRecipe(recipe);
                    Toast.makeText(getContext(), "Receta '" + recipe.getTitle() + "' dada de baja correctamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(recipe);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.admin_frame_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
