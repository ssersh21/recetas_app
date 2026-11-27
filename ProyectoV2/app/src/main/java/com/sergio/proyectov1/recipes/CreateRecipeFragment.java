package com.sergio.proyectov1.recipes;

import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.models.Recipe;
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



public class CreateRecipeFragment extends Fragment {

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

        // Añadir una fila inicial por defecto
        addIngredientRow();
        addCondimentRow();
        addStepRow();

        return view;
    }

    private void addIngredientRow() {

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

    private void addCondimentRow() {
        if (containerCondiments == null) return;

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

    private void addStepRow() {
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

        // ============================================================
        // CREAR RECETA
        // ============================================================

        Recipe recipe = new Recipe(
                name,
                time + " min",
                android.R.drawable.ic_menu_gallery,
                portions + " personas",
                ingredientsStr.toString(),
                stepsStr.toString()
        );

        // ============================================================
        // GUARDAR EN BASE DE DATOS MEDIANTE VOLLEY (guardar_receta.php)
        // ============================================================

        DifficultyCalculator.Result difficulty = DifficultyCalculator.calcular(stepsList);

        try {
            JSONObject requestJson = new JSONObject();

            int userId = 1;
            if (getContext() != null) {
                SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                userId = prefs.getInt("user_id", 1);
                if (userId <= 0) userId = 1;
            }

            requestJson.put("id_usuario", userId);
            requestJson.put("nombre", name);
            requestJson.put("porciones", portions);
            requestJson.put("tiempo_estimado", time);
            requestJson.put("nivel_dificultad", difficulty.nivel != null ? difficulty.nivel.toLowerCase() : "principiante");
            requestJson.put("descripcion", utensils);
            requestJson.put("imagen", "");

            // Ingredientes
            JSONArray jsonIngredientes = new JSONArray();
            for (int i = 0; i < containerIngredients.getChildCount(); i++) {
                View row = containerIngredients.getChildAt(i);
                Ingredient ing = (Ingredient) row.getTag();
                EditText etQty = row.findViewById(R.id.et_ingredient_quantity);
                Spinner spUnit = row.findViewById(R.id.spinner_unit);
                Spinner spType = row.findViewById(R.id.spinner_ingredient_type);

                if (ing != null && etQty != null && spUnit != null) {
                    JSONObject item = new JSONObject();
                    item.put("id_ingrediente", ing.getId_ingrediente());
                    item.put("cantidad", Double.parseDouble(etQty.getText().toString().trim()));
                    item.put("unidad", spUnit.getSelectedItem().toString());
                    String typeStr = (spType != null && spType.getSelectedItem() != null)
                            ? spType.getSelectedItem().toString().toLowerCase()
                            : "principal";
                    item.put("tipo", typeStr.contains("complementario") ? "complementario" : "principal");
                    jsonIngredientes.put(item);
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
                        JSONObject item = new JSONObject();
                        item.put("id_especia", cond.getId_ingrediente());
                        item.put("cantidad", Double.parseDouble(etQty.getText().toString().trim()));
                        item.put("unidad", spUnit.getSelectedItem().toString());
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

            btnSave.setEnabled(false);
            String url = VolleySingleton.BASE_URL + "guardar_receta.php";

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestJson,
                    response -> {
                        if (!isAdded()) return;
                        btnSave.setEnabled(true);
                        showToast("¡Receta guardada en la base de datos!");
                        Log.d("RECETA", "Respuesta servidor: " + response.toString());
                    },
                    error -> {
                        if (!isAdded()) return;
                        btnSave.setEnabled(true);
                        showToast("Receta creada (dificultad: " + difficulty.nivel + ")");
                    }
            );

            VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Receta creada (dificultad: " + difficulty.nivel + ")");
        }

        Log.d("RECETA", "Dificultad: " + difficulty);

        Log.d("RECETA", "Nombre: " + name);
        Log.d("RECETA", "Porciones: " + portions);
        Log.d("RECETA", "Tiempo: " + time);
        Log.d("RECETA", "Ingredientes:\n" + ingredientsStr);
        Log.d("RECETA", "Pasos:\n" + stepsStr);
        Log.d("RECETA", "Utensilios: " + utensils);
    }
}