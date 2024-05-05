package com.example.gameexchanger.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gameexchanger.activities.AddGameActivity;
import com.example.gameexchanger.adapters.MyWishListAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.FragmentMyWishListBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyWishListFragment extends Fragment {

    private FragmentMyWishListBinding binding;
    private MyWishListAdapter myWishListAdapter;
    private UserConnector userConnector;
    private AuthConnector authConnector;
    private List<String> myWishList;
    private GameConnector gameConnector;

    public MyWishListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authConnector = new AuthConnector();
        userConnector = new UserConnector();
        gameConnector = new GameConnector();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyWishListBinding.inflate(inflater, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.myWishListRecycler.setLayoutManager(gridLayoutManager);

        binding.btAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddGameActivity.class);
                intent.putExtra("collectionName", "wishList");
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        getIdGamesFromMyWishList();
    }

    // BUSCA EN LA BBDD EL USUARIO CUYO UID ES IGUAL AL UID DEL USUARIO QUE ESTÁ CONECTADO, "authConnector.getUserId()"
    // UNA VEZ ENCONTRADO ACCEDEMOS AL ARRAY DE SU LISTA DE DESEOS QUE CONTIENE LOS ID DE LOS JUEGOS Y LO GUARDAMOS EN "myWishListGames"
    // PASAMOS POR PARÁMETROS ESE ARRAY AL MÉTODO "getGames" QUE NOS ENTREGARÁ TODA LA INFORMACIÓN DE ESOS JUEGOS
    public void getIdGamesFromMyWishList() {
        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> wishList = (List<String>) documentSnapshot.get("wishList");
                    // Verifica si wishList no es nulo antes de intentar asignarlo a nyWishList
                    if (wishList != null) {
                        // Asigna directamente gamesCollection a myCollectionGames
                        myWishList = wishList;
                        getGames(myWishList);
                    }
                }
            }
        });
    }




    // EN EL MÉTODO ANTERIOR HEMOS OBTENIDO UNA LISTA CON LOS ID DE LOS JUEGOS QUE EL USUARIO CONECTADO TIENE EN SU LISTA DE DESEOS
    // AHORA CON ESA LISTA RECORREMOS LA COLLECTION "Games" DE FIRESTORE PARA QUE NOS ENTREGUE TODA LA INFORMACIÓN (Título, imagen género, etc.) DE LOS
    // JUEGOS QUE COINCIDEN CON ESE ID
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

                    myWishListAdapter = new MyWishListAdapter(getContext(), games);
                    binding.myWishListRecycler.setAdapter(myWishListAdapter);
                }
            });
        }
    }

}