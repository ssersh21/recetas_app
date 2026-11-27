package com.sergio.proyectov1.models;

import java.util.List;

/** Respuesta de unidades.php: medidas compatibles de un alimento (configuradas en la BD). */
public class UnitsResponse {

    private int id;
    private String nombre;
    private String contexto;
    private List<String> unidades;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContexto() {
        return contexto;
    }

    public List<String> getUnidades() {
        return unidades;
    }
}
