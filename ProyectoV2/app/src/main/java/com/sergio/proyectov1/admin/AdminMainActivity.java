package com.sergio.proyectov1.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sergio.proyectov1.R;

public class AdminMainActivity extends AppCompatActivity {

    private final AdminReportedRecipesFragment reportedFragment = new AdminReportedRecipesFragment();
    private final AdminAllRecipesFragment allRecipesFragment = new AdminAllRecipesFragment();
    private final AdminProfileFragment profileFragment = new AdminProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        BottomNavigationView navigation = findViewById(R.id.admin_bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            loadFragment(reportedFragment);
        }
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_admin_reported) {
                loadFragment(reportedFragment);
                return true;
            } else if (itemId == R.id.nav_admin_all_recipes) {
                loadFragment(allRecipesFragment);
                return true;
            } else if (itemId == R.id.nav_admin_profile) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_frame_container, fragment);
        transaction.commit();
    }
}
