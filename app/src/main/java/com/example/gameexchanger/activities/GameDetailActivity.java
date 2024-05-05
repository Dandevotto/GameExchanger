package com.example.gameexchanger.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.example.gameexchanger.adapters.UsersAdapter;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.ActivityDetailBinding;
import com.example.gameexchanger.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class GameDetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private String gameId;
    private String gameTitle;
    private String gameSystem;
    private String gameGenre;
    private String gameImageURL;
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
        binding.tvTitleDetail.setText(gameTitle);
        binding.tvGameSystem.setText(gameSystem);
        Glide.with(this).load(gameImageURL).into(binding.imageDetail);

        userConnector = new UserConnector();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.userRecycler.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
    }

    // Método que nos muestra todos los usuarios que tienen ese juego en su colección para cambiarlo
    private void getUsers(){
        Query query = userConnector.getUserStoredGame(gameId);
        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        usersAdapter = new UsersAdapter(users, GameDetailActivity.this, gameImageURL, gameId, gameTitle, gameSystem, gameGenre);
        binding.userRecycler.setAdapter(usersAdapter);
        usersAdapter.startListening();
    }

/*
    public void getUsersWithGame(String gameId) {
        // Referencia a la colección "Users" en Firestore
        CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
                             userCollection = FirebaseFirestore.getInstance().collection("Users");

        // Realizar la consulta
        usersCollection.whereArrayContains("gamesCollection", gameId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Iterar sobre los resultados de la consulta
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtener el ID y el nombre de usuario del usuario
                                //String userId = document.getId();

                                String username = document.getString("username");
                                String userId = document.getString("id");
                                ArrayList<String> gamesCollection = (ArrayList<String>) document.get("gamesCollection");
                                ArrayList<String> wishList = (ArrayList<String>) document.get("wishList");

                                User user = new User();
                                user.setId(userId);
                                user.setUsername(username);
                                user.setGamesCollection(gamesCollection);
                                user.setWishList(wishList);
                                usuarios.add(user);

                            }
                        } else {
                            Log.e(TAG, "Error al obtener usuarios con el juego: ", task.getException());
                        }
                        userAdapters = new UserAdapters(GameDetailActivity.this, usuarios, gameId, gameImageURL, gameTitle, gameSystem, gameGenre);
                        binding.userRecycler.setAdapter(userAdapters);
                    }
                });
    }
    */

}