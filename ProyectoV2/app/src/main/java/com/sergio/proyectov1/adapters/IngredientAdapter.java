package com.sergio.proyectov1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sergio.proyectov1.R;
import com.sergio.proyectov1.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    private final List<Ingredient> ingredients = new ArrayList<>();

    private final OnIngredientClickListener listener;

    public IngredientAdapter(
            OnIngredientClickListener listener
    ) {
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> newIngredients) {

        ingredients.clear();

        if (newIngredients != null) {
            ingredients.addAll(newIngredients);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_ingredient_result,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Ingredient ingredient = ingredients.get(position);

        holder.name.setText(ingredient.getNombre());

        holder.itemView.setOnClickListener(v ->
                listener.onIngredientClick(ingredient)
        );
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(
                    R.id.tv_ingredient_name
            );
        }
    }
}