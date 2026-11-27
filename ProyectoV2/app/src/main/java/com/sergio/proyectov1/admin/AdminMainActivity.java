package com.sergio.proyectov1.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sergio.proyectov1.R;
import com.sergio.proyectov1.api.ApiHelper;
import com.sergio.proyectov1.core.NotificationsChecker;

import org.json.JSONArray;

public class AdminMainActivity extends AppCompatActivity implements AdminModerationFragment.PendingCountListener {

    private final AdminReportedRecipesFragment reportedFragment = new AdminReportedRecipesFragment();
    private final AdminModerationFragment moderationFragment = new AdminModerationFragment();
    private final AdminAllRecipesFragment allRecipesFragment = new AdminAllRecipesFragment();
    private final AdminProfileFragment profileFragment = new AdminProfileFragment();

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        navigation = findViewById(R.id.admin_bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            loadFragment(reportedFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Avisos (p. ej. "Imagen por revisar") y número de imágenes pendientes en la pestaña
        NotificationsChecker.check(this);
        refreshPendingCount();
    }

    private void refreshPendingCount() {
        ApiHelper.get(this, "moderacion.php?id_admin=" + ApiHelper.userId(this), (ok, message, body) -> {
            if (!ok || body == null || isFinishing()) return;
            try {
                onPendingCount(new JSONArray(body).length());
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void onPendingCount(int count) {
        if (count > 0) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.nav_admin_moderation);
            badge.setNumber(count);
            badge.setVisible(true);
        } else {
            navigation.removeBadge(R.id.nav_admin_moderation);
        }
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_admin_reported) {
                loadFragment(reportedFragment);
                return true;
            } else if (itemId == R.id.nav_admin_moderation) {
                loadFragment(moderationFragment);
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
