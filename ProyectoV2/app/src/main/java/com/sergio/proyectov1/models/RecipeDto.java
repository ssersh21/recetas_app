package com.sergio.proyectov1.models;

import java.io.Serializable;
import java.util.List;

/**
 * Respuesta de recetas.php (los nombres coinciden con el JSON del servidor).
 * Es Serializable para poder pasar la receta completa a la pantalla de edición.
 */
public class RecipeDto implements Serializable {

    public int id_receta;
    public String nombre;
    public int porciones;
    public int tiempo_estimado;
    public String nivel_dificultad;
    public String imagen;
    public String descripcion;
    public double calificacion_promedio;
    public int total_calificaciones;
    public int total_reportes;

    public int id_usuario;          // autor de la receta
    public String autor;
    public String estado;

    // Valores por UNA porción (null = no calculados)
    public Double calorias_porcion;
    public Double proteinas_porcion;
    public Double carbohidratos_porcion;
    public Double grasas_porcion;

    public List<Item> ingredientes;
    public List<Item> condimentos;
    public List<Paso> pasos;
    public List<Utensilio> utensilios;
    public List<Etiqueta> etiquetas;

    /** Sirve para ingredientes y condimentos (id_especia). */
    public static class Item implements Serializable {
        public int id_ingrediente;
        public int id_especia;
        public String nombre;
        public double cantidad;
        public String unidad;
        public String tipo;
    }

    public static class Paso implements Serializable {
        public int numero_paso;
        public String descripcion;
    }

    public static class Utensilio implements Serializable {
        public int id_utensilio;
        public String nombre;
    }

    public static class Etiqueta implements Serializable {
        public String nombre;
        public boolean automatica;
    }
}
