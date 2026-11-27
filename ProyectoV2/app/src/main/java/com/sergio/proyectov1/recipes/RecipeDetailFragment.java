package com.sergio.proyectov1.recipes;

import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.viewmodels.NutritionViewModel;
import com.sergio.proyectov1.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sergio.proyectov1.feedback.RecipeFeedbackStore;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import com.sergio.proyectov1.models.FoodItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private Recipe recipe;
    private NutritionViewModel nutritionViewModel;
    private TextView tvMacros;

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivImage = view.findViewById(R.id.iv_detail_image);
        TextView tvTitle = view.findViewById(R.id.tv_detail_title);
        TextView tvTime = view.findViewById(R.id.tv_detail_time);
        TextView tvPortions = view.findViewById(R.id.tv_detail_portions);
        TextView tvIngredients = view.findViewById(R.id.tv_detail_ingredients);
        TextView tvSteps = view.findViewById(R.id.tv_detail_steps);
        tvMacros = view.findViewById(R.id.tv_detail_macros);
        Toolbar toolbar = view.findViewById(R.id.detail_toolbar);

        nutritionViewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        if (recipe != null) {
            ivImage.setImageResource(recipe.getImageRes());
            tvTitle.setText(recipe.getTitle());
            tvTime.setText(recipe.getTime());
            tvPortions.setText(recipe.getPortions());
            tvIngredients.setText(recipe.getIngredients());
            tvSteps.setText(recipe.getSteps());
            
            if (recipe.getCalories() > 0) {
                displayMacros();
            } else {
                tvMacros.setText("Calculando información nutricional...");
                calculateMacrosForRecipe();
            }
        }

        nutritionViewModel.getTotalNutrition().observe(getViewLifecycleOwner(), nutrition -> {
            recipe.setCalories(nutrition.calories);
            recipe.setProtein(nutrition.protein);
            recipe.setFats(nutrition.fats);
            recipe.setCarbs(nutrition.carbs);
            displayMacros();
        });

        setupFeedback(view);

        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    // ============================================================
    // Calificación y reporte (se guardan en la BD)
    // ============================================================

    private static final String[] REPORT_REASONS = {
            "Contenido inapropiado",
            "Imagen inapropiada",
            "Receta incorrecta o peligrosa",
            "Spam o publicidad",
            "Otro"
    };

    private RecipeFeedbackStore feedbackStore;
    private RatingBar rbRating;
    private TextView tvRatingLabel;
    private ImageButton btnReport;

    private void setupFeedback(View view) {
        rbRating = view.findViewById(R.id.rb_detail_rating);
        tvRatingLabel = view.findViewById(R.id.tv_detail_rating_label);
        btnReport = view.findViewById(R.id.btn_detail_report);
        if (recipe == null || rbRating == null || btnReport == null) return;

        feedbackStore = new RecipeFeedbackStore(requireContext());

        if (recipe.getId() <= 0) {
            rbRating.setIsIndicator(true);
            btnReport.setEnabled(false);
            return;
        }

        showAverage(recipe.getRatingAverage(), recipe.getRatingCount());
        feedbackStore.load(recipe.getId(), (ok, message, feedback) -> {
            if (!isAdded()) return;
            if (feedback != null) applyFeedback(feedback);
        });

        rbRating.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (!fromUser || rating < 1) return;
            feedbackStore.rate(recipe.getId(), Math.round(rating), (ok, message, feedback) -> {
                if (!isAdded()) return;
                if (feedback != null) applyFeedback(feedback);
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            });
        });

        btnReport.setOnClickListener(v -> showReportDialog());
    }

    private void applyFeedback(RecipeFeedbackStore.Feedback feedback) {
        if (feedback.myRating > 0) rbRating.setRating(feedback.myRating);
        recipe.setRating(feedback.average, feedback.total);
        showAverage(feedback.average, feedback.total);
        if (feedback.reported) {
            btnReport.setEnabled(false);
            btnReport.setAlpha(0.4f);
            btnReport.setContentDescription("Ya reportaste esta receta");
        }
    }

    private void showAverage(double average, int total) {
        if (tvRatingLabel == null) return;
        tvRatingLabel.setText(total > 0
                ? String.format(Locale.getDefault(), "Valoración: %.1f (%d %s)",
                        average, total, total == 1 ? "voto" : "votos")
                : "Valoración de la receta (sin votos)");
    }

    private void showReportDialog() {
        final int[] selected = {-1};
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Por qué reportas esta receta?")
                .setSingleChoiceItems(REPORT_REASONS, -1, (d, which) -> {
                    selected[0] = which;
                    ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                })
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Reportar", (d, w) -> {
                    if (selected[0] < 0) return;
                    feedbackStore.report(recipe.getId(), REPORT_REASONS[selected[0]], (ok, message, feedback) -> {
                        if (!isAdded()) return;
                        if (feedback != null) applyFeedback(feedback);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    });
                })
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false));
        dialog.show();
    }

    private void displayMacros() {
        if (recipe == null) return;
        
        int portions = recipe.getPortionsCount();
        double calPerPortion = recipe.getCalories() / portions;
        double protPerPortion = recipe.getProtein() / portions;
        double fatsPerPortion = recipe.getFats() / portions;
        double carbsPerPortion = recipe.getCarbs() / portions;

        String summary = String.format("Valores por Porción (%d porciones):\nCalorías: %.1f kcal | Prot: %.1f g | Grasas: %.1f g | Carb: %.1f g",
                portions, calPerPortion, protPerPortion, fatsPerPortion, carbsPerPortion);
        
        String label = recipe.getDietaryLabel(); // Note: DietaryLabel might need adjustment for per-portion, but usually it's per recipe or per 100g. 
        // Let's keep the label as is or adjust it later.
        if (!label.isEmpty()) {
            summary += "\nEtiquetas: " + label;
        }
        tvMacros.setText(summary);
    }

    private void calculateMacrosForRecipe() {
        String ingredients = recipe.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) return;

        List<NutritionViewModel.IngredientData> ingredientDataList = new ArrayList<>();
        String[] lines = ingredients.split("\n");
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            // Expected format: "- QTY UNIT de NAME"
            try {
                String content = line.substring(2); // Remove "- "
                // Límite 2: "Pechuga de pollo" no debe cortarse en el segundo " de "
                String[] parts = content.split(" de ", 2);
                if (parts.length < 2) continue;
                
                String name = parts[1].trim();
                // Limpiar el sufijo del tipo de ingrediente si existe, ej: "Pollo (Ingrediente Principal)" -> "Pollo"
                name = name.replaceAll("\\s*\\(.*\\)", "").trim();
                String qtyPart = parts[0].trim();
                
                double grams = 0;
                boolean isToTaste = false;
                
                if (qtyPart.toLowerCase().contains("al gusto")) {
                    isToTaste = true;
                } else {
                    // Extract numeric part and unit
                    String numericStr = qtyPart.replaceAll("[^0-9.]", "");
                    if (!numericStr.isEmpty()) {
                        grams = Double.parseDouble(numericStr);
                        
                        // Basic conversion to grams
                        String unit = qtyPart.toLowerCase();
                        if (unit.contains("kg") || unit.contains("kilogramo")) grams *= 1000;
                        else if (unit.contains("taza")) grams *= 240;
                        else if (unit.contains("cucharada")) grams *= 15;
                        else if (unit.contains("cucharadita")) grams *= 5;
                        else if (unit.contains("litro")) grams *= 1000; // "ml" se queda igual (1 ml ≈ 1 g)
                        // grams (gr) or piezas (assume 100g each if not specified better)
                        else if (unit.contains("pieza")) grams *= 100; 
                    }
                }
                
                ingredientDataList.add(new NutritionViewModel.IngredientData(name, grams, isToTaste));
                
            } catch (Exception e) {
                // Skip problematic lines
            }
        }

        if (!ingredientDataList.isEmpty()) {
            nutritionViewModel.calculateTotalNutrition(ingredientDataList);
        } else {
            tvMacros.setText("Información nutricional no disponible");
        }
    }
}
