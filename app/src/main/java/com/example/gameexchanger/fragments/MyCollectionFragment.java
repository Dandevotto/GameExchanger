package com.example.gameexchanger.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gameexchanger.activities.AddGameActivity;
import com.example.gameexchanger.adapters.MyCollectionAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.FragmentMyCollectionBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;


public class MyCollectionFragment extends Fragment {

    private FragmentMyCollectionBinding binding;
    private AuthConnector authConnector;
    private GameConnector gameConnector;
    private UserConnector userConnector;
    private List<String> myCollectionGames;
    private MyCollectionAdapter myCollectionAdapter;

    public MyCollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentMyCollectionBinding.inflate(inflater, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerMyCollection.setLayoutManager(gridLayoutManager);

        authConnector = new AuthConnector();
        gameConnector = new GameConnector();
        userConnector = new UserConnector();

        binding.btAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddGameActivity.class);
                intent.putExtra("collectionName", "gamesCollection");
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        getIdGamesFromMyCollection();
    }


    // BUSCA EN LA BBDD EL USUARIO CUYO UID ES IGUAL AL UID DEL USUARIO QUE ESTÁ CONECTADO, "authConnector.getUserId()"
    // UNA VEZ ENCONTRADO ACCEDEMOS AL ARRAY DE SU COLECCIÓN QUE CONTIENE LOS ID DE LOS JUEGOS Y LO GUARDAMOS EN "myCollectionGames"
    // PASAMOS POR PARÁMETROS ESE ARRAY AL MÉTODO "getGames" QUE NOS ENTREGARÁ TODA LA INFORMACIÓN DE ESOS JUEGOS
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

    // EN EL MÉTODO ANTERIOR HEMOS OBTENIDO UNA LISTA CON LOS ID DE LOS JUEGOS QUE EL USUARIO CONECTADO TIENE EN SU COLECCIÓN
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
                    // EL ADAPTADOR "MyCollectionAdapter" LO USAMOS EN 2 SITUACIONES:
                    // 1 - CUANDO VAMOS A OFRECER UN INTERCAMBIO Y SE NOS MUESTRA NUESTRA COLECCIÓN DE JUEGOS PARA ELEGIR EL QUE QUEREMOS OFRECER
                    // 2 - CUANDO QUEREMOS VER NUESTRA COLECCIÓN DE JUEGOS EN LA PESTAÑA DE "Mis juegos"
                    // COMO EN ESTE CASO ES LA SEGUNDA SITUACIÓN Y POR TANTO NO SE TRATA DE UNA OFERTA DE INTERCAMBIO, PASAMOS COMO NULL TODAS LOS PARÁMETROS
                    // DEL CONSTRUCTOR QUE TIENEN QUE VER CON UN INTERCAMBIO
                    // DE ESTA MANERA PODEMOS REUTILIZAR EL MISMO ADAPTADOR CUANDO SE TRATA DE NUESTRA COLECCIÓN DE JUEGOS
                    myCollectionAdapter = new MyCollectionAdapter(getContext(), games, null, null, null, null, null, null);
                    binding.recyclerMyCollection.setAdapter(myCollectionAdapter);
                }
            });
        }
    }



}