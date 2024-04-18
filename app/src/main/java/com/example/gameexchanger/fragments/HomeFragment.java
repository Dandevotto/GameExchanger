package com.example.gameexchanger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gameexchanger.R;
import com.example.gameexchanger.adapters.GamesAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.FragmentHomeBinding;
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private View view;
    private AuthConnector auth;
    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;
    private GameConnector gameConnector;
    GamesAdapter gamesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.fragment_home, container, false);

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        auth = new AuthConnector();
        gameConnector = new GameConnector();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.gamesList.setLayoutManager(gridLayoutManager);

        return binding.getRoot();
    }

    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        Query query = gameConnector.getAllGames();
        FirestoreRecyclerOptions<Game> games = new FirestoreRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        gamesAdapter = new GamesAdapter(games, getContext());
        binding.gamesList.setAdapter(gamesAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        gamesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        gamesAdapter.stopListening();
    }
}