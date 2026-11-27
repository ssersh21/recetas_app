package com.sergio.proyectov1.difficulty;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class DifficultyCalculator {

    // Valores que usa el servidor
    public static final String PRINCIPIANTE = "principiante";
    public static final String INTERMEDIO = "intermedio";
    public static final String AVANZADO = "avanzado";

    // Puntos por técnica según su nivel
    private static final int PUNTOS_BASICA = 1;
    private static final int PUNTOS_INTERMEDIA = 3;
    private static final int PUNTOS_AVANZADA = 6;

    // Umbrales del puntaje total
    private static final int UMBRAL_INTERMEDIO = 6;
    private static final int UMBRAL_AVANZADO = 15;

    private DifficultyCalculator() {
    }

    public static final class Result {
        public final String nivel;
        public final int puntaje;
        public final int puntosPasos;
        public final int totalPasos;
        public final List<TechniqueDictionary.Technique> tecnicas;

        Result(String nivel, int puntaje, int puntosPasos, int totalPasos,
               List<TechniqueDictionary.Technique> tecnicas) {
            this.nivel = nivel;
            this.puntaje = puntaje;
            this.puntosPasos = puntosPasos;
            this.totalPasos = totalPasos;
            this.tecnicas = tecnicas;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(nivel).append(" (puntaje ").append(puntaje)
                    .append(", ").append(totalPasos).append(" pasos = ")
                    .append(puntosPasos).append(" pts)");
            for (TechniqueDictionary.Technique t : tecnicas) {
                sb.append("\n  - ").append(t.nombre)
                        .append(" [nivel ").append(t.nivel).append("]");
            }
            return sb.toString();
        }
    }

    /** @param pasos texto de cada paso de la receta (los vacíos se ignoran). */
    public static Result calcular(List<String> pasos) {
        List<String> validos = new ArrayList<>();
        StringBuilder todo = new StringBuilder();

        if (pasos != null) {
            for (String paso : pasos) {
                if (paso != null && !paso.trim().isEmpty()) {
                    validos.add(paso);
                    todo.append(' ').append(paso);
                }
            }
        }

        String texto = normalizar(todo.toString());

        List<TechniqueDictionary.Technique> detectadas = new ArrayList<>();
        int maxNivel = 0;
        int puntosTecnicas = 0;

        for (TechniqueDictionary.Technique t : TechniqueDictionary.getAll()) {
            if (aparece(texto, t)) {
                detectadas.add(t);
                maxNivel = Math.max(maxNivel, t.nivel);
                puntosTecnicas += puntosDeNivel(t.nivel);
            }
        }

        int puntosPasos = puntosPorCantidadDePasos(validos.size());
        int puntaje = puntosTecnicas + puntosPasos;

        String nivel;
        if (maxNivel == TechniqueDictionary.AVANZADA
                || puntaje >= UMBRAL_AVANZADO) {
            nivel = AVANZADO;
        } else if (maxNivel == TechniqueDictionary.INTERMEDIA
                || puntaje >= UMBRAL_INTERMEDIO) {
            nivel = INTERMEDIO;
        } else {
            nivel = PRINCIPIANTE;
        }

        return new Result(nivel, puntaje, puntosPasos, validos.size(), detectadas);
    }

    private static int puntosDeNivel(int nivel) {
        switch (nivel) {
            case TechniqueDictionary.AVANZADA:
                return PUNTOS_AVANZADA;
            case TechniqueDictionary.INTERMEDIA:
                return PUNTOS_INTERMEDIA;
            default:
                return PUNTOS_BASICA;
        }
    }

    private static int puntosPorCantidadDePasos(int pasos) {
        if (pasos >= 13) return 5;
        if (pasos >= 9) return 3;
        if (pasos >= 6) return 1;
        return 0;
    }

    private static boolean aparece(String texto, TechniqueDictionary.Technique t) {
        for (String clave : t.palabrasClave) {
            boolean exacta = clave.endsWith("!");
            String k = normalizar(exacta
                    ? clave.substring(0, clave.length() - 1)
                    : clave).trim();
            if (k.isEmpty()) continue;
            // La clave debe empezar al inicio de una palabra
            // (y terminarla si es una palabra exacta)
            Pattern p = Pattern.compile("(?<![a-z0-9])" + Pattern.quote(k)
                    + (exacta ? "(?![a-z0-9])" : ""));
            if (p.matcher(texto).find()) {
                return true;
            }
        }
        return false;
    }

    static String normalizar(String s) {
        String n = Normalizer.normalize(s.toLowerCase(java.util.Locale.ROOT),
                Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}+", "");
        n = n.replaceAll("[^a-z0-9]+", " ");
        return " " + n.trim() + " ";
    }
}