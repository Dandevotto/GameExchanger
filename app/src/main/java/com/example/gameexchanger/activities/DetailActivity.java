package com.example.gameexchanger.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.gameexchanger.adapters.UsersAdapter;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.ActivityDetailBinding;
import com.example.gameexchanger.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private String gameId;
    private String gameTitle;
    private String gameSystem;
    private String gameGenre;
    private String gameImageURL;
    private GameConnector gameConnector;
    private UsersAdapter usersAdapter;

    private UserConnector userConnector;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameId = getIntent().getStringExtra("id");
        gameTitle = getIntent().getStringExtra("title");
        gameGenre = getIntent().getStringExtra("genre");
        gameSystem = getIntent().getStringExtra("system");
        gameImageURL = getIntent().getStringExtra("image");

        gameConnector = new GameConnector();
        userConnector = new UserConnector();

        binding.tvTitleDetail.setText(gameTitle);
        binding.tvGameSystem.setText(gameSystem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.userRecycler.setLayoutManager(linearLayoutManager);
        Glide.with(this).load(gameImageURL).into(binding.imageDetail);
        getUsers();
        //getGameInfo();
    }


    // Método que nos muestra todos los usuarios que tienen ese juego en su colección para cambiarlo
    private void getGameInfo(){
        gameConnector.getGame(gameId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String titleDetail = documentSnapshot.getString("title");
                    binding.tvTitleDetail.setText(titleDetail);
                }
            }
        });
    }
/*
    private void getUsers(){
        Query query = storeConnector.getStoredGamesById(gameId);
        FirestoreRecyclerOptions<StoredGame> users = new FirestoreRecyclerOptions.Builder<StoredGame>().setQuery(query, StoredGame.class).build();
        usersAdapter = new UsersAdapter(users, DetailActivity.this);
        binding.userRecycler.setAdapter(usersAdapter);
        usersAdapter.startListening();

    }*/

    private void getUsers(){
        Query query = userConnector.getUserStoredGame(gameId);
        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        usersAdapter = new UsersAdapter(users, DetailActivity.this, gameImageURL);
        binding.userRecycler.setAdapter(usersAdapter);
        usersAdapter.startListening();

    }
}