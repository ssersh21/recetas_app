package com.sergio.proyectov1.api;

import com.sergio.proyectov1.models.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {

    @FormUrlEncoded
    @POST("login.php")
    Call<AuthResponse> login(
            @Field("correo") String correo,
            @Field("contrasena") String contrasena
    );
}
