package com.sergio.proyectov1.home;

import com.sergio.proyectov1.adapters.CategoryAdapter;
import com.sergio.proyectov1.adapters.RecipeAdapter;
import com.sergio.proyectov1.models.Category;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.recipes.RecipeDetailFragment;
import com.sergio.proyectov1.viewmodels.RecipeViewModel;
import com.sergio.proyectov1.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private static final long SEARCH_DELAY_MS = 400;

    private RecyclerView rvMain, rvResults;
    private TextView tvEmpty;
    private TextInputEditText etSearch;
    private ChipGroup chipFilters;
    private RecipeViewModel recipeViewModel;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;
    private List<Recipe> lastLoaded = new ArrayList<>();
    private String currentQuery = "";

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMain = view.findViewById(R.id.rv_main_categories);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults = view.findViewById(R.id.rv_results);
        rvResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        tvEmpty = view.findViewById(R.id.tv_empty);
        etSearch = view.findViewById(R.id.et_search);
        chipFilters = view.findViewById(R.id.chip_filters);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        // Cuando llegan las recetas de la base de datos se muestran con los filtros activos
        recipeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            lastLoaded = recipes == null ? new ArrayList<>() : recipes;
            render();
        });

        recipeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                recipeViewModel.clearError();
            }
        });

        // La búsqueda espera a que el usuario deje de escribir antes de consultar al servidor
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (pendingSearch != null) handler.removeCallbacks(pendingSearch);
                pendingSearch = () -> search(s.toString().trim());
                handler.postDelayed(pendingSearch, SEARCH_DELAY_MS);
            }
        });
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (pendingSearch != null) handler.removeCallbacks(pendingSearch);
                search(v.getText() == null ? "" : v.getText().toString().trim());
                return true;
            }
            return false;
        });

        // Los chips filtran sobre lo ya cargado (no necesitan ir al servidor)
        for (int i = 0; i < chipFilters.getChildCount(); i++) {
            ((Chip) chipFilters.getChildAt(i)).setOnCheckedChangeListener((b, checked) -> render());
        }

        recipeViewModel.cargarRecetas("");
    }

    @Override
    public void onDestroyView() {
        if (pendingSearch != null) handler.removeCallbacks(pendingSearch);
        super.onDestroyView();
    }

    private void search(String query) {
        currentQuery = query;
        recipeViewModel.cargarRecetas(query);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recipeViewModel != null) {
            recipeViewModel.cargarRecetas(currentQuery);
        }
    }

    private boolean checked(int chipId) {
        Chip chip = chipFilters.findViewById(chipId);
        return chip != null && chip.isChecked();
    }

    /** Muestra secciones si no hay búsqueda ni filtros; si los hay, una cuadrícula con los resultados. */
    private void render() {
        if (!isAdded()) return;

        boolean quick = checked(R.id.chip_quick);
        boolean healthy = checked(R.id.chip_healthy);
        boolean beginner = checked(R.id.chip_beginner);
        boolean intermediate = checked(R.id.chip_intermediate);
        boolean advanced = checked(R.id.chip_advanced);
        boolean anyLevel = beginner || intermediate || advanced;
        boolean filtering = !currentQuery.isEmpty() || quick || healthy || anyLevel;

        if (!filtering) {
            List<Category> categories = buildCategories(lastLoaded);
            rvResults.setVisibility(View.GONE);
            rvMain.setVisibility(categories.isEmpty() ? View.GONE : View.VISIBLE);
            rvMain.setAdapter(new CategoryAdapter(categories, this));
            showEmpty(categories.isEmpty() ? "Aún no hay recetas publicadas" : null);
            return;
        }

        List<Recipe> results = new ArrayList<>();
        for (Recipe r : lastLoaded) {
            if (quick && !(r.getTimeMinutes() > 0 && r.getTimeMinutes() <= 30)) continue;
            if (healthy && r.getDietaryLabel().isEmpty()) continue;
            if (anyLevel) {
                String d = r.getDifficulty();
                boolean match = (beginner && "principiante".equals(d))
                        || (intermediate && "intermedio".equals(d))
                        || (advanced && "avanzado".equals(d));
                if (!match) continue;
            }
            results.add(r);
        }

        rvMain.setVisibility(View.GONE);
        rvResults.setVisibility(results.isEmpty() ? View.GONE : View.VISIBLE);
        rvResults.setAdapter(new RecipeAdapter(results, this));
        showEmpty(results.isEmpty() ? "No encontramos recetas con esa búsqueda o filtros" : null);
    }

    private void showEmpty(String message) {
        tvEmpty.setVisibility(message == null ? View.GONE : View.VISIBLE);
        if (message != null) tvEmpty.setText(message);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(recipe);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.frame_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * La base de datos no tiene categorías, así que las secciones del inicio
     * se arman con los datos de cada receta. Las secciones vacías se omiten.
     */
    private List<Category> buildCategories(List<Recipe> recipes) {
        List<Category> categories = new ArrayList<>();
        if (recipes == null || recipes.isEmpty()) return categories;

        // El servidor ya las entrega de la más reciente a la más antigua
        categories.add(new Category("Recién publicadas", new ArrayList<>(recipes)));

        List<Recipe> quick = new ArrayList<>();
        List<Recipe> beginner = new ArrayList<>();
        List<Recipe> rated = new ArrayList<>();

        for (Recipe recipe : recipes) {
            int minutes = recipe.getTimeMinutes();
            if (minutes > 0 && minutes <= 30) quick.add(recipe);
            if ("principiante".equals(recipe.getDifficulty())) beginner.add(recipe);
            if (recipe.getRatingCount() > 0) rated.add(recipe);
        }

        Collections.sort(rated, (a, b) ->
                Double.compare(b.getRatingAverage(), a.getRatingAverage()));

        if (!rated.isEmpty()) categories.add(new Category("Mejor calificadas", rated));
        if (!quick.isEmpty()) categories.add(new Category("Rápidas (30 min o menos)", quick));
        if (!beginner.isEmpty()) categories.add(new Category("Para principiantes", beginner));

        return categories;
    }
}
