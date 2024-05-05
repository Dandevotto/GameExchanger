package com.example.gameexchanger.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.gameexchanger.adapters.AddGameAdapter;
import com.example.gameexchanger.adapters.GamesAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.ActivityAddGameBinding;
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

public class AddGameActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private ActivityAddGameBinding binding;
    private GameConnector gameConnector;
    private AddGameAdapter addGameAdapter;
    private AddGameAdapter gamesSearchAdapter;
    private UserConnector userConnector;
    private AuthConnector authConnector;
    private String collectionName;
    private List<String> gamesCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // CONECTORES
        gameConnector = new GameConnector();
        authConnector = new AuthConnector();
        userConnector = new UserConnector();

        // COLECTION DE FIREBASE EN LA QUE SE VAN A HACER LOS CAMBIOS
        collectionName = getIntent().getStringExtra("collectionName");

        // LISTA DE JUEGOS
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.gamesList.setLayoutManager(gridLayoutManager);
        // BARRA DE BÚSQUEDA
        binding.searchBar.setOnSearchActionListener(this);

    }


    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        getIdGamesFromMyCollection();
    }

    private void searchGamesByTitle(List<String> gameId, String gameTitle){
        Query query = gameConnector.getAllGamesWithoutAnIDAndTitle(gameId, gameTitle);
        FirestoreRecyclerOptions<Game> games = new FirestoreRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        gamesSearchAdapter = new AddGameAdapter(games, this, collectionName);
        gamesSearchAdapter.notifyDataSetChanged();
        binding.gamesList.setAdapter(gamesSearchAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        gamesSearchAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        addGameAdapter.stopListening();
        if(gamesSearchAdapter != null){
            gamesSearchAdapter.stopListening();
        }
    }

    // El usuario hace click en la flecha de la derecha.
    // Volvemos a cargar todos los juegos de la BBDD
    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getIdGamesFromMyCollection();
        }
    }

    // El usuario escribe un texto en la barra de búsqueda
    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchGamesByTitle(gamesCollection, text.toString().toUpperCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void getAllGames(List<String> gamesId){
        Query query = gameConnector.getAllGamesWithoutAnID(gamesId);
        FirestoreRecyclerOptions<Game> games = new FirestoreRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        addGameAdapter = new AddGameAdapter(games, this, collectionName);
        addGameAdapter.notifyDataSetChanged();
        binding.gamesList.setAdapter(addGameAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        addGameAdapter.startListening();
    }

    public void getIdGamesFromMyCollection() {
        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    gamesCollection = (List<String>) documentSnapshot.get(collectionName);
                    // Verifica si gamesCollection no es nulo antes de intentar asignarlo a myCollectionGames
                    if (gamesCollection != null) {
                        if(gamesCollection.isEmpty()){
                            gamesCollection.add("");
                        }
                        // Asigna directamente gamesCollection a myCollectionGames
                        //myCollectionGames = gamesCollection;
                        getAllGames(gamesCollection);
                    }
                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}