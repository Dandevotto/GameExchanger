package com.example.gameexchanger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.ExchangeConnector;
import com.example.gameexchanger.databinding.ActivityOfferExchangeBinding;
import com.example.gameexchanger.model.Exchange;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;

public class OfferExchangeActivity extends AppCompatActivity {

    private ActivityOfferExchangeBinding binding;


    // USUARIO QUE RECIBE LA OFERTA
    private String user2Id;
    private String user2Name;


    // JUEGO QUE SE OFRECE PARA CAMBIAR
    private String user1GameImageURL;
    private String user1GameTitle;
    private String user1GameId;


    // JUEGO QUE SE QUIERE CAMBIAR
    private String user2GameTitle;
    private String user2GameImageURL;
    private String user2GameId;

    // CONECTORES
    private AuthConnector authConnector;
    private ExchangeConnector exchangeConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfferExchangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Conector
        exchangeConnector = new ExchangeConnector();
        authConnector = new AuthConnector();

        // Datos que traemos desde el Intent en UserDetailActivity
        user2Id = getIntent().getStringExtra("user2Id");
        user2Name =  getIntent().getStringExtra("user2Name");


        user2GameTitle = getIntent().getStringExtra("user2GameTitle");
        user2GameImageURL = getIntent().getStringExtra("user2GameImageURL");
        user2GameId = getIntent().getStringExtra("user2GameId");


        user1GameTitle = getIntent().getStringExtra("user1GameTitle");
        user1GameImageURL = getIntent().getStringExtra("user1GameImageURL");
        user1GameId = getIntent().getStringExtra("user1GameId");

        // Vistas
        binding.tvOffer.setText("Ofreces a "+user2Name+":");
        binding.tvReceiveOffer.setText("A cambio de: ");
        binding.tvOfferGameTitle.setText(user1GameTitle);
        binding.tvExchangeGameTitle.setText(user2GameTitle);

        Glide.with(this).load(user2GameImageURL).into(binding.ivExchangeGame);
        Glide.with(this).load(user1GameImageURL).into(binding.ivOfferedGame);

        // Botón de confirmación del intercambio
        binding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExchange();
            }
        });
    }

    private void confirmExchange(){

        // Creamos un Objeto de la clase "Exchange" y le incorporamos la información de ambos usuarios y juegos
        Exchange exchange = new Exchange();
        String user1Id = authConnector.getUserId(); // Guardamos el ID del usuario conectado para solo consultar la BBDD una vez
        exchange.setUser1Id(user1Id);
        exchange.setUser2Id(user2Id);
        exchange.setUser1GameId(user1GameId);
        exchange.setUser2GameId(user2GameId);
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(user1Id);
        userIds.add(user2Id);
        exchange.setUsersIds(userIds);
        exchange.setStatus("pendiente");

        exchangeConnector.createExchange(exchange).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(OfferExchangeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(OfferExchangeActivity.this, "Intercambio realizado correctamente!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OfferExchangeActivity.this, "Ocurrió un problema al confirmar el intercambio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}