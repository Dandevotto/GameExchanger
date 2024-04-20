package com.example.gameexchanger.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gameexchanger.R;
import com.example.gameexchanger.databinding.ActivityOfferExchangeBinding;

public class OfferExchangeActivity extends AppCompatActivity {

    private ActivityOfferExchangeBinding binding;
    private String userId;
    private String gameId;
    private String gameSystem;
    private String gameImage;
    private String gameTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfferExchangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");
        gameId = getIntent().getStringExtra("gameId");
        gameSystem = getIntent().getStringExtra("gameSystem");
        gameImage = getIntent().getStringExtra("gameImage");
        gameTitle = getIntent().getStringExtra("gameTitle");


        binding.tvGameSystem.setText(gameSystem);
        binding.tvActivityTitle.setText("¿Qué juego de tu colección le vas a ofrecer a "+userId+" a cambio de su "+gameTitle);
        Glide.with(this).load(gameImage).into(binding.imageDetail);
    }
}