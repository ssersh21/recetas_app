package com.sergio.proyectov1.auth;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.sergio.proyectov1.admin.AdminMainActivity;
import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.core.MainActivity;
import com.sergio.proyectov1.models.AuthResponse;
import com.sergio.proyectov1.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        TextView tvRegister = view.findViewById(R.id.tv_go_to_register);
        TextView tvForgot = view.findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            if (getActivity() instanceof login) {
                ((login) getActivity()).loadFragment(new RegisterFragment());
            }
        });

        tvForgot.setOnClickListener(v -> {
            if (getActivity() instanceof login) {
                ((login) getActivity()).loadFragment(new RecoverFragment());
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Ingresa tu correo");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa tu contraseña");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);

        String url = VolleySingleton.BASE_URL + "login.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (!isAdded()) return;
                    btnLogin.setEnabled(true);
                    try {
                        String jsonString = response != null ? response.trim() : "";
                        int firstBrace = jsonString.indexOf('{');
                        int lastBrace = jsonString.lastIndexOf('}');
                        if (firstBrace >= 0 && lastBrace > firstBrace) {
                            jsonString = jsonString.substring(firstBrace, lastBrace + 1);
                        }

                        AuthResponse auth = new Gson().fromJson(jsonString, AuthResponse.class);
                        if (auth != null && auth.isSuccess()) {
                            Toast.makeText(getContext(), auth.getMessage(), Toast.LENGTH_SHORT).show();

                            if (getActivity() != null) {
                                SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                if (auth.getUsuario() != null) {
                                    editor.putInt("user_id", auth.getUsuario().getId_usuario());
                                    editor.putString("user_name", auth.getUsuario().getNombre());
                                    editor.putString("user_email", auth.getUsuario().getCorreo());
                                    editor.putString("username", auth.getUsuario().getNombre_usuario());
                                }

                                boolean isFirstLogin = prefs.getBoolean("isFirstLogin", true);

                                Intent intent;
                                if (auth.getUsuario() != null && auth.getUsuario().isAdmin()) {
                                    intent = new Intent(getActivity(), AdminMainActivity.class);
                                } else {
                                    intent = new Intent(getActivity(), MainActivity.class);
                                    if (isFirstLogin) {
                                        intent.putExtra("GO_TO_PROFILE", true);
                                        editor.putBoolean("isFirstLogin", false);
                                    }
                                }
                                editor.apply();

                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            String msg = (auth != null && auth.getMessage() != null) ? auth.getMessage() : "Correo o contraseña incorrectos";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al procesar la respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (!isAdded()) return;
                    btnLogin.setEnabled(true);
                    String errorMsg = "Error de conexión con " + VolleySingleton.BASE_URL + ". Verifica que XAMPP esté activo.";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorJson = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            int firstBrace = errorJson.indexOf('{');
                            int lastBrace = errorJson.lastIndexOf('}');
                            if (firstBrace >= 0 && lastBrace > firstBrace) {
                                errorJson = errorJson.substring(firstBrace, lastBrace + 1);
                            }
                            AuthResponse errorBody = new Gson().fromJson(errorJson, AuthResponse.class);
                            if (errorBody != null && errorBody.getMessage() != null && !errorBody.getMessage().isEmpty()) {
                                errorMsg = errorBody.getMessage();
                            }
                        } catch (Exception ignored) {}
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("correo", email);
                params.put("contrasena", password);
                params.put("password", password); // Compatibilidad por si el backend PHP lee $_POST['password']
                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(stringRequest);
    }
}
