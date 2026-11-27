package com.sergio.proyectov1.admin;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.api.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cola de revisión de imágenes (carril 2 de la moderación con SafeSearch).
 * El administrador ve la imagen, los puntajes por categoría y decide.
 */
public class AdminModerationFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private final List<JSONObject> items = new ArrayList<>();
    private final Adapter adapter = new Adapter();

    /** Para que la pestaña muestre cuántas imágenes hay pendientes. */
    public interface PendingCountListener {
        void onPendingCount(int count);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_moderation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_moderation);
        tvEmpty = view.findViewById(R.id.tv_no_moderation);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        int admin = ApiHelper.userId(requireContext());
        ApiHelper.get(requireContext(), "moderacion.php?id_admin=" + admin, (ok, message, body) -> {
            if (!isAdded()) return;
            items.clear();
            if (ok && body != null) {
                try {
                    JSONArray array = new JSONArray(body);
                    for (int i = 0; i < array.length(); i++) items.add(array.getJSONObject(i));
                } catch (Exception ignored) {
                }
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            if (getActivity() instanceof PendingCountListener) {
                ((PendingCountListener) getActivity()).onPendingCount(items.size());
            }
        });
    }

    private void decide(JSONObject item, String action, @Nullable String reason) {
        Map<String, String> p = new HashMap<>();
        p.put("id_admin", String.valueOf(ApiHelper.userId(requireContext())));
        p.put("id_moderacion", String.valueOf(item.optInt("id_moderacion")));
        p.put("accion", action);
        if (reason != null) p.put("motivo", reason);
        ApiHelper.post(requireContext(), "moderacion.php", p, (ok, message, body) -> {
            if (!isAdded()) return;
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            load();
        });
    }

    /** Pide el motivo (obligatorio al rechazar, opcional al pedir otra imagen). */
    private void askReason(JSONObject item, String action) {
        boolean required = "rechazar".equals(action);
        TextInputLayout til = new TextInputLayout(requireContext(), null,
                com.google.android.material.R.attr.textInputOutlinedStyle);
        til.setHint(required ? "Motivo del rechazo" : "Motivo (opcional)");
        TextInputEditText input = new TextInputEditText(til.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        til.addView(input);
        FrameLayout box = new FrameLayout(requireContext());
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        box.setPadding(pad, pad / 2, pad, 0);
        box.addView(til);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(required ? "Rechazar imagen" : "Pedir otra imagen")
                .setMessage("El autor recibirá este motivo.")
                .setView(box)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton(required ? "Rechazar" : "Enviar", null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String reason = input.getText() == null ? "" : input.getText().toString().trim();
            if (required && reason.isEmpty()) {
                til.setError("Escribe el motivo");
                return;
            }
            dialog.dismiss();
            decide(item, action, reason.isEmpty() ? null : reason);
        }));
        dialog.show();
    }

    private static String scores(JSONObject o) {
        String[][] rows = {
                {"adult", "Adultos"}, {"violence", "Violencia"}, {"racy", "Sugerente"},
                {"medical", "Médico"}, {"spoof", "Alterada"}
        };
        StringBuilder sb = new StringBuilder();
        for (String[] r : rows) {
            String value = o.isNull(r[0]) ? "sin análisis" : o.optString(r[0]);
            sb.append(String.format("%-10s %s%n", r[1], value));
        }
        return sb.toString().trim();
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_moderation, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder h, int position) {
            JSONObject o = items.get(position);
            ApiHelper.loadImage(h.image, o.optString("imagen"), android.R.drawable.ic_menu_gallery);
            h.recipe.setText(o.optString("receta"));
            h.author.setText("Por " + o.optString("autor") + " · " + o.optString("fecha"));
            h.reason.setText(o.isNull("motivo") ? "" : o.optString("motivo"));
            h.scores.setText(scores(o));
            h.approve.setOnClickListener(v -> decide(o, "aprobar", null));
            h.reject.setOnClickListener(v -> askReason(o, "rechazar"));
            h.resubmit.setOnClickListener(v -> askReason(o, "resubir"));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView recipe, author, reason, scores;
            final View approve, reject, resubmit;

            Holder(@NonNull View v) {
                super(v);
                image = v.findViewById(R.id.iv_moderation_image);
                recipe = v.findViewById(R.id.tv_moderation_recipe);
                author = v.findViewById(R.id.tv_moderation_author);
                reason = v.findViewById(R.id.tv_moderation_reason);
                scores = v.findViewById(R.id.tv_moderation_scores);
                approve = v.findViewById(R.id.btn_moderation_approve);
                reject = v.findViewById(R.id.btn_moderation_reject);
                resubmit = v.findViewById(R.id.btn_moderation_resubmit);
            }
        }
    }
}
