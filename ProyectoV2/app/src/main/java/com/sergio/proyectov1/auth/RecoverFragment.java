package com.sergio.proyectov1.auth;

import com.sergio.proyectov1.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecoverFragment extends Fragment {

    public RecoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvBack = view.findViewById(R.id.tv_recover_back_to_login);

        tvBack.setOnClickListener(v -> {
            if (getActivity() instanceof login) {
                ((login) getActivity()).loadFragment(new LoginFragment());
            }
        });
    }
}