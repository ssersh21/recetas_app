package com.sergio.proyectov1.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sergio.proyectov1.R;

import java.util.List;
import java.util.Locale;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.Holder> {

    public interface Listener {
        void onEdit(StockItem item);
        void onDelete(StockItem item);
        /** Solo en la lista de compras: el usuario marcó el artículo como comprado. */
        void onBought(StockItem item);
    }

    private final List<StockItem> items;
    private final boolean shoppingMode;
    private final Listener listener;

    public StockAdapter(List<StockItem> items, boolean shoppingMode, Listener listener) {
        this.items = items;
        this.shoppingMode = shoppingMode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        StockItem item = items.get(position);
        h.name.setText(item.getName());
        h.qty.setText(format(item));
        h.check.setVisibility(shoppingMode ? View.VISIBLE : View.GONE);
        h.check.setOnCheckedChangeListener(null);
        h.check.setChecked(false);
        h.check.setContentDescription("Marcar " + item.getName() + " como comprado");
        if (shoppingMode) {
            h.check.setOnCheckedChangeListener((b, checked) -> {
                if (checked) listener.onBought(item);
            });
        }
        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setContentDescription("Eliminar " + item.getName());
        h.delete.setOnClickListener(v -> listener.onDelete(item));
    }

    private String format(StockItem item) {
        double q = item.getQuantity();
        String number = q == Math.floor(q)
                ? String.valueOf((long) q) : String.format(Locale.getDefault(), "%.1f", q);
        String unit = item.getUnit() == null ? "" : item.getUnit();
        return unit.equals("Al gusto") ? unit : (number + " " + unit).trim();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final CheckBox check;
        final TextView name, qty;
        final ImageButton delete;

        Holder(@NonNull View v) {
            super(v);
            check = v.findViewById(R.id.cb_stock);
            name = v.findViewById(R.id.tv_stock_name);
            qty = v.findViewById(R.id.tv_stock_qty);
            delete = v.findViewById(R.id.ib_stock_delete);
        }
    }
}
