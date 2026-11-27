package com.sergio.proyectov1.adapters;

import com.sergio.proyectov1.models.Category;
import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private RecipeAdapter.OnRecipeClickListener listener;

    public CategoryAdapter(List<Category> categories, RecipeAdapter.OnRecipeClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_row, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvTitle.setText(category.getTitle());
        
        // Configurar RecyclerView horizontal
        RecipeAdapter recipeAdapter = new RecipeAdapter(category.getRecipes(), listener);
        holder.rvRecipes.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.rvRecipes.setAdapter(recipeAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RecyclerView rvRecipes;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_category_title);
            rvRecipes = itemView.findViewById(R.id.rv_horizontal_recipes);
        }
    }
}