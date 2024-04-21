package com.example.gameexchanger.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gameexchanger.adapters.CollectionGamesAdapter;
import com.example.gameexchanger.adapters.WishGamesAdapter;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.ActivityUserDetailBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;
    private String username;
    private ArrayList<String> gamesCollection;
    private ArrayList<String> wishList;
    private GameConnector gameConnector;
    private WishGamesAdapter wishGamesAdapter;
    private CollectionGamesAdapter collectionGamesAdapter;
    private String gameImage;
    String mainGameTitle;

/*
ESTA ACTIVITY MUESTRA EL PERFIL DE UN USUARIO CON EL QUE PODEMOS OFRECER UN INTERCAMBIO. SE MOSTRARÁN LOS JUEGOS QUE QUIERE Y LOS
DEMÁS JUEGOS QUE TIENE EN SU COLECCIÓN
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // USER
        username = getIntent().getStringExtra("username");
        gamesCollection = getIntent().getStringArrayListExtra("gamesCollection");
        wishList = getIntent().getStringArrayListExtra("wishList");

        // GAME
        gameImage = getIntent().getStringExtra("gameImage");
        mainGameTitle = getIntent().getStringExtra("gameTitle");
        Glide.with(this).load(gameImage).into(binding.ivGame);

        // CONECTORES
        gameConnector = new GameConnector();

        // COLLECTION RECYCLER (JUEGOS QUE QUIERE)
        GridLayoutManager collectionGridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerCollection.setLayoutManager(collectionGridLayoutManager);

        // WISHLIST RECYCLER (JUEGOS QUE TIENE)
        GridLayoutManager whishListGridLayoutManager2 = new GridLayoutManager(this, 2);
        binding.recyclerWhislist.setLayoutManager(whishListGridLayoutManager2);

        binding.tvGameCollection.setText("COLECCIÓN DE "+username.toUpperCase());
        binding.tvWishList.setText("A "+username.toUpperCase()+" LE INTERESA");
    }


    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        getGamesWishList(wishList);
        getGamesCollection(gamesCollection);
    }

    // Obtiene la lista de juegos que aceptaría el usuario
    public void getGamesWishList(List<String> gameList){
        List<Game> games = new ArrayList<>();
        for(String id : gameList){
            gameConnector.getGame(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        // Recogemos la información del juego sobre el que se pulsó
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
                    wishGamesAdapter = new WishGamesAdapter(UserDetailActivity.this, games, username, mainGameTitle, gameImage);
                    binding.recyclerWhislist.setAdapter(wishGamesAdapter);
                }
            });
        }
    }

    // Obtiene la lista de juegos que el usuario tiene en su colección
    public void getGamesCollection(List<String> gameList){
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
                    collectionGamesAdapter = new CollectionGamesAdapter(UserDetailActivity.this, games, username, mainGameTitle);
                    binding.recyclerCollection.setAdapter(collectionGamesAdapter);
                }
            });
        }
    }







}