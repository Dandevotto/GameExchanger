package com.example.gameexchanger.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.gameexchanger.adapters.MyCollectionAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.ActivityExchangeFromMyCollectionBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExchangeFromMyCollectionActivity extends AppCompatActivity {

    private ActivityExchangeFromMyCollectionBinding binding;

    // Información que nos traemos del Intent desed "UserDetailActivity"
    private String user2Id;
    private String user2Name;

    private String user2GameId;
    private String user2GameTitle;
    private String user2GameSystem;
    private String user2GameImageURL;


    // CONECTORES
    private GameConnector gameConnector;
    private UserConnector userConnector;
    private AuthConnector authConnector;

    // ADAPTADO
    private MyCollectionAdapter myCollectionAdapter;

    private List<String> myCollectionGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExchangeFromMyCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameConnector = new GameConnector();
        userConnector = new UserConnector();
        authConnector = new AuthConnector();

        user2Id = getIntent().getStringExtra("user2Id");
        user2Name = getIntent().getStringExtra("user2Name");
        user2GameId = getIntent().getStringExtra("user2GameId");
        user2GameTitle = getIntent().getStringExtra("user2GameTitle");
        user2GameSystem = getIntent().getStringExtra("user2GameSystem");
        user2GameImageURL = getIntent().getStringExtra("user2GameImageURL");

        //Enlazamos con las vista
        Glide.with(this).load(user2GameImageURL).into(binding.ivExchangeGame);
        binding.tvTitle.setText("Elige un juego de tu colección");

        // Elementos de la lista
        GridLayoutManager myCollectionGridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerMyGamesCollection.setLayoutManager(myCollectionGridLayoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getIdGamesFromMyCollection();
    }

    public void getGames(List<String> gameList){
        List<Game> games = new ArrayList<>();
        // Recorremos la lista de juegos del usuario y los comparamos con la colection de juegos en Firebase, si coincide alguno significa que el
        // usuario lo tiene en su lista, por lo que lo añadimos a la List games
        for(String id : gameList){
            gameConnector.getGameById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

                    // Pasamos al adaptador la información del Usuario 2 y su juego
                    myCollectionAdapter = new MyCollectionAdapter(ExchangeFromMyCollectionActivity.this, games, user2Id, user2Name, user2GameId, user2GameTitle, user2GameImageURL, user2GameSystem);
                    binding.recyclerMyGamesCollection.setAdapter(myCollectionAdapter);
                }
            });
        }
    }

    public void getIdGamesFromMyCollection() {
        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> gamesCollection = (List<String>) documentSnapshot.get("gamesCollection");
                    // Verifica si gamesCollection no es nulo antes de intentar asignarlo a myCollectionGames
                    if (gamesCollection != null) {
                        // Asigna directamente gamesCollection a myCollectionGames
                        myCollectionGames = gamesCollection;
                        getGames(myCollectionGames);
                    }
                }
            }
        });
    }


}