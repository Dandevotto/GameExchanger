package com.example.gameexchanger.activities;

import android.os.Bundle;
import android.view.View;

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
    private String offerImageGameURL;
    private String offerGameTitle;
    private String exchangeGameTitle;
    private String exchangeImageGameURL;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfferExchangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = getIntent().getStringExtra("username");
        exchangeGameTitle = getIntent().getStringExtra("exchangeGameTitle");
        offerGameTitle = getIntent().getStringExtra("offerGameTitle");
        offerImageGameURL = getIntent().getStringExtra("offerImageGameURL");
        exchangeImageGameURL = getIntent().getStringExtra("exchangeImageGameURL");

        binding.tvOffer.setText("Ofreces a "+username+":");
        binding.tvReceiveOffer.setText("A cambio de: ");
        binding.tvOfferGameTitle.setText(offerGameTitle);
        binding.tvExchangeGameTitle.setText(exchangeGameTitle);

        Glide.with(this).load(exchangeImageGameURL).into(binding.ivExchangeGame);
        Glide.with(this).load(offerImageGameURL).into(binding.ivOfferedGame);

        binding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExchange();
            }
        });
    }

    private void confirmExchange(){

    }
}