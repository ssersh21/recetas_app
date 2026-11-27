package com.sergio.proyectov1.personalization;

/**
 * Cálculos del módulo 3 (RQF3.6 a RQF3.9). Clase pura de Java, sin dependencias de Android,
 * para poder probarla con una prueba unitaria normal.
 *
 * El orden de los factores y de las metas coincide con el de los arreglos
 * activity_levels y goals_options de res/values/arrays.xml, así que se calcula por posición
 * y no por el texto mostrado (cambiar un texto de la interfaz ya no rompe el cálculo).
 */
public final class NutritionCalculator {

    // RQNF3.2: poco o nada, ligero, moderado, intenso
    private static final double[] ACTIVITY_FACTORS = {1.2, 1.375, 1.55, 1.75};

    // Metas (posición en goals_options)
    public static final int GOAL_LOSE = 0;
    public static final int GOAL_GAIN = 1;
    public static final int GOAL_HEALTHY = 2;
    public static final int GOAL_EXPLORER = 3;

    // RQNF3.9: déficit de 300 a 500 kcal; el superávit queda por definir en los requerimientos
    public static final double DEFICIT_KCAL = 500;
    public static final double SURPLUS_KCAL = 300;

    // Piso de seguridad para no recomendar planes de pérdida de peso demasiado bajos
    public static final double MIN_KCAL_FEMALE = 1200;
    public static final double MIN_KCAL_MALE = 1500;

    // Rangos aceptados en el formulario (RQNF3.1: años, kg y cm)
    public static final int MIN_AGE = 15, MAX_AGE = 100;
    public static final double MIN_WEIGHT = 30, MAX_WEIGHT = 300;
    public static final double MIN_HEIGHT = 100, MAX_HEIGHT = 250;
    public static final int MIN_TIME = 10, MAX_TIME = 300;

    private NutritionCalculator() {}

    public static final class Result {
        public final double tmb;
        public final double tdee;
        public final double dailyTarget;

        Result(double tmb, double tdee, double dailyTarget) {
            this.tmb = tmb;
            this.tdee = tdee;
            this.dailyTarget = dailyTarget;
        }
    }

    /**
     * RQF3.6 y RQF3.7 (unidos): Mifflin-St Jeor.
     * TMB = 10*peso + 6.25*altura - 5*edad + 5 (hombre) o - 161 (mujer).
     */
    public static double bmr(boolean male, double weightKg, double heightCm, int ageYears) {
        double base = (10 * weightKg) + (6.25 * heightCm) - (5 * ageYears);
        return male ? base + 5 : base - 161;
    }

    /** RQF3.8: TDEE = TMB * factor de actividad. */
    public static double tdee(double bmr, int activityIndex) {
        return bmr * activityFactor(activityIndex);
    }

    public static double activityFactor(int activityIndex) {
        if (activityIndex < 0 || activityIndex >= ACTIVITY_FACTORS.length) {
            throw new IllegalArgumentException("Nivel de actividad inválido: " + activityIndex);
        }
        return ACTIVITY_FACTORS[activityIndex];
    }

    /** RQF3.9: ajusta el TDEE según el objetivo. */
    public static double dailyTarget(double tdee, int goalIndex, boolean male) {
        switch (goalIndex) {
            case GOAL_LOSE:
                double floor = male ? MIN_KCAL_MALE : MIN_KCAL_FEMALE;
                return Math.max(tdee - DEFICIT_KCAL, Math.min(floor, tdee));
            case GOAL_GAIN:
                return tdee + SURPLUS_KCAL;
            case GOAL_HEALTHY:
            case GOAL_EXPLORER:
                return tdee; // mantenimiento
            default:
                throw new IllegalArgumentException("Objetivo inválido: " + goalIndex);
        }
    }

    public static Result calculate(boolean male, double weightKg, double heightCm, int ageYears,
                                   int activityIndex, int goalIndex) {
        double bmr = bmr(male, weightKg, heightCm, ageYears);
        double tdee = tdee(bmr, activityIndex);
        return new Result(bmr, tdee, dailyTarget(tdee, goalIndex, male));
    }
}
