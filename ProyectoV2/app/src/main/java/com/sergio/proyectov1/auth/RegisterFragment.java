package com.sergio.proyectov1.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.api.VolleySingleton;
import com.sergio.proyectov1.core.MainActivity;
import com.sergio.proyectov1.models.AuthResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText etName, etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.et_reg_name);
        etUsername = view.findViewById(R.id.et_reg_username);
        etEmail = view.findViewById(R.id.et_reg_email);
        etPassword = view.findViewById(R.id.et_reg_password);
        etConfirmPassword = view.findViewById(R.id.et_reg_confirm_password);
        btnRegister = view.findViewById(R.id.btn_register);

        TextView tvBack = view.findViewById(R.id.tv_back_to_login);

        btnRegister.setOnClickListener(v -> registerUser());

        tvBack.setOnClickListener(v -> {
            if (getActivity() instanceof login) {
                ((login) getActivity()).loadFragment(new LoginFragment());
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Ingresa tu nombre");
            etName.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Ingresa tu nombre de usuario");
            etUsername.requestFocus();
            return;
        }

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

        if (password.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);

        String url = VolleySingleton.BASE_URL + "registro.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    if (!isAdded()) return;
                    btnRegister.setEnabled(true);
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
                                editor.putBoolean("isFirstLogin", true);
                                editor.apply();

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("GO_TO_PROFILE", true);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            String msg = (auth != null && auth.getMessage() != null) ? auth.getMessage() : "No se pudo registrar la cuenta";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al procesar la respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (!isAdded()) return;
                    btnRegister.setEnabled(true);
                    String errorMsg = "Error de conexión con el servidor. Verifica que tu servidor local esté activo.";
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
                params.put("nombre", name);
                params.put("nombre_usuario", username);
                params.put("correo", email);
                params.put("contrasena", password);
                params.put("password", password); // Compatibilidad por si el PHP usa 'password'
                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(stringRequest);
    }
}
