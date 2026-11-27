package com.sergio.proyectov1.models;

import java.util.Collections;
import java.util.List;

public class Ingredient {

    private int id_ingrediente;
    private int id_condimento;
    private String nombre;

    // Medidas compatibles, vienen de la BD (ingrediente_unidad / especia_unidad)
    private List<String> unidades_alacena;
    private List<String> unidades_receta;

    public Ingredient() {
    }

    public Ingredient(int id_ingrediente, String nombre) {
        this.id_ingrediente = id_ingrediente;
        this.nombre = nombre;
    }

    public int getId_ingrediente() {
        return id_ingrediente > 0 ? id_ingrediente : id_condimento;
    }

    public void setId_ingrediente(int id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public int getId_condimento() {
        return id_condimento;
    }

    public void setId_condimento(int id_condimento) {
        this.id_condimento = id_condimento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getUnidadesAlacena() {
        return unidades_alacena != null ? unidades_alacena : Collections.emptyList();
    }

    public void setUnidadesAlacena(List<String> unidades) {
        this.unidades_alacena = unidades;
    }

    public List<String> getUnidadesReceta() {
        return unidades_receta != null ? unidades_receta : Collections.emptyList();
    }

    public void setUnidadesReceta(List<String> unidades) {
        this.unidades_receta = unidades;
    }

    @Override
    public String toString() {
        return nombre;
    }
}