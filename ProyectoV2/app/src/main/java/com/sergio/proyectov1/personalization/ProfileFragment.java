package com.sergio.proyectov1.personalization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.auth.login;
import com.sergio.proyectov1.core.MainActivity;
import com.sergio.proyectov1.models.AuthResponse;
import com.sergio.proyectov1.models.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Perfil y personalización (módulo 3) más las opciones de cuenta (RQF1.4, RQF1.6, RQF1.7).
 */
public class ProfileFragment extends Fragment {

    private TextInputLayout tilName, tilAge, tilSex, tilWeight, tilHeight,
            tilActivity, tilGoal, tilCooking, tilTime;
    private TextInputEditText etName, etAge, etWeight, etHeight, etTime;
    private AutoCompleteTextView ddSex, ddActivity, ddGoal, ddCooking;
    private MaterialButtonToggleGroup toggleMeals;
    private ChipGroup chipsAllergies, chipsDislikes;
    private TextInputEditText etAllergyInput, etDislikeInput;
    private View cardResults;
    private TextView tvTarget, tvGoalHint, tvTmbTdee, tvEmail;
    private ImageView ivProfileImage;

    private final List<String> allergies = new ArrayList<>();
    private final List<String> dislikes = new ArrayList<>();

    private PersonalizationViewModel viewModel;
    private ProfileStore store;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null && ivProfileImage != null) {
                    ivProfileImage.setImageURI(uri);
                }
            });

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        store = new ProfileStore(requireContext());
        viewModel = new ViewModelProvider(this).get(PersonalizationViewModel.class);

        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        if (toolbar != null) toolbar.setTitle("Mi Perfil");

        bindViews(view);
        setupDropdown(ddSex, R.array.sex_options);
        setupDropdown(ddActivity, R.array.activity_levels);
        setupDropdown(ddGoal, R.array.goals_options);
        setupDropdown(ddCooking, R.array.cooking_levels);
        clearErrorOnChange();

        setupChipInput(view, R.id.btn_add_allergy, etAllergyInput, allergies, chipsAllergies);
        setupChipInput(view, R.id.btn_add_dislike, etDislikeInput, dislikes, chipsDislikes);

        SharedPreferences session = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tvEmail.setText(session.getString("user_email", ""));

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), this::showResults);

        UserProfile saved = store.load();
        if (saved != null) {
            fillForm(saved);
            viewModel.setProfile(saved);
        } else {
            etName.setText(session.getString("user_name", ""));
            toggleMeals.check(R.id.btn_meals_3);
        }

        // Consultar a la base de datos remota si existe una versión actualizada
        store.fetchRemoteProfile(new ProfileStore.ProfileCallback() {
            @Override
            public void onSuccess(UserProfile remoteProfile) {
                if (!isAdded() || remoteProfile == null) return;
                fillForm(remoteProfile);
                viewModel.calculateNutrition(remoteProfile);
                store.saveLocal(remoteProfile);
            }

            @Override
            public void onError(String message) {
                // Si falla o no hay conexión, ya se tiene la versión local cargada
            }
        });

        ivProfileImage.setOnClickListener(v -> launchPhotoPicker());
        view.findViewById(R.id.fab_edit_profile_image).setOnClickListener(v -> launchPhotoPicker());
        view.findViewById(R.id.btn_save_profile).setOnClickListener(v -> saveAndCalculate());
        view.findViewById(R.id.btn_logout_profile).setOnClickListener(v -> logoutUser());
        view.findViewById(R.id.btn_delete_account).setOnClickListener(v -> confirmDeleteAccount());
    }

    private void bindViews(View v) {
        tilName = v.findViewById(R.id.til_name);
        tilAge = v.findViewById(R.id.til_age);
        tilSex = v.findViewById(R.id.til_sex);
        tilWeight = v.findViewById(R.id.til_weight);
        tilHeight = v.findViewById(R.id.til_height);
        tilActivity = v.findViewById(R.id.til_activity);
        tilGoal = v.findViewById(R.id.til_goal);
        tilCooking = v.findViewById(R.id.til_cooking);
        tilTime = v.findViewById(R.id.til_time);

        etName = v.findViewById(R.id.et_profile_username);
        etAge = v.findViewById(R.id.et_age);
        etWeight = v.findViewById(R.id.et_weight);
        etHeight = v.findViewById(R.id.et_height);
        etTime = v.findViewById(R.id.et_available_time);

        ddSex = v.findViewById(R.id.dd_sex);
        ddActivity = v.findViewById(R.id.dd_activity);
        ddGoal = v.findViewById(R.id.dd_goal);
        ddCooking = v.findViewById(R.id.dd_cooking_level);

        toggleMeals = v.findViewById(R.id.toggle_meals);
        chipsAllergies = v.findViewById(R.id.chip_group_allergies);
        chipsDislikes = v.findViewById(R.id.chip_group_dislikes);
        etAllergyInput = v.findViewById(R.id.et_allergy_input);
        etDislikeInput = v.findViewById(R.id.et_dislike_input);

        cardResults = v.findViewById(R.id.layout_results);
        tvTarget = v.findViewById(R.id.tv_target);
        tvGoalHint = v.findViewById(R.id.tv_goal_hint);
        tvTmbTdee = v.findViewById(R.id.tv_tmb_tdee);
        tvEmail = v.findViewById(R.id.tv_profile_email);
        ivProfileImage = v.findViewById(R.id.iv_profile_image);
    }

    // ---------- Dropdowns ----------

    private void setupDropdown(AutoCompleteTextView view, int arrayRes) {
        String[] items = getResources().getStringArray(arrayRes);
        view.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, items));
    }

    /** Posición elegida en el arreglo, o -1 si el usuario no ha elegido nada. */
    private int indexOf(AutoCompleteTextView view, int arrayRes) {
        String[] items = getResources().getStringArray(arrayRes);
        return Arrays.asList(items).indexOf(view.getText().toString());
    }

    private void select(AutoCompleteTextView view, int arrayRes, int index) {
        String[] items = getResources().getStringArray(arrayRes);
        if (index >= 0 && index < items.length) {
            view.setText(items[index], false); // false: no filtrar la lista
        }
    }

    private void clearErrorOnChange() {
        Map<TextInputLayout, TextView> pairs = new HashMap<>();
        pairs.put(tilName, etName);
        pairs.put(tilAge, etAge);
        pairs.put(tilWeight, etWeight);
        pairs.put(tilHeight, etHeight);
        pairs.put(tilTime, etTime);
        pairs.put(tilSex, ddSex);
        pairs.put(tilActivity, ddActivity);
        pairs.put(tilGoal, ddGoal);
        pairs.put(tilCooking, ddCooking);
        for (Map.Entry<TextInputLayout, TextView> e : pairs.entrySet()) {
            final TextInputLayout til = e.getKey();
            e.getValue().addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
                @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                    til.setError(null);
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });
        }
    }

    // ---------- Alergias y aversiones ----------

    private void setupChipInput(View root, int buttonId, TextInputEditText input,
                                List<String> target, ChipGroup group) {
        MaterialButton add = root.findViewById(buttonId);
        add.setOnClickListener(v -> addItems(input, target, group));
        input.setOnEditorActionListener((tv, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addItems(input, target, group);
                return true;
            }
            return false;
        });
    }

    /** Acepta varios ingredientes separados por coma y evita duplicados. */
    private void addItems(TextInputEditText input, List<String> target, ChipGroup group) {
        String raw = input.getText() == null ? "" : input.getText().toString();
        for (String part : raw.split(",")) {
            String item = part.trim();
            if (item.isEmpty() || containsIgnoreCase(target, item)) continue;
            target.add(item);
        }
        input.setText("");
        renderChips(target, group);
    }

    private boolean containsIgnoreCase(List<String> list, String item) {
        for (String s : list) if (s.equalsIgnoreCase(item)) return true;
        return false;
    }

    private void renderChips(List<String> items, ChipGroup group) {
        group.removeAllViews();
        for (String item : new ArrayList<>(items)) {
            Chip chip = new Chip(requireContext());
            chip.setText(item);
            chip.setCloseIconVisible(true);
            chip.setCloseIconContentDescription("Quitar " + item);
            chip.setOnCloseIconClickListener(v -> {
                items.remove(item);
                renderChips(items, group);
            });
            group.addView(chip);
        }
    }

    // ---------- Guardar y calcular ----------

    private void fillForm(UserProfile p) {
        etName.setText(p.getUsername());
        etAge.setText(String.valueOf(p.getAge()));
        etWeight.setText(trimNumber(p.getWeight()));
        etHeight.setText(trimNumber(p.getHeight()));
        etTime.setText(String.valueOf(p.getAvailableTimeMinutes()));
        select(ddSex, R.array.sex_options, "Mujer".equalsIgnoreCase(p.getSex()) ? 1 : 0);
        select(ddActivity, R.array.activity_levels, p.getActivityIndex());
        select(ddGoal, R.array.goals_options, p.getGoalIndex());
        select(ddCooking, R.array.cooking_levels,
                Arrays.asList(getResources().getStringArray(R.array.cooking_levels))
                        .indexOf(p.getCookingLevel()));
        toggleMeals.check(p.getMealTimes() == 5 ? R.id.btn_meals_5 : R.id.btn_meals_3);
        allergies.clear();
        allergies.addAll(p.getAllergies());
        dislikes.clear();
        dislikes.addAll(p.getDislikes());
        renderChips(allergies, chipsAllergies);
        renderChips(dislikes, chipsDislikes);
    }

    private String trimNumber(double d) {
        return d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
    }

    private String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private boolean fail(TextInputLayout til, String message) {
        til.setError(message);
        return false;
    }

    /** Valida todo el formulario y marca cada campo con error. Devuelve true si todo es correcto. */
    private boolean validate() {
        boolean ok = true;
        View firstError = null;

        if (text(etName).length() < 2) { ok = fail(tilName, "Escribe tu nombre"); firstError = tilName; }

        int age = parseInt(text(etAge));
        if (age < NutritionCalculator.MIN_AGE || age > NutritionCalculator.MAX_AGE) {
            ok = fail(tilAge, "Entre " + NutritionCalculator.MIN_AGE + " y " + NutritionCalculator.MAX_AGE);
            if (firstError == null) firstError = tilAge;
        }
        if (indexOf(ddSex, R.array.sex_options) < 0) {
            ok = fail(tilSex, "Elige una opción");
            if (firstError == null) firstError = tilSex;
        }
        double weight = parseDouble(text(etWeight));
        if (weight < NutritionCalculator.MIN_WEIGHT || weight > NutritionCalculator.MAX_WEIGHT) {
            ok = fail(tilWeight, "Entre " + (int) NutritionCalculator.MIN_WEIGHT + " y " + (int) NutritionCalculator.MAX_WEIGHT + " kg");
            if (firstError == null) firstError = tilWeight;
        }
        double height = parseDouble(text(etHeight));
        if (height < NutritionCalculator.MIN_HEIGHT || height > NutritionCalculator.MAX_HEIGHT) {
            ok = fail(tilHeight, "Entre " + (int) NutritionCalculator.MIN_HEIGHT + " y " + (int) NutritionCalculator.MAX_HEIGHT + " cm");
            if (firstError == null) firstError = tilHeight;
        }
        if (indexOf(ddActivity, R.array.activity_levels) < 0) {
            ok = fail(tilActivity, "Elige una opción");
            if (firstError == null) firstError = tilActivity;
        }
        if (indexOf(ddGoal, R.array.goals_options) < 0) {
            ok = fail(tilGoal, "Elige una opción");
            if (firstError == null) firstError = tilGoal;
        }
        if (indexOf(ddCooking, R.array.cooking_levels) < 0) {
            ok = fail(tilCooking, "Elige una opción");
            if (firstError == null) firstError = tilCooking;
        }
        int time = parseInt(text(etTime));
        if (time < NutritionCalculator.MIN_TIME || time > NutritionCalculator.MAX_TIME) {
            ok = fail(tilTime, "Entre " + NutritionCalculator.MIN_TIME + " y " + NutritionCalculator.MAX_TIME + " minutos");
            if (firstError == null) firstError = tilTime;
        }
        return ok;
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return -1; }
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(',', '.')); } catch (NumberFormatException e) { return -1; }
    }

    private void saveAndCalculate() {
        if (!validate()) {
            Toast.makeText(getContext(), "Revisa los campos marcados", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean firstTime = !store.exists();

        UserProfile p = new UserProfile();
        SharedPreferences session = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        p.setEmail(session.getString("user_email", ""));
        p.setUsername(text(etName));
        p.setAge(parseInt(text(etAge)));
        p.setWeight(parseDouble(text(etWeight)));
        p.setHeight(parseDouble(text(etHeight)));
        p.setSex(indexOf(ddSex, R.array.sex_options) == 1 ? "Mujer" : "Hombre");
        p.setActivityIndex(indexOf(ddActivity, R.array.activity_levels));
        p.setActivityLevel(ddActivity.getText().toString());
        p.setGoalIndex(indexOf(ddGoal, R.array.goals_options));
        p.setGoal(ddGoal.getText().toString());
        p.setCookingLevel(ddCooking.getText().toString());
        p.setAvailableTimeMinutes(parseInt(text(etTime)));
        p.setMealTimes(toggleMeals.getCheckedButtonId() == R.id.btn_meals_5 ? 5 : 3);
        p.setAllergies(new ArrayList<>(allergies));
        p.setDislikes(new ArrayList<>(dislikes));

        viewModel.calculateNutrition(p);
        session.edit().putString("user_name", p.getUsername()).apply();

        View btnSave = getView() != null ? getView().findViewById(R.id.btn_save_profile) : null;
        if (btnSave != null) btnSave.setEnabled(false);

        // Guardar en la base de datos (guardar_perfil.php) y en el almacenamiento local
        store.save(p, new ProfileStore.SaveCallback() {
            @Override
            public void onSuccess(String message) {
                if (!isAdded()) return;
                if (btnSave != null) btnSave.setEnabled(true);
                Toast.makeText(getContext(), message != null ? message : "Perfil guardado en la base de datos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                if (btnSave != null) btnSave.setEnabled(true);
                // Mostrar el error exacto que devolvió el servidor o la red
                Toast.makeText(getContext(), "Error servidor: " + (message != null ? message : "Sin conexión"), Toast.LENGTH_LONG).show();
            }
        });

        // Primer llenado tras el registro (RQF1.2): continuar a Inicio
        if (firstTime && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).goHome();
        }
    }

    private void showResults(UserProfile p) {
        if (p == null || p.getTdee() <= 0) {
            cardResults.setVisibility(View.GONE);
            return;
        }
        cardResults.setVisibility(View.VISIBLE);
        tvTarget.setText(String.format(Locale.getDefault(), "%.0f kcal", p.getDailyCalorieTarget()));
        double diff = p.getDailyCalorieTarget() - p.getTdee();
        String hint;
        if (Math.abs(diff) < 1) hint = "Mantenimiento";
        else if (diff < 0) hint = String.format(Locale.getDefault(), "Déficit de %.0f kcal sobre tu TDEE", -diff);
        else hint = String.format(Locale.getDefault(), "Superávit de %.0f kcal sobre tu TDEE", diff);
        tvGoalHint.setText(hint);
        tvTmbTdee.setText(String.format(Locale.getDefault(),
                "TMB: %.0f kcal  ·  TDEE: %.0f kcal", p.getTmb(), p.getTdee()));
    }

    // ---------- Cuenta ----------

    private void logoutUser() {
        // Solo se borra la sesión; el perfil se conserva en ProfilePrefs
        requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit().clear().apply();
        goToLogin();
        Toast.makeText(getContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }

    /** RQNF1.7: mensaje de confirmación antes de eliminar. */
    private void confirmDeleteAccount() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Eliminar tu cuenta?")
                .setMessage("Se borrarán de forma permanente tus datos personales, perfil, alacena, "
                        + "utensilios, recetas, comentarios y calificaciones. Esta acción no se puede deshacer.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Continuar", (d, w) -> askPassword())
                .show();
    }

    /** RQF1.7: confirmación explícita con contraseña. */
    private void askPassword() {
        TextInputLayout til = new TextInputLayout(requireContext(), null,
                com.google.android.material.R.attr.textInputOutlinedStyle);
        til.setHint("Contraseña");
        til.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        TextInputEditText input = new TextInputEditText(til.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        til.addView(input);

        FrameLayout box = new FrameLayout(requireContext());
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        box.setPadding(pad, pad / 2, pad, 0);
        box.addView(til);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirma con tu contraseña")
                .setView(box)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar definitivamente", null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String pwd = input.getText() == null ? "" : input.getText().toString();
                    if (pwd.isEmpty()) {
                        til.setError("Ingresa tu contraseña");
                        return;
                    }
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    deleteAccount(pwd, dialog, til);
                }));
        dialog.show();
    }

    /**
     * Pide al servidor borrar la cuenta (RQNF1.5 y RQNF1.6).
     * eliminar_cuenta.php recibe id_usuario y password, valida la contraseña (bcrypt)
     * y borra todo en una transacción. Responde {"success":true|false,"message":"..."}.
     */
    private void deleteAccount(String password, AlertDialog dialog, TextInputLayout til) {
        SharedPreferences session = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        final int userId = session.getInt("user_id", 0);
        String url = VolleySingleton.BASE_URL + "eliminar_cuenta.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (!isAdded()) return;
                    AuthResponse r = parse(response);
                    if (r != null && r.isSuccess()) {
                        dialog.dismiss();
                        store.clear();
                        session.edit().clear().apply();
                        Toast.makeText(getContext(), "Tu cuenta fue eliminada", Toast.LENGTH_LONG).show();
                        goToLogin();
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        til.setError(r != null && r.getMessage() != null ? r.getMessage() : "No se pudo eliminar la cuenta");
                    }
                },
                error -> {
                    if (!isAdded()) return;
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    til.setError("No hay conexión con el servidor");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(userId));
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(requireContext()).addToRequestQueue(req);
    }

    private AuthResponse parse(String response) {
        try {
            String json = response == null ? "" : response.trim();
            int a = json.indexOf('{'), b = json.lastIndexOf('}');
            if (a >= 0 && b > a) json = json.substring(a, b + 1);
            return new Gson().fromJson(json, AuthResponse.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void launchPhotoPicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
