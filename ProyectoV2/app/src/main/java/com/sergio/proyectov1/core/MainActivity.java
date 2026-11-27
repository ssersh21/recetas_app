package com.sergio.proyectov1.core;

import com.sergio.proyectov1.home.HomeFragment;
import com.sergio.proyectov1.pantry.ListStore;
import com.sergio.proyectov1.pantry.StockListFragment;
import com.sergio.proyectov1.personalization.ProfileStore;
import com.sergio.proyectov1.home.PlanFragment;
import com.sergio.proyectov1.personalization.ProfileFragment;
import com.sergio.proyectov1.recipes.CreateRecipeFragment;
import com.sergio.proyectov1.R;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private final HomeFragment homeFragment = new HomeFragment();
    private final StockListFragment pantryFragment = StockListFragment.newInstance(ListStore.PANTRY);
    private final CreateRecipeFragment createRecipeFragment = new CreateRecipeFragment();
    private final PlanFragment planFragment = new PlanFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.bottom_navegation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState != null) return; // Android restaura el fragmento visible

        // Tras el registro (RQF1.2) se pide el perfil, pero solo si aún no lo ha llenado
        boolean needsProfile = getIntent().getBooleanExtra("GO_TO_PROFILE", false)
                && !new ProfileStore(this).exists();
        if (needsProfile) {
            navigation.setSelectedItemId(R.id.nav_perfil); // dispara el listener y carga el perfil
        } else {
            loadFragment(homeFragment);
        }
    }

    public void goHome() {
        navigation.setSelectedItemId(R.id.nav_inicio);
    }

    public void goToProfile() {
        navigation.setSelectedItemId(R.id.nav_perfil);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Avisos sin leer, p. ej. "Tu receta fue revisada y ya está publicada"
        NotificationsChecker.check(this);
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inicio) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_alacena) {
                loadFragment(pantryFragment);
                return true;
            } else if (itemId == R.id.nav_crear) {
                loadFragment(createRecipeFragment);
                return true;
            } else if (itemId == R.id.nav_plan) {
                loadFragment(planFragment);
                return true;
            } else if (itemId == R.id.nav_perfil) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        // Al cambiar de pestaña se descartan pantallas internas (detalle de receta, lista de compras)
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}