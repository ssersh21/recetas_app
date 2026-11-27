package com.sergio.proyectov1.personalization;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.api.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sección "Mis utensilios especiales" del perfil.
 * Lee y guarda los utensilios del usuario en la BD (utensilios.php → usuario_utensilio).
 */
public class UtensilsSection {

    private final Fragment fragment;
    private final TextView tvSummary;
    private final String emptyText;

    public UtensilsSection(Fragment fragment, View root) {
        this.fragment = fragment;
        this.tvSummary = root.findViewById(R.id.tv_my_utensils);
        this.emptyText = tvSummary.getText().toString();
        root.findViewById(R.id.btn_my_utensils).setOnClickListener(v -> openSelector());
        refresh();
    }

    private int userId() {
        return ApiHelper.userId(fragment.requireContext());
    }

    /** Muestra los utensilios que ya tiene el usuario. */
    public void refresh() {
        if (userId() <= 0) return;
        ApiHelper.get(fragment.requireContext(), "utensilios.php?id_usuario=" + userId(), (ok, message, body) -> {
            if (!fragment.isAdded() || !ok || body == null) return;
            try {
                JSONArray array = new JSONArray(body);
                List<String> mine = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject u = array.getJSONObject(i);
                    if (u.optBoolean("tengo")) mine.add(u.getString("nombre"));
                }
                tvSummary.setText(mine.isEmpty() ? emptyText : String.join(", ", mine));
            } catch (Exception ignored) {
            }
        });
    }

    private void openSelector() {
        if (userId() <= 0) {
            toast("Inicia sesión para guardar tus utensilios");
            return;
        }
        ApiHelper.get(fragment.requireContext(), "utensilios.php?id_usuario=" + userId(), (ok, message, body) -> {
            if (!fragment.isAdded()) return;
            if (!ok || body == null) {
                toast(message);
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
                    checked[i] = u.optBoolean("tengo");
                }
                new MaterialAlertDialogBuilder(fragment.requireContext())
                        .setTitle("¿Cuáles tienes en casa?")
                        .setMultiChoiceItems(names, checked, (d, which, isChecked) -> checked[which] = isChecked)
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Guardar", (d, w) -> save(ids, checked))
                        .show();
            } catch (Exception e) {
                toast("No se pudo cargar la lista de utensilios");
            }
        });
    }

    private void save(int[] ids, boolean[] checked) {
        StringBuilder selected = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (!checked[i]) continue;
            if (selected.length() > 0) selected.append(",");
            selected.append(ids[i]);
        }
        Map<String, String> params = new HashMap<>();
        params.put("id_usuario", String.valueOf(userId()));
        params.put("utensilios", selected.toString());
        ApiHelper.post(fragment.requireContext(), "utensilios.php", params, (ok, message, body) -> {
            if (!fragment.isAdded()) return;
            toast(message);
            if (ok) refresh();
        });
    }

    private void toast(String message) {
        Toast.makeText(fragment.requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
