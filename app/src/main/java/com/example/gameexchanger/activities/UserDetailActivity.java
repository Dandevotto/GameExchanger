package com.example.gameexchanger.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameexchanger.adapters.CollectionGamesAdapter;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.ActivityUserDetailBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;
    private String userId;
    private String username;
    private ArrayList<String> gamesCollection;
    private ArrayList<String> wishList;
    private GameConnector gameConnector;
    private CollectionGamesAdapter collectionGamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        gamesCollection = getIntent().getStringArrayListExtra("gamesCollection");
        wishList = getIntent().getStringArrayListExtra("wishList");

        gameConnector = new GameConnector();
        GridLayoutManager collectionGridLayoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager whishListGridLayoutManager2 = new GridLayoutManager(this, 2);
        binding.recyclerCollection.setLayoutManager(collectionGridLayoutManager);
        binding.recyclerWhislist.setLayoutManager(whishListGridLayoutManager2);

        binding.tvGameCollection.setText("COLECCIÓN DE "+username.toUpperCase());
        binding.tvWishList.setText("A "+username.toUpperCase()+" LE INTERESA");

        this.registerForContextMenu(binding.recyclerCollection);
        this.registerForContextMenu(binding.recyclerWhislist);

    }





    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        //getWishList();
        //getGamesCollection();
        getGames(wishList, binding.recyclerWhislist);
        getGames(gamesCollection, binding.recyclerCollection);
    }

    public void getGames(List<String> gameList, RecyclerView recyclerView){
        List<Game> games = new ArrayList<>();
        for(String id : gameList){
            gameConnector.getGame(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String id = documentSnapshot.getString("id");
                        String genre = documentSnapshot.getString("genre");
                        String image = documentSnapshot.getString("image");
                        String system = documentSnapshot.getString("system");
                        String title = documentSnapshot.getString("title");

                        Game game = new Game();
                        game.setId(id);
                        game.setGenre(genre);
                        game.setImage(image);
                        game.setSystem(system);
                        game.setTitle(title);

                        games.add(game);
                    }
                    collectionGamesAdapter = new CollectionGamesAdapter(UserDetailActivity.this, games);
                    recyclerView.setAdapter(collectionGamesAdapter);
                }
            });
        }
    }

    public void getWishList(){
        List<Game> gameList = new ArrayList<>();
        for(String id : wishList){
            gameConnector.getGame(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String id = documentSnapshot.getString("id");
                        String genre = documentSnapshot.getString("genre");
                        String image = documentSnapshot.getString("image");
                        String system = documentSnapshot.getString("system");
                        String title = documentSnapshot.getString("title");

                        Game game = new Game();
                        game.setId(id);
                        game.setGenre(genre);
                        game.setImage(image);
                        game.setSystem(system);
                        game.setTitle(title);

                        gameList.add(game);
                    }
                    collectionGamesAdapter = new CollectionGamesAdapter(UserDetailActivity.this, gameList);
                    binding.recyclerWhislist.setAdapter(collectionGamesAdapter);
                }
            });
        }
    }

    public void getGamesCollection(){
        List<Game> gameList = new ArrayList<>();
        for(String id : gamesCollection){
            gameConnector.getGame(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String id = documentSnapshot.getString("id");
                        String genre = documentSnapshot.getString("genre");
                        String image = documentSnapshot.getString("image");
                        String system = documentSnapshot.getString("system");
                        String title = documentSnapshot.getString("title");

                        Game game = new Game();
                        game.setId(id);
                        game.setGenre(genre);
                        game.setImage(image);
                        game.setSystem(system);
                        game.setTitle(title);

                        gameList.add(game);
                    }
                    collectionGamesAdapter = new CollectionGamesAdapter(UserDetailActivity.this, gameList);
                    binding.recyclerCollection.setAdapter(collectionGamesAdapter);
                }
            });
        }
    }




}