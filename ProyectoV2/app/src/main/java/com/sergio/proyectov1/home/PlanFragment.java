package com.sergio.proyectov1.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.core.MainActivity;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.models.UserProfile;
import com.sergio.proyectov1.pantry.ListStore;
import com.sergio.proyectov1.pantry.StockListFragment;
import com.sergio.proyectov1.personalization.ProfileStore;
import com.sergio.proyectov1.plan.PlanStore;
import com.sergio.proyectov1.plan.PlannedMeal;
import android.widget.Toast;
import com.sergio.proyectov1.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Calendario y plan alimenticio (módulo 6). Muestra los tiempos de comida del perfil
 * para el día elegido y deja asignar una receta a cada uno.
 */
public class PlanFragment extends Fragment {

    private PlanStore planStore;
    private UserProfile profile;
    private RecipeViewModel recipeViewModel;
    private List<Recipe> recipes = new ArrayList<>();

    private String selectedDate;
    private TextView tvDate, tvSummary;
    private LinearProgressIndicator progress;
    private ViewGroup slotsContainer;
    private View cardNoProfile;

    public PlanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        planStore = new PlanStore(requireContext());

        Toolbar toolbar = view.findViewById(R.id.plan_toolbar);
        toolbar.setTitle("Plan alimenticio");
        toolbar.inflateMenu(R.menu.stock_list_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_shopping) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, StockListFragment.newInstance(ListStore.SHOPPING))
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });

        tvDate = view.findViewById(R.id.tv_plan_date);
        tvSummary = view.findViewById(R.id.tv_plan_summary);
        progress = view.findViewById(R.id.progress_calories);
        slotsContainer = view.findViewById(R.id.container_slots);
        cardNoProfile = view.findViewById(R.id.card_no_profile);

        ((MaterialButton) view.findViewById(R.id.btn_go_profile)).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).goToProfile();
        });

        CalendarView calendar = view.findViewById(R.id.calendar_plan);
        Calendar today = Calendar.getInstance();
        selectedDate = key(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        calendar.setOnDateChangeListener((cv, year, month, day) -> {
            selectedDate = key(year, month, day);
            renderDay();
        });

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getRecipes().observe(getViewLifecycleOwner(), list -> {
            recipes = list == null ? new ArrayList<>() : list;
        });
        recipeViewModel.cargarRecetas("");
    }

    @Override
    public void onResume() {
        super.onResume();
        // El perfil pudo cambiar (tiempos de comida o meta) desde la otra pestaña
        profile = new ProfileStore(requireContext()).load();
        renderDay();
    }

    private String key(int year, int month, int day) {
        return String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
    }

    private void renderDay() {
        if (!isAdded()) return;
        tvDate.setText(prettyDate(selectedDate));
        slotsContainer.removeAllViews();

        if (profile == null) {
            cardNoProfile.setVisibility(View.VISIBLE);
            tvSummary.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            return;
        }
        cardNoProfile.setVisibility(View.GONE);

        // El plan está en el servidor; se ignora la respuesta si el usuario ya cambió de día
        final String requestedDate = selectedDate;
        tvSummary.setVisibility(View.VISIBLE);
        tvSummary.setText("Cargando plan...");
        planStore.getDay(requestedDate, (day, error) -> {
            if (!isAdded() || !requestedDate.equals(selectedDate)) return;
            if (day == null) {
                tvSummary.setText(error);
                progress.setVisibility(View.GONE);
                return;
            }
            showDay(day);
        });
    }

    private void showDay(Map<String, PlannedMeal> day) {
        slotsContainer.removeAllViews();
        double total = 0;
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (String slot : PlanStore.slots(profile.getMealTimes())) {
            View row = inflater.inflate(R.layout.item_meal_slot, slotsContainer, false);
            ((TextView) row.findViewById(R.id.tv_slot_name)).setText(slot);
            TextView tvRecipe = row.findViewById(R.id.tv_slot_recipe);
            MaterialButton btnPick = row.findViewById(R.id.btn_slot_pick);
            View btnRemove = row.findViewById(R.id.btn_slot_remove);

            PlannedMeal meal = day.get(slot);
            if (meal == null) {
                tvRecipe.setText("Sin receta");
                btnPick.setText("Elegir");
                btnRemove.setVisibility(View.GONE);
            } else {
                total += meal.getCalories();
                StringBuilder text = new StringBuilder();
                if (meal.isCooked()) text.append("✓ ");
                if (meal.getTime() != null) text.append(meal.getTime()).append(" · ");
                text.append(meal.getTitle());
                if (meal.getCalories() > 0) {
                    text.append(String.format(Locale.getDefault(), " · %.0f kcal", meal.getCalories()));
                }
                tvRecipe.setText(text);
                tvRecipe.setContentDescription(text + (meal.isCooked()
                        ? ". Cocinada. Toca para marcarla como pendiente"
                        : ". Toca para marcarla como cocinada"));
                // Tocar la receta la marca como cocinada (historial) o pendiente
                tvRecipe.setOnClickListener(v -> planStore.setCooked(selectedDate, slot, !meal.isCooked(),
                        (ok, message) -> {
                            if (!isAdded()) return;
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            renderDay();
                        }));
                btnPick.setText("Cambiar");
                btnRemove.setVisibility(View.VISIBLE);
                btnRemove.setOnClickListener(v -> planStore.remove(selectedDate, slot, (ok, message) -> {
                    if (!isAdded()) return;
                    if (!ok) Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    renderDay();
                }));
            }
            btnPick.setOnClickListener(v -> pickRecipe(slot));
            slotsContainer.addView(row);
        }

        double goal = profile.getDailyCalorieTarget();
        tvSummary.setVisibility(View.VISIBLE);
        progress.setVisibility(goal > 0 ? View.VISIBLE : View.GONE);
        tvSummary.setText(String.format(Locale.getDefault(),
                "Planeado: %.0f de %.0f kcal", total, goal));
        if (goal > 0) {
            progress.setMax((int) goal);
            progress.setProgressCompat((int) Math.min(total, goal), true);
        }
    }

    private String prettyDate(String key) {
        String[] p = key.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(p[0]), Integer.parseInt(p[1]) - 1, Integer.parseInt(p[2]));
        return new java.text.SimpleDateFormat("EEEE d 'de' MMMM", new Locale("es", "MX")).format(c.getTime());
    }

    private void pickRecipe(String slot) {
        if (recipes.isEmpty()) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Sin recetas")
                    .setMessage("Aún no hay recetas disponibles para agregar al plan.")
                    .setPositiveButton("Entendido", null)
                    .show();
            return;
        }
        String[] titles = new String[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            Recipe r = recipes.get(i);
            titles[i] = r.getTime() == null || r.getTime().isEmpty()
                    ? r.getTitle() : r.getTitle() + " (" + r.getTime() + ")";
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(slot)
                .setItems(titles, (d, which) -> askTimeAndSave(slot, recipes.get(which)))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /** Hora opcional para la receta programada (RQ: fecha y hora específicas). */
    private void askTimeAndSave(String slot, Recipe recipe) {
        String date = selectedDate;
        PlanStore.ResultCallback done = (ok, message) -> {
            if (!isAdded()) return;
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            renderDay();
        };
        android.app.TimePickerDialog picker = new android.app.TimePickerDialog(requireContext(),
                (view, hour, minute) -> planStore.set(date, slot, recipe.getId(),
                        String.format(Locale.US, "%02d:%02d", hour, minute), done),
                defaultHour(slot), 0, true);
        picker.setTitle("¿A qué hora? (" + slot + ")");
        picker.setButton(android.content.DialogInterface.BUTTON_NEUTRAL, "Sin hora",
                (dlg, w) -> planStore.set(date, slot, recipe.getId(), null, done));
        picker.show();
    }

    private static int defaultHour(String slot) {
        switch (slot) {
            case "Desayuno": return 8;
            case "Colación matutina": return 11;
            case "Comida": return 14;
            case "Colación vespertina": return 17;
            default: return 20;
        }
    }
}
