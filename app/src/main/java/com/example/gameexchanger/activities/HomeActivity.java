package com.example.gameexchanger.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.gameexchanger.R;
import com.example.gameexchanger.databinding.ActivityHomeBinding;
import com.example.gameexchanger.fragments.MyExchangesFragment;
import com.example.gameexchanger.fragments.MyWishListFragment;
import com.example.gameexchanger.fragments.HomeFragment;
import com.example.gameexchanger.fragments.MyCollectionFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String fragmentId = getIntent().getStringExtra("fragment");

        if(fragmentId != null){
            // SE HA AÑADIDO UN NUEVO JUEGO A LA COLECCIÓN DEL USUARIO
            if(fragmentId.equals("gamesCollection")) {
                openFragment(new MyCollectionFragment());
                binding.bottomBar.setSelectedItemId(R.id.profilePage); // Cambia el ícono seleccionado
            // SE HA AÑADIDO UN NUEVO JUEGO A LA LISTA DE DESEOS
            }else if(fragmentId.equals("wishList")){
                openFragment(new MyWishListFragment());
                binding.bottomBar.setSelectedItemId(R.id.favoritesPage); // Cambia el ícono seleccionado
            }
        }else{
            // Fragment que se muestra al inicio de la activity
            openFragment(new HomeFragment());
        }

        // Bottom Navigation Bar
        binding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.homePage){
                    openFragment(new HomeFragment());
                }else if(item.getItemId() == R.id.favoritesPage){
                    openFragment(new MyWishListFragment());
                }else if(item.getItemId() == R.id.exchangesPage){
                    openFragment(new MyExchangesFragment());
                }else if(item.getItemId() == R.id.profilePage){
                    openFragment(new MyCollectionFragment());
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