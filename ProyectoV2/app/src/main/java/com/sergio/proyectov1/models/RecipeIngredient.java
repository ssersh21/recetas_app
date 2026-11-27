package com.sergio.proyectov1.models;

public class RecipeIngredient{

    private int idIngrediente;
    private String nombre;
    private double cantidad;
    private String unidad;
    private String tipo;

    public RecipeIngredient(
            int idIngrediente,
            String nombre,
            double cantidad,
            String unidad,
            String tipo
    ) {
        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.tipo = tipo;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public String getTipo() {
        return tipo;
    }
}