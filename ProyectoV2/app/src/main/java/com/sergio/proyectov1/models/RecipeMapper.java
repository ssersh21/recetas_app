package com.sergio.proyectov1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Convierte lo que devuelve el servidor en el modelo Recipe de la app. */
public final class RecipeMapper {

    private RecipeMapper() {
    }

    public static List<Recipe> toRecipes(List<RecipeDto> dtos) {
        List<Recipe> result = new ArrayList<>();
        if (dtos == null) return result;
        for (RecipeDto dto : dtos) {
            result.add(toRecipe(dto));
        }
        return result;
    }

    public static Recipe toRecipe(RecipeDto dto) {

        // Mismo formato que arma CreateRecipeFragment:
        // "- 500 g de Pechuga de pollo"
        // RecipeDetailFragment lo usa para calcular los macros.
        StringBuilder ingredients = new StringBuilder();

        if (dto.ingredientes != null) {
            for (RecipeDto.Item item : dto.ingredientes) {
                appendLine(ingredients, item, null);
            }
        }
        if (dto.condimentos != null) {
            for (RecipeDto.Item item : dto.condimentos) {
                appendLine(ingredients, item, "Condimento");
            }
        }

        StringBuilder steps = new StringBuilder();
        if (dto.pasos != null) {
            for (RecipeDto.Paso paso : dto.pasos) {
                if (steps.length() > 0) steps.append("\n");
                steps.append(paso.numero_paso).append(". ").append(paso.descripcion);
            }
        }

        Recipe recipe = new Recipe(
                dto.nombre,
                dto.tiempo_estimado + " min",
                android.R.drawable.ic_menu_gallery,
                dto.porciones + " personas",
                ingredients.toString(),
                steps.toString()
        );

        recipe.setId(dto.id_receta);
        recipe.setDifficulty(dto.nivel_dificultad);
        recipe.setDescription(dto.descripcion);
        recipe.setImageUrl(dto.imagen);
        recipe.setRating(dto.calificacion_promedio, dto.total_calificaciones);
        recipe.setOwnerId(dto.id_usuario);
        recipe.setAuthor(dto.autor);
        recipe.setSource(dto);
        recipe.setStatus(dto.estado);

        // El servidor guarda los valores por porción; Recipe guarda los totales
        if (dto.calorias_porcion != null && dto.calorias_porcion > 0) {
            int p = Math.max(1, dto.porciones);
            recipe.setCalories(dto.calorias_porcion * p);
            recipe.setProtein(valueOrZero(dto.proteinas_porcion) * p);
            recipe.setCarbs(valueOrZero(dto.carbohidratos_porcion) * p);
            recipe.setFats(valueOrZero(dto.grasas_porcion) * p);
        }

        List<String> tags = new ArrayList<>();
        if (dto.etiquetas != null) {
            for (RecipeDto.Etiqueta e : dto.etiquetas) tags.add(e.nombre);
        }
        recipe.setTags(tags);

        List<String> utensils = new ArrayList<>();
        if (dto.utensilios != null) {
            for (RecipeDto.Utensilio u : dto.utensilios) utensils.add(u.nombre);
        }
        recipe.setUtensils(utensils);

        return recipe;
    }

    private static void appendLine(StringBuilder sb, RecipeDto.Item item, String label) {
        if (sb.length() > 0) sb.append("\n");

        sb.append("- ")
                .append(formatQuantity(item.cantidad))
                .append(" ")
                .append(item.unidad == null ? "" : item.unidad)
                .append(" de ")
                .append(item.nombre);

        if (label != null) {
            sb.append(" (").append(label).append(")");
        }
    }

    private static double valueOrZero(Double d) {
        return d == null ? 0 : d;
    }

    /** 500.0 -> "500", 0.5 -> "0.5" */
    public static String formatQuantity(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format(Locale.US, "%s", value);
    }
}
