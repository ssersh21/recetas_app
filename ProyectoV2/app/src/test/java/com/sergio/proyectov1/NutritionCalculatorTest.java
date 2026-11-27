package com.sergio.proyectov1;

import static org.junit.Assert.assertEquals;

import com.sergio.proyectov1.personalization.NutritionCalculator;

import org.junit.Test;

public class NutritionCalculatorTest {

    @Test
    public void bmrMale() {
        // 10*80 + 6.25*180 - 5*30 + 5 = 1780
        assertEquals(1780.0, NutritionCalculator.bmr(true, 80, 180, 30), 0.0001);
    }

    @Test
    public void bmrFemale() {
        // 10*60 + 6.25*165 - 5*25 - 161 = 1345.25
        assertEquals(1345.25, NutritionCalculator.bmr(false, 60, 165, 25), 0.0001);
    }

    @Test
    public void activityFactors() {
        assertEquals(1.2, NutritionCalculator.activityFactor(0), 0);
        assertEquals(1.375, NutritionCalculator.activityFactor(1), 0);
        assertEquals(1.55, NutritionCalculator.activityFactor(2), 0);
        assertEquals(1.75, NutritionCalculator.activityFactor(3), 0);
    }

    @Test
    public void goalsAdjustTdee() {
        double tdee = 2500;
        assertEquals(2000, NutritionCalculator.dailyTarget(tdee, NutritionCalculator.GOAL_LOSE, true), 0);
        assertEquals(2800, NutritionCalculator.dailyTarget(tdee, NutritionCalculator.GOAL_GAIN, true), 0);
        assertEquals(2500, NutritionCalculator.dailyTarget(tdee, NutritionCalculator.GOAL_HEALTHY, true), 0);
        assertEquals(2500, NutritionCalculator.dailyTarget(tdee, NutritionCalculator.GOAL_EXPLORER, true), 0);
    }

    @Test
    public void loseWeightNeverBelowSafetyFloor() {
        // TDEE 1500 mujer: 1500-500=1000, el piso es 1200
        assertEquals(1200, NutritionCalculator.dailyTarget(1500, NutritionCalculator.GOAL_LOSE, false), 0);
        // Si el propio TDEE ya es menor que el piso, no se recorta más
        assertEquals(1100, NutritionCalculator.dailyTarget(1100, NutritionCalculator.GOAL_LOSE, false), 0);
    }

    @Test
    public void fullCalculation() {
        NutritionCalculator.Result r = NutritionCalculator.calculate(true, 80, 180, 30, 2, 1);
        assertEquals(1780.0, r.tmb, 0.0001);
        assertEquals(1780.0 * 1.55, r.tdee, 0.0001);
        assertEquals(1780.0 * 1.55 + 300, r.dailyTarget, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidActivity() {
        NutritionCalculator.activityFactor(7);
    }
}
