package com.sergio.proyectov1.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AdminReportedRecipesFragment extends Fragment implements AdminRecipeAdapter.OnAdminActionListener {

    private RecyclerView rvReported;
    private TextView tvNoReported;
    private AdminRecipeAdapter adapter;
    private final List<Recipe> reportedRecipes = new ArrayList<>();

    interface BajaService {
        @FormUrlEncoded
        @POST("baja_receta.php")
        Call<Void> darDeBaja(@Field("id_receta") int idReceta);
    }

    public AdminReportedRecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_reported_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvReported = view.findViewById(R.id.rv_reported_recipes);
        tvNoReported = view.findViewById(R.id.tv_no_reported_recipes);

        rvReported.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminRecipeAdapter(this, true);
        rvReported.setAdapter(adapter);

        loadReportedRecipes();
    }

    private void loadReportedRecipes() {
        RecipeService service = IngredientApiClient.create(RecipeService.class);
        service.getReportedRecipes(1, 5).enqueue(new Callback<List<RecipeDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeDto>> call, @NonNull Response<List<RecipeDto>> response) {
                if (!isAdded()) return;
                reportedRecipes.clear();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    reportedRecipes.addAll(RecipeMapper.toRecipes(response.body()));
                }

                if (reportedRecipes.isEmpty()) {
                    tvNoReported.setVisibility(View.VISIBLE);
                    rvReported.setVisibility(View.GONE);
                } else {
                    tvNoReported.setVisibility(View.GONE);
                    rvReported.setVisibility(View.VISIBLE);
                    adapter.setRecipes(reportedRecipes);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeDto>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                if (reportedRecipes.isEmpty()) {
                    tvNoReported.setVisibility(View.VISIBLE);
                    rvReported.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDeleteClick(Recipe recipe) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Dar de baja receta")
                .setMessage("¿Estás seguro de dar de baja la receta '" + recipe.getTitle() + "' del sistema?")
                .setPositiveButton("Sí, dar de baja", (dialog, which) -> {
                    if (recipe.getId() > 0) {
                        BajaService service = IngredientApiClient.create(BajaService.class);
                        service.darDeBaja(recipe.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {}

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
                        });
                    }
                    adapter.removeRecipe(recipe);
                    Toast.makeText(getContext(), "Receta '" + recipe.getTitle() + "' dada de baja correctamente", Toast.LENGTH_SHORT).show();

                    if (adapter.getItemCount() == 0) {
                        tvNoReported.setVisibility(View.VISIBLE);
                        rvReported.setVisibility(View.GONE);
                    }
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
