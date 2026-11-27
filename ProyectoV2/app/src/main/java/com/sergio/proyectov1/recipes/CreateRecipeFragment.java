package com.sergio.proyectov1.recipes;

import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.models.RecipeDto;
import com.sergio.proyectov1.models.RecipeMapper;
import com.sergio.proyectov1.api.ApiHelper;
import androidx.annotation.Nullable;
import com.sergio.proyectov1.viewmodels.NutritionViewModel;
import com.sergio.proyectov1.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sergio.proyectov1.adapters.IngredientAdapter;
import com.sergio.proyectov1.difficulty.DifficultyCalculator;
import java.util.ArrayList;
import java.util.List;
import com.sergio.proyectov1.models.FoodUnitHelper;
import com.sergio.proyectov1.models.Ingredient;
import com.sergio.proyectov1.viewmodels.IngredientViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Crear una receta nueva o, con {@link #newInstanceForEdit}, editar una receta propia.
 * Al guardar: calcula la nutrición por porción (USDA), sube la imagen si se eligió
 * una nueva y manda todo a guardar_receta.php / editar_receta.php.
 */
public class CreateRecipeFragment extends Fragment {

    private static final String ARG_EDIT = "receta_a_editar";

    /** Abre el formulario lleno con una receta propia para editarla. */
    public static CreateRecipeFragment newInstanceForEdit(RecipeDto recipe) {
        CreateRecipeFragment f = new CreateRecipeFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_EDIT, recipe);
        f.setArguments(b);
        return f;
    }

    /** Receta que se está editando (null = receta nueva). */
    @Nullable
    private RecipeDto editing;

    // Imagen: la nueva que eligió el usuario o la que ya tenía la receta en el servidor
    @Nullable
    private android.net.Uri selectedImageUri;
    @Nullable
    private String existingImagePath;

    // Utensilios elegidos del catálogo (id -> nombre)
    private final java.util.LinkedHashMap<Integer, String> selectedUtensils = new java.util.LinkedHashMap<>();

    private EditText etTags;
    private boolean saving = false;
    /** Datos listos para enviar mientras se calcula la nutrición. */
    @Nullable
    private JSONObject pendingRequest;

    private EditText etName, etPortions, etTime, etUtensils;
    private LinearLayout containerIngredients, containerCondiments, containerSteps;
    private Button btnAddIngredient, btnAddCondiment, btnAddStep, btnSave, btnSelectImage;
    private ImageView ivRecipeImage;

    private NutritionViewModel nutritionViewModel;
    private IngredientViewModel ingredientViewModel;

    private int stepCount = 0;

    private Toast currentToast;

    private void showToast(String message) {
        if (getContext() == null || message == null) return;
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        currentToast.show();
    }

    // Registrar el selector de fotos
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ivRecipeImage.setImageURI(uri);
                } else {
                    showToast("No se seleccionó ninguna imagen");
                }
            });

    public CreateRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        // Inicializar vistas básicas
        etName = view.findViewById(R.id.et_recipe_name);
        etPortions = view.findViewById(R.id.et_portions);
        etTime = view.findViewById(R.id.et_time);
        etUtensils = view.findViewById(R.id.et_utensils);

        containerIngredients = view.findViewById(R.id.container_ingredients);
        containerCondiments = view.findViewById(R.id.container_condiments);
        containerSteps = view.findViewById(R.id.container_steps);

        btnAddIngredient = view.findViewById(R.id.btn_add_ingredient);
        btnAddCondiment = view.findViewById(R.id.btn_add_condiment);
        btnAddStep = view.findViewById(R.id.btn_add_step);
        btnSave = view.findViewById(R.id.btn_save);
        btnSelectImage = view.findViewById(R.id.btn_select_image);
        ivRecipeImage = view.findViewById(R.id.iv_recipe_image);

        // Inicializar ViewModel
        nutritionViewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        // Listener para el Photo Picker
        btnSelectImage.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        ingredientViewModel =
                new ViewModelProvider(this)
                        .get(IngredientViewModel.class);
        ingredientViewModel.getError()
                .observe(
                        getViewLifecycleOwner(),
                        error -> {
                            if (error != null && !error.isEmpty()) {
                                showToast(error);
                                ingredientViewModel.clearError();
                            }
                        }
                );

        // Listeners para añadir filas
        btnAddIngredient.setOnClickListener(v -> addIngredientRow());
        if (btnAddCondiment != null) {
            btnAddCondiment.setOnClickListener(v -> addCondimentRow());
        }
        btnAddStep.setOnClickListener(v -> addStepRow());

        btnSave.setOnClickListener(v -> saveRecipe());

        etTags = view.findViewById(R.id.et_tags);
        etUtensils.setOnClickListener(v -> showUtensilSelector());

        // Cuando termina el cálculo nutricional se envía la receta
        nutritionViewModel.getTotalNutrition().observe(getViewLifecycleOwner(), nutrition -> {
            if (pendingRequest == null || nutrition == null) return;
            JSONObject request = pendingRequest;
            pendingRequest = null;
            putNutrition(request, nutrition);
            uploadImageThenSend(request);
        });

        if (getArguments() != null) {
            editing = (RecipeDto) getArguments().getSerializable(ARG_EDIT);
        }

        if (editing != null) {
            fillForEdit(editing);
        } else {
            // Añadir una fila inicial por defecto
            addIngredientRow();
            addCondimentRow();
            addStepRow();
        }

        return view;
    }

    // ============================================================
    // Edición: llenar el formulario con la receta existente
    // ============================================================

    private void fillForEdit(RecipeDto r) {
        btnSave.setText("Guardar cambios");
        etName.setText(r.nombre);
        etPortions.setText(String.valueOf(r.porciones));
        etTime.setText(String.valueOf(r.tiempo_estimado));

        existingImagePath = r.imagen == null || r.imagen.isEmpty() ? null : r.imagen;
        ApiHelper.loadImage(ivRecipeImage, existingImagePath, android.R.drawable.ic_menu_gallery);

        if (r.ingredientes != null) {
            for (RecipeDto.Item item : r.ingredientes) {
                View row = addIngredientRow();
                Ingredient ing = new Ingredient(item.id_ingrediente, item.nombre);
                row.setTag(ing);
                ((TextView) row.findViewById(R.id.tv_selected_ingredient)).setText(item.nombre);
                ((EditText) row.findViewById(R.id.et_ingredient_quantity)).setText(RecipeMapper.formatQuantity(item.cantidad));
                Spinner type = row.findViewById(R.id.spinner_ingredient_type);
                if (type != null) type.setSelection("complementario".equals(item.tipo) ? 1 : 0);
                loadUnitsForRow(row, R.id.spinner_unit, ing, FoodUnitHelper.TIPO_INGREDIENTE, item.unidad);
            }
        }
        if (r.condimentos != null) {
            for (RecipeDto.Item item : r.condimentos) {
                View row = addCondimentRow();
                if (row == null) break;
                Ingredient cond = new Ingredient(item.id_especia, item.nombre);
                cond.setId_condimento(item.id_especia);
                row.setTag(cond);
                ((TextView) row.findViewById(R.id.tv_selected_condiment)).setText(item.nombre);
                ((EditText) row.findViewById(R.id.et_condiment_quantity)).setText(RecipeMapper.formatQuantity(item.cantidad));
                loadUnitsForRow(row, R.id.spinner_condiment_unit, cond, FoodUnitHelper.TIPO_ESPECIA, item.unidad);
            }
        }
        if (r.pasos != null) {
            for (RecipeDto.Paso paso : r.pasos) {
                View row = addStepRow();
                ((EditText) row.findViewById(R.id.et_step_description)).setText(paso.descripcion);
            }
        }
        if (containerIngredients.getChildCount() == 0) addIngredientRow();
        if (containerSteps.getChildCount() == 0) addStepRow();

        if (r.utensilios != null) {
            for (RecipeDto.Utensilio u : r.utensilios) selectedUtensils.put(u.id_utensilio, u.nombre);
        }
        showSelectedUtensils();

        // Solo las etiquetas que puso el usuario; las automáticas las vuelve a calcular el servidor
        StringBuilder tags = new StringBuilder();
        if (r.etiquetas != null) {
            for (RecipeDto.Etiqueta e : r.etiquetas) {
                if (e.automatica) continue;
                if (tags.length() > 0) tags.append(", ");
                tags.append(e.nombre);
            }
        }
        etTags.setText(tags);
    }

    /** Consulta en la BD las medidas del alimento y las pone en el spinner de la fila. */
    private void loadUnitsForRow(View row, int spinnerId, Ingredient food, String tipo, String selectedUnit) {
        FoodUnitHelper.loadUnits(tipo, FoodUnitHelper.CONTEXTO_RECETA, food.getNombre(), units -> {
            if (!isAdded()) return;
            food.setUnidadesReceta(units);
            Spinner spinner = row.findViewById(spinnerId);
            if (spinner == null || units.isEmpty()) {
                showToast(FoodUnitHelper.UNITS_NOT_LOADED);
                return;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, units);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            int index = units.indexOf(selectedUnit);
            if (index >= 0) spinner.setSelection(index);
        });
    }

    // ============================================================
    // Utensilios (catálogo de utensilios.php)
    // ============================================================

    private void showUtensilSelector() {
        ApiHelper.get(requireContext(), "utensilios.php", (ok, message, body) -> {
            if (!isAdded()) return;
            if (!ok || body == null) {
                showToast(message);
                return;
            }
            try {
                JSONArray array = new JSONArray(body);
                int n = array.length();
                int[] ids = new int[n];
                String[] names = new String[n];
                boolean[] checked = new boolean[n];
                for (int i = 0; i < n; i++) {
                    JSONObject u = array.getJSONObject(i);
                    ids[i] = u.getInt("id_utensilio");
                    names[i] = u.getString("nombre");
                    checked[i] = selectedUtensils.containsKey(ids[i]);
                }
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Utensilios especiales")
                        .setMultiChoiceItems(names, checked, (d, which, isChecked) -> checked[which] = isChecked)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Listo", (d, w) -> {
                            selectedUtensils.clear();
                            for (int i = 0; i < n; i++) if (checked[i]) selectedUtensils.put(ids[i], names[i]);
                            showSelectedUtensils();
                        })
                        .show();
            } catch (Exception e) {
                showToast("No se pudo cargar la lista de utensilios");
            }
        });
    }

    private void showSelectedUtensils() {
        etUtensils.setText(selectedUtensils.isEmpty() ? "" : String.join(", ", selectedUtensils.values()));
    }

    private View addIngredientRow() {

        View row = getLayoutInflater()
                .inflate(
                        R.layout.item_ingredient_row,
                        null
                );

        Spinner spinnerType = row.findViewById(R.id.spinner_ingredient_type);
        if (spinnerType != null) {
            ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.ingredient_types,
                    android.R.layout.simple_spinner_item
            );
            adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(adapterType);
        }

        Spinner spinner =
                row.findViewById(R.id.spinner_unit);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.ingredient_units,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        EditText etQuantity = row.findViewById(R.id.et_ingredient_quantity);
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if ("Al gusto".equalsIgnoreCase(selected)) {
                    etQuantity.setText("0");
                    etQuantity.setEnabled(false);
                    etQuantity.setError(null);
                } else {
                    etQuantity.setEnabled(true);
                    if ("0".equals(etQuantity.getText().toString().trim())) {
                        etQuantity.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        TextView tvIngredient =
                row.findViewById(
                        R.id.tv_selected_ingredient
                );

        tvIngredient.setOnClickListener(v ->
                showIngredientSelector(row)
        );

        ImageButton btnRemove =
                row.findViewById(
                        R.id.btn_remove_ingredient
                );

        btnRemove.setOnClickListener(v ->
                containerIngredients.removeView(row)
        );

        containerIngredients.addView(row);
        return row;
    }

    private void showIngredientSelector(View row) {

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar ingrediente")
                .create();

        LinearLayout layout = new LinearLayout(requireContext());

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText search = new EditText(requireContext());

        search.setHint("Buscar ingrediente...");

        layout.addView(search);

        RecyclerView recyclerView =
                new RecyclerView(requireContext());

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        IngredientAdapter adapter =
                new IngredientAdapter(
                        ingredient -> {

                            TextView selected =
                                    row.findViewById(
                                            R.id.tv_selected_ingredient
                                    );

                            selected.setText(
                                    ingredient.getNombre()
                            );

                            row.setTag(
                                    ingredient
                            );

                            Spinner spinnerUnit = row.findViewById(R.id.spinner_unit);
                            // Medidas compatibles configuradas en la BD (ingrediente_unidad)
                            List<String> units = ingredient.getUnidadesReceta();
                            if (spinnerUnit != null && !units.isEmpty()) {
                                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        units
                                );
                                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerUnit.setAdapter(unitAdapter);
                            }

                            dialog.dismiss();
                        }
                );

        recyclerView.setAdapter(adapter);

        layout.addView(
                recyclerView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        600
                )
        );

        dialog.setView(layout);

        search.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {}

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        ingredientViewModel
                                .buscarIngredientes(
                                        s.toString()
                                );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {}
                }
        );

        ingredientViewModel.getIngredients()
                .observe(
                        getViewLifecycleOwner(),
                        adapter::setIngredients
                );

        dialog.show();

        ingredientViewModel.buscarIngredientes("");
    }

    private View addCondimentRow() {
        if (containerCondiments == null) return null;

        View row = getLayoutInflater().inflate(R.layout.item_condiment_row, null);

        Spinner spinner = row.findViewById(R.id.spinner_condiment_unit);
        EditText etQuantity = row.findViewById(R.id.et_condiment_quantity);
        if (spinner != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.ingredient_units,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            if (etQuantity != null) {
                spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        String selected = parent.getItemAtPosition(position).toString();
                        if ("Al gusto".equalsIgnoreCase(selected)) {
                            etQuantity.setText("0");
                            etQuantity.setEnabled(false);
                            etQuantity.setError(null);
                        } else {
                            etQuantity.setEnabled(true);
                            if ("0".equals(etQuantity.getText().toString().trim())) {
                                etQuantity.setText("");
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
            }
        }

        TextView tvCondiment = row.findViewById(R.id.tv_selected_condiment);
        if (tvCondiment != null) {
            tvCondiment.setOnClickListener(v -> showCondimentSelector(row));
        }

        ImageButton btnRemove = row.findViewById(R.id.btn_remove_condiment);
        if (btnRemove != null) {
            btnRemove.setOnClickListener(v -> containerCondiments.removeView(row));
        }

        containerCondiments.addView(row);
        return row;
    }

    private void showCondimentSelector(View row) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Seleccionar condimento")
                .create();

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText search = new EditText(requireContext());
        search.setHint("Buscar condimento...");
        layout.addView(search);

        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        IngredientAdapter adapter = new IngredientAdapter(ingredient -> {
            TextView selected = row.findViewById(R.id.tv_selected_condiment);
            if (selected != null) {
                selected.setText(ingredient.getNombre());
            }
            row.setTag(ingredient);

            Spinner spinnerUnit = row.findViewById(R.id.spinner_condiment_unit);
            // Medidas compatibles configuradas en la BD (especia_unidad)
            List<String> units = ingredient.getUnidadesReceta();
            if (spinnerUnit != null && !units.isEmpty()) {
                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        units
                );
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUnit.setAdapter(unitAdapter);
            }

            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);
        layout.addView(recyclerView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 600
        ));

        dialog.setView(layout);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredientViewModel.buscarCondimentos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ingredientViewModel.getCondiments().observe(getViewLifecycleOwner(), adapter::setIngredients);

        dialog.show();
        ingredientViewModel.buscarCondimentos("");
    }

    private View addStepRow() {
        stepCount++;
        View row = getLayoutInflater().inflate(R.layout.item_step_row, null);

        TextView tvNumber = row.findViewById(R.id.tv_step_number);
        tvNumber.setText(stepCount + ".");

        ImageButton btnRemove = row.findViewById(R.id.btn_remove_step);
        btnRemove.setOnClickListener(v -> {
            containerSteps.removeView(row);
            updateStepNumbers();
        });

        containerSteps.addView(row);
        return row;
    }

    private void updateStepNumbers() {
        stepCount = 0;
        for (int i = 0; i < containerSteps.getChildCount(); i++) {
            stepCount++;
            View row = containerSteps.getChildAt(i);
            TextView tvNumber = row.findViewById(R.id.tv_step_number);
            tvNumber.setText(stepCount + ".");
        }
    }

    private void saveRecipe() {

        if (saving) return; // ya se está guardando
        if (ApiHelper.userId(requireContext()) <= 0) {
            showToast("Inicia sesión para guardar recetas");
            return;
        }

        // Obtener datos generales de la receta
        String name = etName.getText().toString().trim();
        String portionsText = etPortions.getText().toString().trim();
        String timeText = etTime.getText().toString().trim();
        String utensils = etUtensils.getText().toString().trim();

        // Validar nombre
        if (name.isEmpty()) {
            etName.setError("Ingresa el nombre de la receta");
            etName.requestFocus();
            return;
        }

        // Validar porciones
        if (portionsText.isEmpty()) {
            etPortions.setError("Ingresa las porciones");
            etPortions.requestFocus();
            return;
        }

        // Validar tiempo
        if (timeText.isEmpty()) {
            etTime.setError("Ingresa el tiempo de preparación");
            etTime.requestFocus();
            return;
        }

        int portions;
        int time;

        try {
            portions = Integer.parseInt(portionsText);
            time = Integer.parseInt(timeText);
        } catch (NumberFormatException e) {
            showToast("Las porciones y el tiempo deben ser números");
            return;
        }

        // ============================================================
        // INGREDIENTES
        // ============================================================

        StringBuilder ingredientsStr = new StringBuilder();

        for (int i = 0; i < containerIngredients.getChildCount(); i++) {

            View row = containerIngredients.getChildAt(i);

            EditText etQuantity = row.findViewById(R.id.et_ingredient_quantity);
            Spinner spinnerUnit = row.findViewById(R.id.spinner_unit);

            // El ingrediente seleccionado está guardado en el TAG
            Ingredient ingredient = (Ingredient) row.getTag();

            // Verificar que haya seleccionado un ingrediente
            if (ingredient == null) {
                showToast("Selecciona un ingrediente en la fila " + (i + 1));
                return;
            }

            // Obtener unidad
            String unit = spinnerUnit.getSelectedItem().toString();
            boolean atTaste = "Al gusto".equalsIgnoreCase(unit);

            double quantity = 0;

            if (!atTaste) {
                // Obtener cantidad
                String quantityText = etQuantity.getText().toString().trim();

                if (quantityText.isEmpty()) {
                    etQuantity.setError("Ingresa la cantidad");
                    etQuantity.requestFocus();
                    return;
                }

                try {
                    quantity = Double.parseDouble(quantityText);
                } catch (NumberFormatException e) {
                    etQuantity.setError("Cantidad no válida");
                    etQuantity.requestFocus();
                    return;
                }

                if (quantity <= 0) {
                    etQuantity.setError("La cantidad debe ser mayor a 0");
                    etQuantity.requestFocus();
                    return;
                }

                if (unit.contains("Gramos") && quantity > 10000) {
                    etQuantity.setError("Máximo 10,000 gr. Usa Kilogramos.");
                    etQuantity.requestFocus();
                    return;
                } else if (unit.contains("Mililitros") && quantity > 10000) {
                    etQuantity.setError("Máximo 10,000 ml. Usa Litros.");
                    etQuantity.requestFocus();
                    return;
                } else if ((unit.contains("Kilogramos") || unit.contains("Litros")) && quantity > 50) {
                    etQuantity.setError("Cantidad máxima: 50.");
                    etQuantity.requestFocus();
                    return;
                } else if (unit.contains("Piezas") && quantity > 100) {
                    etQuantity.setError("Cantidad máxima: 100.");
                    etQuantity.requestFocus();
                    return;
                } else if ((unit.contains("Cucharad") || unit.contains("Tazas")) && quantity > 30) {
                    etQuantity.setError("Cantidad máxima: 30.");
                    etQuantity.requestFocus();
                    return;
                }
            }

            // Obtener tipo de ingrediente (Principal, Complementario, Condimento)
            Spinner spinnerType = row.findViewById(R.id.spinner_ingredient_type);
            String type = (spinnerType != null && spinnerType.getSelectedItem() != null)
                    ? spinnerType.getSelectedItem().toString()
                    : "Ingrediente Principal";

            // ========================================================
            // Aquí ya tenemos el ingrediente REAL de la BD
            // ========================================================

            int idIngrediente = ingredient.getId_ingrediente();
            String nombreIngrediente = ingredient.getNombre();

            String unitError = FoodUnitHelper.validateUnit(nombreIngrediente, unit, ingredient.getUnidadesReceta());
            if (unitError != null) {
                showToast(unitError);
                return;
            }

            // Construir texto para mostrar/guardar temporalmente
            if (ingredientsStr.length() > 0) {
                ingredientsStr.append("\n");
            }

            if (atTaste) {
                ingredientsStr.append("- ")
                        .append(nombreIngrediente)
                        .append(" (Al gusto, ")
                        .append(type)
                        .append(")");
            } else {
                ingredientsStr.append("- ")
                        .append(quantity)
                        .append(" ")
                        .append(unit)
                        .append(" de ")
                        .append(nombreIngrediente)
                        .append(" (")
                        .append(type)
                        .append(")");
            }

            // Por ahora también podemos mostrar el ID para comprobar
            // que realmente estamos utilizando el ingrediente de la BD.
            Log.d(
                    "RECETA",
                    "Ingrediente: " + nombreIngrediente +
                            " | ID: " + idIngrediente +
                            " | Tipo: " + type +
                            " | Cantidad: " + quantity +
                            " | Unidad: " + unit
            );
        }

        // ============================================================
        // CONDIMENTOS Y ESPECIAS
        // ============================================================

        if (containerCondiments != null) {
            for (int i = 0; i < containerCondiments.getChildCount(); i++) {

                View row = containerCondiments.getChildAt(i);

                EditText etQuantity = row.findViewById(R.id.et_condiment_quantity);
                Spinner spinnerUnit = row.findViewById(R.id.spinner_condiment_unit);

                Ingredient condiment = (Ingredient) row.getTag();

                if (condiment == null) {
                    showToast("Selecciona un condimento en la fila " + (i + 1));
                    return;
                }

                String unit = spinnerUnit.getSelectedItem().toString();
                boolean atTaste = "Al gusto".equalsIgnoreCase(unit);
                double quantity = 0;

                if (!atTaste) {
                    String quantityText = etQuantity.getText().toString().trim();

                    if (quantityText.isEmpty()) {
                        etQuantity.setError("Ingresa la cantidad");
                        etQuantity.requestFocus();
                        return;
                    }

                    try {
                        quantity = Double.parseDouble(quantityText);
                    } catch (NumberFormatException e) {
                        etQuantity.setError("Cantidad no válida");
                        etQuantity.requestFocus();
                        return;
                    }

                    if (quantity <= 0) {
                        etQuantity.setError("La cantidad debe ser mayor a 0");
                        etQuantity.requestFocus();
                        return;
                    }

                    if (unit.contains("Gramos") && quantity > 1000) {
                        etQuantity.setError("Máximo 1,000 gr para condimentos");
                        etQuantity.requestFocus();
                        return;
                    }
                }

                int idCondimento = condiment.getId_ingrediente();
                String nombreCondimento = condiment.getNombre();

                String unitError = FoodUnitHelper.validateUnit(nombreCondimento, unit, condiment.getUnidadesReceta());
                if (unitError != null) {
                    showToast(unitError);
                    return;
                }

                if (ingredientsStr.length() > 0) {
                    ingredientsStr.append("\n");
                }

                if (atTaste) {
                    ingredientsStr.append("- ")
                            .append(nombreCondimento)
                            .append(" (Al gusto, Condimento)");
                } else {
                    ingredientsStr.append("- ")
                            .append(quantity)
                            .append(" ")
                            .append(unit)
                            .append(" de ")
                            .append(nombreCondimento)
                            .append(" (Condimento)");
                }

                Log.d(
                        "RECETA",
                        "Condimento: " + nombreCondimento +
                                " | ID: " + idCondimento +
                                " | Cantidad: " + quantity +
                                " | Unidad: " + unit
                );
            }
        }

        // ============================================================
        // PASOS
        // ============================================================

        StringBuilder stepsStr = new StringBuilder();
        List<String> stepsList = new ArrayList<>();

        for (int i = 0; i < containerSteps.getChildCount(); i++) {

            View row = containerSteps.getChildAt(i);

            EditText etStep = row.findViewById(R.id.et_step_description);

            if (etStep == null) {
                continue;
            }

            String step = etStep.getText().toString().trim();

            if (!step.isEmpty()) {

                stepsList.add(step);

                if (stepsStr.length() > 0) {
                    stepsStr.append("\n");
                }

                stepsStr.append((i + 1))
                        .append(". ")
                        .append(step);
            }
        }
        if (stepsList.isEmpty()) {
            showToast("Agrega al menos un paso");
            return;
        }

        // ============================================================
        // ARMAR LA PETICIÓN (guardar_receta.php / editar_receta.php)
        // ============================================================

        DifficultyCalculator.Result difficulty = DifficultyCalculator.calcular(stepsList);
        List<NutritionViewModel.IngredientData> nutritionInput = new ArrayList<>();

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("id_usuario", ApiHelper.userId(requireContext()));
            if (editing != null) requestJson.put("id_receta", editing.id_receta);
            requestJson.put("nombre", name);
            requestJson.put("porciones", portions);
            requestJson.put("tiempo_estimado", time);
            requestJson.put("nivel_dificultad", difficulty.nivel != null ? difficulty.nivel.toLowerCase() : "principiante");
            requestJson.put("descripcion", "");

            // Ingredientes
            JSONArray jsonIngredientes = new JSONArray();
            for (int i = 0; i < containerIngredients.getChildCount(); i++) {
                View row = containerIngredients.getChildAt(i);
                Ingredient ing = (Ingredient) row.getTag();
                EditText etQty = row.findViewById(R.id.et_ingredient_quantity);
                Spinner spUnit = row.findViewById(R.id.spinner_unit);
                Spinner spType = row.findViewById(R.id.spinner_ingredient_type);

                if (ing != null && etQty != null && spUnit != null) {
                    String unit = spUnit.getSelectedItem().toString();
                    double qty = "Al gusto".equalsIgnoreCase(unit) ? 0 : parseQuantity(etQty);
                    JSONObject item = new JSONObject();
                    item.put("id_ingrediente", ing.getId_ingrediente());
                    item.put("cantidad", qty);
                    item.put("unidad", unit);
                    String typeStr = (spType != null && spType.getSelectedItem() != null)
                            ? spType.getSelectedItem().toString().toLowerCase()
                            : "principal";
                    item.put("tipo", typeStr.contains("complementario") ? "complementario" : "principal");
                    jsonIngredientes.put(item);
                    nutritionInput.add(new NutritionViewModel.IngredientData(
                            ing.getNombre(), toGrams(qty, unit), qty <= 0));
                }
            }
            requestJson.put("ingredientes", jsonIngredientes);

            // Condimentos
            JSONArray jsonCondimentos = new JSONArray();
            if (containerCondiments != null) {
                for (int i = 0; i < containerCondiments.getChildCount(); i++) {
                    View row = containerCondiments.getChildAt(i);
                    Ingredient cond = (Ingredient) row.getTag();
                    EditText etQty = row.findViewById(R.id.et_condiment_quantity);
                    Spinner spUnit = row.findViewById(R.id.spinner_condiment_unit);

                    if (cond != null && etQty != null && spUnit != null) {
                        String unit = spUnit.getSelectedItem().toString();
                        JSONObject item = new JSONObject();
                        item.put("id_especia", cond.getId_ingrediente());
                        item.put("cantidad", "Al gusto".equalsIgnoreCase(unit) ? 0 : parseQuantity(etQty));
                        item.put("unidad", unit);
                        jsonCondimentos.put(item);
                    }
                }
            }
            requestJson.put("condimentos", jsonCondimentos);

            // Pasos
            JSONArray jsonPasos = new JSONArray();
            for (String paso : stepsList) {
                jsonPasos.put(paso);
            }
            requestJson.put("pasos", jsonPasos);

            // Utensilios (ids del catálogo)
            JSONArray jsonUtensilios = new JSONArray();
            for (Integer id : selectedUtensils.keySet()) jsonUtensilios.put(id);
            requestJson.put("utensilios", jsonUtensilios);

            // Etiquetas escritas por el usuario (el servidor agrega las automáticas)
            JSONArray jsonEtiquetas = new JSONArray();
            for (String tag : etTags.getText().toString().split(",")) {
                if (!tag.trim().isEmpty()) jsonEtiquetas.put(tag.trim());
            }
            requestJson.put("etiquetas", jsonEtiquetas);

            setSaving(true, "Calculando información nutricional...");
            Log.d("RECETA", "Dificultad: " + difficulty + " | ingredientes:\n" + ingredientsStr);

            // 1) Nutrición por porción con USDA -> 2) subir imagen -> 3) enviar receta
            requestJson.put("porciones", portions);
            pendingRequest = requestJson;
            boolean anyToCalculate = false;
            for (NutritionViewModel.IngredientData d : nutritionInput) {
                if (!d.isToTaste && d.quantity > 0) anyToCalculate = true;
            }
            if (anyToCalculate) {
                nutritionViewModel.calculateTotalNutrition(nutritionInput);
            } else {
                pendingRequest = null;
                uploadImageThenSend(requestJson);
            }
        } catch (JSONException e) {
            setSaving(false, null);
            showToast("No se pudo preparar la receta");
        }
    }

    private static double parseQuantity(EditText et) {
        try {
            return Double.parseDouble(et.getText().toString().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Conversión aproximada a gramos para consultar USDA (que da valores por 100 g). */
    private static double toGrams(double qty, String unit) {
        String u = unit.toLowerCase();
        if (u.contains("kilogramo") || u.contains("litro")) return qty * 1000;
        if (u.contains("taza")) return qty * 240;
        if (u.contains("cucharadita")) return qty * 5;
        if (u.contains("cucharada")) return qty * 15;
        if (u.contains("pieza")) return qty * 100;
        return qty; // gramos o mililitros
    }

    /** Agrega los valores POR PORCIÓN (si USDA no respondió, no se mandan). */
    private void putNutrition(JSONObject request, NutritionViewModel.RecipeNutrition total) {
        try {
            int portions = Math.max(1, request.optInt("porciones", 1));
            if (total.calories > 0) {
                request.put("calorias_porcion", round2(total.calories / portions));
                request.put("proteinas_porcion", round2(total.protein / portions));
                request.put("carbohidratos_porcion", round2(total.carbs / portions));
                request.put("grasas_porcion", round2(total.fats / portions));
            }
        } catch (JSONException ignored) {
        }
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private void uploadImageThenSend(JSONObject request) {
        if (!isAdded()) return;
        if (selectedImageUri == null) {
            putImage(request, existingImagePath);
            sendRecipe(request);
            return;
        }
        setSaving(true, "Subiendo imagen...");
        ApiHelper.uploadImage(requireContext(), selectedImageUri, (ok, message, path) -> {
            if (!isAdded()) return;
            if (!ok) {
                setSaving(false, null);
                showToast(message);
                return;
            }
            // Si se vuelve a intentar no se sube otra vez la misma imagen
            selectedImageUri = null;
            existingImagePath = path;
            putImage(request, path);
            sendRecipe(request);
        });
    }

    private static void putImage(JSONObject request, @Nullable String path) {
        try {
            request.put("imagen", path == null ? "" : path);
        } catch (JSONException ignored) {
        }
    }

    private void sendRecipe(JSONObject request) {
        setSaving(true, "Guardando...");
        String endpoint = editing != null ? "editar_receta.php" : "guardar_receta.php";
        ApiHelper.postJson(requireContext(), endpoint, request, (ok, message, body) -> {
            if (!isAdded()) return;
            setSaving(false, null);
            showToast(message);
            if (!ok) return;
            if (editing != null) {
                // Volver al inicio (el detalle que estaba abierto ya tiene datos viejos)
                getParentFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                resetForm();
            }
        });
    }

    private void setSaving(boolean value, @Nullable String status) {
        saving = value;
        btnSave.setEnabled(!value);
        btnSave.setText(value && status != null ? status
                : (editing != null ? "Guardar cambios" : getString(R.string.btn_save_recipe)));
    }

    /** Deja el formulario vacío después de publicar una receta nueva. */
    private void resetForm() {
        etName.setText("");
        etPortions.setText("");
        etTime.setText("");
        etTags.setText("");
        selectedUtensils.clear();
        showSelectedUtensils();
        selectedImageUri = null;
        existingImagePath = null;
        ivRecipeImage.setImageResource(android.R.drawable.ic_menu_gallery);
        containerIngredients.removeAllViews();
        if (containerCondiments != null) containerCondiments.removeAllViews();
        containerSteps.removeAllViews();
        stepCount = 0;
        addIngredientRow();
        addCondimentRow();
        addStepRow();
    }
}
