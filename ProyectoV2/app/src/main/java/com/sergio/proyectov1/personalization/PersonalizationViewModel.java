package com.sergio.proyectov1.personalization;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sergio.proyectov1.models.UserProfile;

public class PersonalizationViewModel extends ViewModel {

    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    /** Publica un perfil ya guardado (para mostrarlo al abrir la pantalla). */
    public void setProfile(UserProfile profile) {
        userProfile.setValue(profile);
    }

    /** Calcula TMB, TDEE y meta diaria (RQF3.6 a RQF3.9) y publica el resultado. */
    public void calculateNutrition(UserProfile profile) {
        boolean male = "Hombre".equalsIgnoreCase(profile.getSex());
        NutritionCalculator.Result r = NutritionCalculator.calculate(
                male, profile.getWeight(), profile.getHeight(), profile.getAge(),
                profile.getActivityIndex(), profile.getGoalIndex());
        profile.setTmb(r.tmb);
        profile.setTdee(r.tdee);
        profile.setDailyCalorieTarget(r.dailyTarget);
        userProfile.setValue(profile);
    }
}
