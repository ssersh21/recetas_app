package com.sergio.proyectov1.adapters;

import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvTime.setText(recipe.getTime());
        // Imagen subida por el autor (o el ícono por defecto)
        com.sergio.proyectov1.api.ApiHelper.loadImage(holder.ivThumb, recipe.getImageUrl(), recipe.getImageRes());

        // Promedio real de la tabla calificacion (lo envía recetas.php)
        float rating = (float) recipe.getRatingAverage();
        boolean hasVotes = recipe.getRatingCount() > 0;

        if (holder.ratingBar != null) {
            holder.ratingBar.setRating(hasVotes ? rating : 0f);
        }
        if (holder.tvRatingNum != null) {
            holder.tvRatingNum.setText(hasVotes
                    ? String.format(Locale.getDefault(), "%.1f", rating)
                    : "Nueva");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvTitle, tvTime, tvRatingNum;
        RatingBar ratingBar;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.iv_recipe_thumb);
            tvTitle = itemView.findViewById(R.id.tv_recipe_title);
            tvTime = itemView.findViewById(R.id.tv_recipe_time);
            ratingBar = itemView.findViewById(R.id.rb_recipe_rating);
            tvRatingNum = itemView.findViewById(R.id.tv_recipe_rating_num);
        }
    }
}
