package com.sergio.proyectov1.models;

import java.util.List;

/** Respuesta de recetas.php (los nombres coinciden con el JSON del servidor). */
public class RecipeDto {

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

    public List<Item> ingredientes;
    public List<Item> condimentos;
    public List<Paso> pasos;

    /** Sirve para ingredientes y condimentos (id_especia). */
    public static class Item {
        public int id_ingrediente;
        public int id_especia;
        public String nombre;
        public double cantidad;
        public String unidad;
        public String tipo;
    }

    public static class Paso {
        public int numero_paso;
        public String descripcion;
    }
}
