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

import android.widget.LinearLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sergio.proyectov1.api.ApiHelper;
import com.sergio.proyectov1.feedback.RecipeFeedbackStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            ApiHelper.loadImage(ivImage, recipe.getImageUrl(), recipe.getImageRes());
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
        if (recipe != null) {
            showAuthorTagsAndUtensils(view);
            setupComments(view);
            setupOwnerMenu(toolbar);
        }

        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    // ============================================================
    // Autor, etiquetas y utensilios
    // ============================================================

    private void showAuthorTagsAndUtensils(View view) {
        TextView tvAuthor = view.findViewById(R.id.tv_detail_author);
        if (recipe.getAuthor() != null && !recipe.getAuthor().isEmpty()) {
            tvAuthor.setText("Por " + recipe.getAuthor());
            tvAuthor.setVisibility(View.VISIBLE);
        }

        ChipGroup chips = view.findViewById(R.id.chip_detail_tags);
        chips.removeAllViews();
        for (String tag : recipe.getTags()) {
            Chip chip = new Chip(requireContext());
            chip.setText(tag);
            chip.setClickable(false);
            chips.addView(chip);
        }
        chips.setVisibility(recipe.getTags().isEmpty() ? View.GONE : View.VISIBLE);

        if (!recipe.getUtensils().isEmpty()) {
            TextView title = view.findViewById(R.id.tv_detail_utensils_title);
            TextView list = view.findViewById(R.id.tv_detail_utensils);
            StringBuilder sb = new StringBuilder();
            for (String u : recipe.getUtensils()) {
                if (sb.length() > 0) sb.append("\n");
                sb.append("• ").append(u);
            }
            list.setText(sb);
            title.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }

    // ============================================================
    // Editar / eliminar (solo el autor)
    // ============================================================

    private void setupOwnerMenu(Toolbar toolbar) {
        int me = ApiHelper.userId(requireContext());
        if (recipe.getId() <= 0 || me <= 0 || recipe.getOwnerId() != me) return;

        toolbar.inflateMenu(R.menu.recipe_owner_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_recipe) {
                if (recipe.getSource() == null) {
                    Toast.makeText(requireContext(), "Vuelve a abrir la receta desde el inicio para editarla",
                            Toast.LENGTH_LONG).show();
                    return true;
                }
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, CreateRecipeFragment.newInstanceForEdit(recipe.getSource()))
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            if (item.getItemId() == R.id.action_delete_recipe) {
                confirmDelete();
                return true;
            }
            return false;
        });
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Eliminar \"" + recipe.getTitle() + "\"?")
                .setMessage("También se borrarán sus comentarios, calificaciones y su lugar en los planes. "
                        + "Esta acción no se puede deshacer.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (d, w) -> {
                    Map<String, String> p = new HashMap<>();
                    p.put("id_usuario", String.valueOf(ApiHelper.userId(requireContext())));
                    p.put("id_receta", String.valueOf(recipe.getId()));
                    ApiHelper.post(requireContext(), "eliminar_receta.php", p, (ok, message, body) -> {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        if (ok) getParentFragmentManager().popBackStack();
                    });
                })
                .show();
    }

    // ============================================================
    // Comentarios (tabla comentario)
    // ============================================================

    private LinearLayout commentsContainer;
    private TextView tvCommentsEmpty;

    private void setupComments(View view) {
        commentsContainer = view.findViewById(R.id.container_comments);
        tvCommentsEmpty = view.findViewById(R.id.tv_detail_comments_empty);
        TextInputLayout til = view.findViewById(R.id.til_comment);
        TextInputEditText et = view.findViewById(R.id.et_comment);
        ImageButton btnSend = view.findViewById(R.id.btn_send_comment);

        if (recipe.getId() <= 0) {
            til.setEnabled(false);
            btnSend.setEnabled(false);
            tvCommentsEmpty.setText("Esta receta todavía no está publicada");
            return;
        }

        btnSend.setOnClickListener(v -> {
            String text = et.getText() == null ? "" : et.getText().toString().trim();
            if (text.isEmpty()) {
                til.setError("Escribe un comentario");
                return;
            }
            til.setError(null);
            btnSend.setEnabled(false);
            Map<String, String> p = new HashMap<>();
            p.put("accion", "crear");
            p.put("id_usuario", String.valueOf(ApiHelper.userId(requireContext())));
            p.put("id_receta", String.valueOf(recipe.getId()));
            p.put("comentario", text);
            ApiHelper.post(requireContext(), "comentarios.php", p, (ok, message, body) -> {
                if (!isAdded()) return;
                btnSend.setEnabled(true);
                if (ok) {
                    et.setText("");
                    loadComments();
                } else {
                    til.setError(message);
                }
            });
        });

        loadComments();
    }

    private void loadComments() {
        ApiHelper.get(requireContext(), "comentarios.php?id_receta=" + recipe.getId(), (ok, message, body) -> {
            if (!isAdded()) return;
            commentsContainer.removeAllViews();
            if (!ok || body == null) {
                tvCommentsEmpty.setText(message);
                tvCommentsEmpty.setVisibility(View.VISIBLE);
                return;
            }
            try {
                JSONArray array = new JSONArray(body);
                int me = ApiHelper.userId(requireContext());
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject c = array.getJSONObject(i);
                    View row = inflater.inflate(R.layout.item_comment, commentsContainer, false);
                    ((TextView) row.findViewById(R.id.tv_comment_author)).setText(c.optString("autor"));
                    ((TextView) row.findViewById(R.id.tv_comment_text)).setText(c.optString("comentario"));
                    ((TextView) row.findViewById(R.id.tv_comment_date)).setText(prettyDate(c.optString("fecha")));
                    if (c.optInt("id_usuario") == me) {
                        View btnDelete = row.findViewById(R.id.btn_delete_comment);
                        btnDelete.setVisibility(View.VISIBLE);
                        int commentId = c.optInt("id_comentario");
                        btnDelete.setOnClickListener(v -> deleteComment(commentId));
                    }
                    commentsContainer.addView(row);
                }
                tvCommentsEmpty.setText("Sé el primero en comentar");
                tvCommentsEmpty.setVisibility(array.length() == 0 ? View.VISIBLE : View.GONE);
            } catch (Exception e) {
                tvCommentsEmpty.setText("No se pudieron cargar los comentarios");
                tvCommentsEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void deleteComment(int commentId) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Borrar tu comentario?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Borrar", (d, w) -> {
                    Map<String, String> p = new HashMap<>();
                    p.put("accion", "eliminar");
                    p.put("id_usuario", String.valueOf(ApiHelper.userId(requireContext())));
                    p.put("id_comentario", String.valueOf(commentId));
                    ApiHelper.post(requireContext(), "comentarios.php", p, (ok, message, body) -> {
                        if (!isAdded()) return;
                        if (!ok) Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        loadComments();
                    });
                })
                .show();
    }

    /** "2026-11-27 09:25:51" -> "27/11/2026" */
    private static String prettyDate(String sqlDate) {
        if (sqlDate == null || sqlDate.length() < 10) return "";
        String[] p = sqlDate.substring(0, 10).split("-");
        return p.length == 3 ? p[2] + "/" + p[1] + "/" + p[0] : sqlDate;
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
