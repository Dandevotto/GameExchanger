package com.example.gameexchanger.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gameexchanger.adapters.ExchangesAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.ExchangeConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.FragmentMyExchangesBinding;
import com.example.gameexchanger.model.Exchange;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class MyExchangesFragment extends Fragment {

    FragmentMyExchangesBinding binding;
    ExchangeConnector exchangeConnector;
    AuthConnector authConnector;
    GameConnector gameConnector;
    ExchangesAdapter exchangesAdapter;
    private ProgressDialog dialog;

    public MyExchangesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyExchangesBinding.inflate(inflater, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.myExchangesRecycler.setLayoutManager(linearLayoutManager);

        exchangeConnector = new ExchangeConnector();
        authConnector = new AuthConnector();
        gameConnector = new GameConnector();
        getMyExchanges();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        //getMyExchanges();
    }

    // OBTENEMOS LOS INTERCAMBIOS EN LOS QUE ESTÁ INVOLUCRADO EL USUARIO Y LOS INSERTAMOS EN LA LISTA
    private void getMyExchanges(){
        Query query = exchangeConnector.getAllExchangesByUser(authConnector.getUserId());
        FirestoreRecyclerOptions<Exchange> exchanges = new FirestoreRecyclerOptions.Builder<Exchange>().setQuery(query, Exchange.class).build();
        exchangesAdapter = new ExchangesAdapter(exchanges, getContext());
        exchangesAdapter.notifyDataSetChanged();
        binding.myExchangesRecycler.setAdapter(exchangesAdapter);
        // Escucha en tiempo real los cambios realizados en la base de datos
        exchangesAdapter.startListening();
    }



}