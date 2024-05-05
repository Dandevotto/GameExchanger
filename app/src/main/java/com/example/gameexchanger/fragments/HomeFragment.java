package com.example.gameexchanger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gameexchanger.R;
import com.example.gameexchanger.adapters.GamesAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.FragmentHomeBinding;
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.math.BigInteger;

public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener{

    private FragmentHomeBinding binding;
    private GameConnector gameConnector;
    private GamesAdapter gamesAdapter;
    private GamesAdapter gamesSearchAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        // CONECTORES
        gameConnector = new GameConnector();
        // LISTA DE JUEGOS
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.gamesList.setLayoutManager(gridLayoutManager);
        // BARRA DE BÚSQUEDA
        binding.searchBar.setOnSearchActionListener(this);

        return binding.getRoot();
    }

    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        getAllGames();
    }

    private void searchGamesByTitle(String gameTitle){
        Query query = gameConnector.getGameByTitle(gameTitle);
        FirestoreRecyclerOptions<Game> games = new FirestoreRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        gamesSearchAdapter = new GamesAdapter(games, getContext());
        gamesSearchAdapter.notifyDataSetChanged();
        binding.gamesList.setAdapter(gamesSearchAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        gamesSearchAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        gamesAdapter.stopListening();
        if(gamesSearchAdapter != null){
            gamesSearchAdapter.stopListening();
        }
    }

    // El usuario hace click en la flecha de la derecha.
    // Volvemos a cargar todos los juegos de la BBDD
    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllGames();
        }
    }

    // El usuario escribe un texto en la barra de búsqueda
    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchGamesByTitle(text.toString().toUpperCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void getAllGames(){
        Query query = gameConnector.getAllGames();
        FirestoreRecyclerOptions<Game> games = new FirestoreRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        gamesAdapter = new GamesAdapter(games, getContext());
        gamesAdapter.notifyDataSetChanged();
        binding.gamesList.setAdapter(gamesAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        gamesAdapter.startListening();
    }


}