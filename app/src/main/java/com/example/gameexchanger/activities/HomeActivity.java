package com.example.gameexchanger.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.gameexchanger.R;
import com.example.gameexchanger.databinding.ActivityHomeBinding;
import com.example.gameexchanger.fragments.ExchangesFragment;
import com.example.gameexchanger.fragments.FavFragment;
import com.example.gameexchanger.fragments.HomeFragment;
import com.example.gameexchanger.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fragment que se muestra al inicio de la activity
        openFragment(new HomeFragment());

        // Bottom Navigation Bar
        binding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.homePage){
                    openFragment(new HomeFragment());
                }else if(item.getItemId() == R.id.favoritesPage){
                    openFragment(new FavFragment());
                }else if(item.getItemId() == R.id.exchangesPage){
                    openFragment(new ExchangesFragment());
                }else if(item.getItemId() == R.id.profilePage){
                    openFragment(new ProfileFragment());
                }
                return true;
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}