package com.sergio.proyectov1.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sergio.proyectov1.models.Recipe;
import com.sergio.proyectov1.R;

import java.util.ArrayList;
import java.util.List;

public class AdminRecipeAdapter extends RecyclerView.Adapter<AdminRecipeAdapter.AdminRecipeViewHolder> {

    public interface OnAdminActionListener {
        void onDeleteClick(Recipe recipe);
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final OnAdminActionListener listener;
    private boolean showReportBadge = false;

    public AdminRecipeAdapter(OnAdminActionListener listener, boolean showReportBadge) {
        this.listener = listener;
        this.showReportBadge = showReportBadge;
    }

    public void setRecipes(List<Recipe> newRecipes) {
        recipes.clear();
        if (newRecipes != null) {
            recipes.addAll(newRecipes);
        }
        notifyDataSetChanged();
    }

    public void removeRecipe(Recipe recipe) {
        int pos = recipes.indexOf(recipe);
        if (pos >= 0) {
            recipes.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @NonNull
    @Override
    public AdminRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_recipe_card, parent, false);
        return new AdminRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvSubtitle.setText(recipe.getTime() + " | " + recipe.getPortions());
        holder.ivThumb.setImageResource(recipe.getImageRes());

        if (showReportBadge) {
            holder.tvReports.setVisibility(View.VISIBLE);
            holder.tvReports.setText("⚠️ 5 reportes acumulados");
        } else {
            holder.tvReports.setVisibility(View.GONE);
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(recipe);
            }
        });

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

    static class AdminRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvTitle, tvSubtitle, tvReports;
        Button btnDelete;

        public AdminRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.iv_admin_thumb);
            tvTitle = itemView.findViewById(R.id.tv_admin_recipe_title);
            tvSubtitle = itemView.findViewById(R.id.tv_admin_recipe_subtitle);
            tvReports = itemView.findViewById(R.id.tv_reports_count);
            btnDelete = itemView.findViewById(R.id.btn_delete_recipe);
        }
    }
}
