package com.sergio.proyectov1.pantry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.adapters.IngredientAdapter;
import com.sergio.proyectov1.models.FoodUnitHelper;
import com.sergio.proyectov1.viewmodels.IngredientViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Una sola pantalla para la alacena y para la lista de compras (cambia el modo).
 * - La alacena se guarda en la BD (alacena.php), que valida las unidades.
 * - La lista de compras se guarda en el teléfono.
 * En ambas el ingrediente solo se puede elegir de la BD y la unidad solo de las
 * medidas que la BD tiene para ese ingrediente.
 * En la lista de compras, marcar un artículo como comprado lo pasa a la alacena (RQNF6.3.1).
 */
public class StockListFragment extends Fragment implements StockAdapter.Listener {

    private static final String ARG_MODE = "mode";

    public static StockListFragment newInstance(String mode) {
        StockListFragment f = new StockListFragment();
        Bundle b = new Bundle();
        b.putString(ARG_MODE, mode);
        f.setArguments(b);
        return f;
    }

    private boolean shopping;
    private ListStore shoppingStore;      // solo lista de compras (teléfono)
    private PantryRepository pantryRepo;  // alacena (servidor)
    private List<StockItem> items = new ArrayList<>();
    private RecyclerView rv;
    private android.widget.TextView empty;
    private IngredientViewModel ingredientViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        ingredientViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show();
                ingredientViewModel.clearError();
            }
        });

        shopping = getArguments() != null
                && ListStore.SHOPPING.equals(getArguments().getString(ARG_MODE));
        shoppingStore = new ListStore(requireContext(), ListStore.SHOPPING);
        pantryRepo = new PantryRepository(requireContext());

        Toolbar toolbar = view.findViewById(R.id.stock_toolbar);
        toolbar.setTitle(shopping ? "Lista de compras" : "Mi alacena");
        if (shopping) {
            toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationContentDescription("Volver");
            toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        } else {
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
        }

        ((android.widget.TextView) view.findViewById(R.id.tv_stock_hint)).setText(shopping
                ? "Marca un artículo cuando lo compres y pasará a tu alacena."
                : "Registra lo que tienes en casa para recibir recetas que puedas preparar.");

        rv = view.findViewById(R.id.rv_stock);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        empty = view.findViewById(R.id.tv_stock_empty);

        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab_add_stock);
        fab.setOnClickListener(v -> showDialog(null));

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rv != null && !shopping) refresh();
    }

    private void refresh() {
        if (shopping) {
            show(shoppingStore.load(), null);
            return;
        }
        pantryRepo.load((list, error) -> {
            if (!isAdded()) return;
            show(list != null ? list : Collections.emptyList(), error);
        });
    }

    private void show(List<StockItem> list, @Nullable String error) {
        items = new ArrayList<>(list);
        rv.setAdapter(new StockAdapter(items, shopping, this));
        empty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        if (error != null) {
            empty.setText(error);
        } else {
            empty.setText(shopping ? "Tu lista de compras está vacía"
                    : "Aún no tienes ingredientes en la alacena");
        }
    }

    // ============================================================
    // Formulario agregar / editar
    // ============================================================

    /** Estado del formulario: ingrediente elegido de la BD y sus medidas permitidas. */
    private static class FormState {
        int ingredientId;
        String ingredientName = "";
        String requestedName = "";
        List<String> units;          // null = cargando; vacía = no se pudieron cargar
    }

    /** Diálogo para agregar (item == null) o editar un ingrediente. */
    private void showDialog(@Nullable StockItem item) {
        View form = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_stock, null);
        TextInputLayout tilName = form.findViewById(R.id.til_stock_name);
        TextInputLayout tilQty = form.findViewById(R.id.til_stock_qty);
        TextInputLayout tilUnit = form.findViewById(R.id.til_stock_unit);
        TextInputEditText etName = form.findViewById(R.id.et_stock_name);
        TextInputEditText etQty = form.findViewById(R.id.et_stock_qty);
        AutoCompleteTextView ddUnit = form.findViewById(R.id.dd_stock_unit);

        FormState state = new FormState();

        // El ingrediente solo se elige del buscador de la BD
        View.OnClickListener openSelector = v -> showIngredientSelector(state, etName, tilName, ddUnit, tilUnit);
        etName.setOnClickListener(openSelector);
        tilName.setEndIconOnClickListener(openSelector);

        ddUnit.setOnItemClickListener((parent, view1, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            if ("Al gusto".equalsIgnoreCase(selected)) {
                etQty.setText("0");
                tilQty.setEnabled(false);
                tilQty.setError(null);
            } else {
                tilQty.setEnabled(true);
                if ("0".equals(etQty.getText() != null ? etQty.getText().toString().trim() : "")) {
                    etQty.setText("");
                }
            }
        });

        if (item != null) {
            etName.setText(item.getName());
            etQty.setText(item.getQuantity() == Math.floor(item.getQuantity())
                    ? String.valueOf((long) item.getQuantity()) : String.valueOf(item.getQuantity()));
            if (item.getIngredientId() > 0) {
                state.ingredientId = item.getIngredientId();
                state.ingredientName = item.getName();
                if (!item.getAllowedUnits().isEmpty()) {
                    setUnits(state, ddUnit, tilUnit, item.getAllowedUnits(), item.getUnit());
                } else {
                    loadUnits(state, item.getName(), ddUnit, tilUnit, item.getUnit());
                }
            } else {
                // Registro viejo escrito a mano: hay que elegir el ingrediente de la BD
                tilName.setError("Vuelve a elegir el ingrediente de la lista");
            }
            if ("Al gusto".equalsIgnoreCase(item.getUnit())) {
                tilQty.setEnabled(false);
            }
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(item == null ? "Agregar ingrediente" : "Editar ingrediente")
                .setView(form)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Guardar", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String unit = ddUnit.getText().toString();
                    boolean atTaste = "Al gusto".equalsIgnoreCase(unit);
                    double qty = 0;
                    boolean ok = true;

                    tilName.setError(null);
                    tilQty.setError(null);
                    tilUnit.setError(null);

                    if (state.ingredientId <= 0) {
                        tilName.setError("Selecciona el ingrediente de la lista");
                        ok = false;
                    } else {
                        String unitError = state.units == null
                                ? "Espera a que carguen las unidades"
                                : FoodUnitHelper.validateUnit(state.ingredientName, unit, state.units);
                        if (unitError != null) {
                            tilUnit.setError(unitError);
                            ok = false;
                        }
                    }

                    if (!atTaste) {
                        try {
                            String q = etQty.getText() == null ? "" : etQty.getText().toString().replace(',', '.');
                            qty = Double.parseDouble(q);
                            if (qty <= 0) {
                                tilQty.setError("Cantidad mayor a 0");
                                ok = false;
                            } else if (unit.contains("Gramos") && qty > 10000) {
                                tilQty.setError("Máximo 10,000 gr. Usa Kilogramos si es mayor.");
                                ok = false;
                            } else if (unit.contains("Mililitros") && qty > 10000) {
                                tilQty.setError("Máximo 10,000 ml. Usa Litros si es mayor.");
                                ok = false;
                            } else if ((unit.contains("Kilogramos") || unit.contains("Litros")) && qty > 50) {
                                tilQty.setError("Cantidad máxima: 50.");
                                ok = false;
                            } else if (unit.contains("Piezas") && qty > 100) {
                                tilQty.setError("Cantidad máxima: 100 piezas.");
                                ok = false;
                            } else if ((unit.contains("Cucharad") || unit.contains("Tazas")) && qty > 30) {
                                tilQty.setError("Cantidad máxima: 30.");
                                ok = false;
                            }
                        } catch (NumberFormatException e) {
                            tilQty.setError("Cantidad no válida"); ok = false;
                        }
                    }
                    if (!ok) return;

                    save(item, state, qty, unit, dialog, tilUnit);
                }));
        dialog.show();
    }

    private void save(@Nullable StockItem item, FormState state, double qty, String unit,
                      AlertDialog dialog, TextInputLayout tilUnit) {
        if (shopping) {
            if (item == null) {
                shoppingStore.add(new StockItem(state.ingredientId, state.ingredientName, qty, unit));
            } else {
                item.setIngredientId(state.ingredientId);
                item.setName(state.ingredientName);
                item.setQuantity(qty);
                item.setUnit(unit);
                shoppingStore.save(items);
            }
            dialog.dismiss();
            refresh();
            return;
        }

        // Alacena: el servidor valida y guarda
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        PantryRepository.ResultCallback done = (success, message) -> {
            if (!isAdded()) return;
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            if (success) {
                dialog.dismiss();
                refresh();
            } else {
                tilUnit.setError(message);
            }
        };
        if (item == null || item.getPantryId() <= 0) {
            pantryRepo.add(state.ingredientId, qty, unit, done);
        } else {
            pantryRepo.update(item.getPantryId(), state.ingredientId, qty, unit, done);
        }
    }

    private void setUnits(FormState state, AutoCompleteTextView ddUnit, TextInputLayout tilUnit,
                          List<String> units, @Nullable String preferredUnit) {
        state.units = units;
        ddUnit.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, units));
        if (units.isEmpty()) {
            ddUnit.setText("", false);
            tilUnit.setError(FoodUnitHelper.UNITS_NOT_LOADED);
            return;
        }
        if (preferredUnit != null && units.contains(preferredUnit)) {
            ddUnit.setText(preferredUnit, false);
        } else {
            ddUnit.setText(units.get(0), false);
        }
        tilUnit.setError(null);
    }

    /** Pide al servidor las medidas compatibles del ingrediente (sin lista de respaldo). */
    private void loadUnits(FormState state, String name, AutoCompleteTextView ddUnit,
                           TextInputLayout tilUnit, @Nullable String preferredUnit) {
        state.units = null;
        state.requestedName = name;
        ddUnit.setText("", false);
        FoodUnitHelper.loadUnits(FoodUnitHelper.TIPO_INGREDIENTE, FoodUnitHelper.CONTEXTO_ALACENA, name, units -> {
            // Ignorar respuestas viejas o si el diálogo ya se cerró
            if (!isAdded() || !name.equals(state.requestedName)) return;
            setUnits(state, ddUnit, tilUnit, units, preferredUnit);
        });
    }

    /** Diálogo de búsqueda en BD idéntico al de las recetas */
    private void showIngredientSelector(FormState state, TextInputEditText etName, TextInputLayout tilName,
                                        AutoCompleteTextView ddUnit, TextInputLayout tilUnit) {
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Seleccionar ingrediente")
                .create();

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText search = new EditText(requireContext());
        search.setHint("Buscar ingrediente...");
        layout.addView(search);

        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        IngredientAdapter adapter = new IngredientAdapter(ingredient -> {
            FoodUnitHelper.remember(FoodUnitHelper.TIPO_INGREDIENTE, ingredient);
            state.ingredientId = ingredient.getId_ingrediente();
            state.ingredientName = ingredient.getNombre();
            etName.setText(ingredient.getNombre());
            tilName.setError(null);
            if (!ingredient.getUnidadesAlacena().isEmpty()) {
                state.requestedName = ingredient.getNombre();
                setUnits(state, ddUnit, tilUnit, ingredient.getUnidadesAlacena(), null);
            } else {
                loadUnits(state, ingredient.getNombre(), ddUnit, tilUnit, null);
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
                ingredientViewModel.buscarIngredientes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ingredientViewModel.getIngredients().observe(getViewLifecycleOwner(), adapter::setIngredients);

        dialog.show();
        ingredientViewModel.buscarIngredientes("");
    }

    // ============================================================
    // Acciones de la lista
    // ============================================================

    @Override
    public void onEdit(StockItem item) {
        showDialog(item);
    }

    @Override
    public void onDelete(StockItem item) {
        if (shopping) {
            items.remove(item);
            shoppingStore.save(items);
            refresh();
            Snackbar.make(requireView(), item.getName() + " eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer", v -> {
                        shoppingStore.add(item);
                        refresh();
                    }).show();
            return;
        }

        pantryRepo.delete(item.getPantryId(), (success, message) -> {
            if (!isAdded()) return;
            refresh();
            if (!success) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
                return;
            }
            Snackbar.make(requireView(), item.getName() + " eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer", v -> pantryRepo.add(item.getIngredientId(), item.getQuantity(),
                            item.getUnit(), (ok, msg) -> {
                                if (isAdded()) refresh();
                            })).show();
        });
    }

    @Override
    public void onBought(StockItem item) {
        if (item.getIngredientId() <= 0) {
            rv.post(this::refresh);
            Snackbar.make(requireView(), "Edita " + item.getName()
                    + " y elige el ingrediente de la lista antes de pasarlo a la alacena", Snackbar.LENGTH_LONG).show();
            return;
        }
        // Solo se quita de la lista de compras si el servidor lo aceptó en la alacena
        pantryRepo.add(item.getIngredientId(), item.getQuantity(), item.getUnit(), (success, message) -> {
            if (!isAdded()) return;
            if (success) {
                items.remove(item);
                shoppingStore.save(items);
                Snackbar.make(requireView(), item.getName() + " pasó a tu alacena", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
            }
            refresh();
        });
    }
}
