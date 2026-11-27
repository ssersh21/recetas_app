package com.sergio.proyectov1.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sergio.proyectov1.R;
import com.sergio.proyectov1.auth.login;

public class AdminProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private Button btnLogout;

    public AdminProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tv_admin_name);
        tvEmail = view.findViewById(R.id.tv_admin_email);
        btnLogout = view.findViewById(R.id.btn_admin_logout);

        if (getContext() != null) {
            SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String name = prefs.getString("user_name", "Administrador del Sistema");
            String email = prefs.getString("user_email", "admin@gmail.com");

            tvName.setText(name);
            tvEmail.setText(email);
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> logoutAdmin());
        }
    }

    private void logoutAdmin() {
        if (getContext() != null) {
            SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
        }

        Intent intent = new Intent(getActivity(), login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }

        Toast.makeText(getContext(), "Sesión de administrador cerrada", Toast.LENGTH_SHORT).show();
    }
}
