package com.example.gameexchanger.activities;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.bumptech.glide.Glide;
import com.example.gameexchanger.adapters.CollectionGamesAdapter;
import com.example.gameexchanger.adapters.WishGamesAdapter;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.databinding.ActivityUserDetailBinding;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;

    // USUARIO QUE RECIBE LA OFERTA DE INTERCAMBIO
    private String user2Name;
    private String user2Id;


    // LISTAS (LISTA DE DESEOS Y COLECCIÓN PERSONAL)
    private ArrayList<String> user2GameCollection;
    private ArrayList<String> user2WishList;

    private WishGamesAdapter wishGamesAdapter;
    private CollectionGamesAdapter collectionGamesAdapter;

    // JUEGO DEL USUARIO 2

    private String user2GameId;
    private String user2GameTitle;
    private String user2GameImageURL;

    // CONECTORES
    private AuthConnector authConnector;
    private GameConnector gameConnector;
/*
ESTA ACTIVITY MUESTRA EL PERFIL DE UN USUARIO CON EL QUE PODEMOS OFRECER UN INTERCAMBIO. SE MOSTRARÁN LOS JUEGOS QUE QUIERE Y LOS
DEMÁS JUEGOS QUE TIENE EN SU COLECCIÓN
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // USUARIO QUE RECIBE LA OFERTA DE INTERCAMBIO
        user2Name = getIntent().getStringExtra("user2Name");
        user2Id = getIntent().getStringExtra("user2Id");

        // LISTAS
        user2GameCollection = getIntent().getStringArrayListExtra("user2GameCollection");
        user2WishList = getIntent().getStringArrayListExtra("user2WishList");

        // JUEGO DEL USUARIO 2 QUE SE ESTÁ MOSTRANDO
        user2GameId = getIntent().getStringExtra("gameId");
        user2GameImageURL = getIntent().getStringExtra("gameImage");
        user2GameTitle = getIntent().getStringExtra("gameTitle");
        Glide.with(this).load(user2GameImageURL).into(binding.ivGame);

        // CONECTORES
        gameConnector = new GameConnector();
        authConnector = new AuthConnector();

        // COLLECTION RECYCLER (JUEGOS QUE QUIERE)
        GridLayoutManager collectionGridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerCollection.setLayoutManager(collectionGridLayoutManager);

        // WISHLIST RECYCLER (JUEGOS QUE TIENE)
        GridLayoutManager whishListGridLayoutManager2 = new GridLayoutManager(this, 2);
        binding.recyclerWhislist.setLayoutManager(whishListGridLayoutManager2);


        binding.tvWishList.setText("A "+user2Name.toUpperCase()+" LE INTERESA");
        binding.tvGameCollection.setText("OTROS JUEGOS DE "+user2Name.toUpperCase());
    }


    // Cargamos todos los juegos en la base de datos cuando se inicia el Fragment mediante el método "getAllGames()"
    @Override
    public void onStart() {
        super.onStart();
        getGamesWishList(user2WishList);
        getGamesCollection(user2GameCollection);
    }

    // Obtiene la lista de juegos que aceptaría el usuario, es decir, la lista de deseos del usuario 2
    public void getGamesWishList(List<String> gameList){
        List<Game> games = new ArrayList<>();
        // Recorremos la lista de juegos del usuario y los comparamos con la colection de juegos en Firebase, si coincide alguno significa que el
        // usuario lo tiene en su lista, por lo que lo añadimos a la List games
        for(String id : gameList){
            gameConnector.getGameById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        // Con los id de juegos que el usuario tiene en su Array de "wishList" buscamos en Firebase los
                        // juegos en la colección Games que coinciden con esos ID, si es así recogemos todos los campos
                        String id = documentSnapshot.getString("id");
                        String genre = documentSnapshot.getString("genre");
                        String image = documentSnapshot.getString("image");
                        String system = documentSnapshot.getString("system");
                        String title = documentSnapshot.getString("title");
                        // Creamos un objeto de la case Game con esos atributos
                        Game game = new Game();
                        game.setId(id);
                        game.setGenre(genre);
                        game.setImage(image);
                        game.setSystem(system);
                        game.setTitle(title);
                        // Los añadidos a la List<Games> que después de pasarsela al adpater éste pintará la información en la recyclerView
                        games.add(game);
                    }

                    wishGamesAdapter = new WishGamesAdapter(UserDetailActivity.this, games, user2Id, user2Name, authConnector.getUserId(), user2GameId, user2GameTitle, user2GameImageURL);
                    binding.recyclerWhislist.setAdapter(wishGamesAdapter);
                }
            });
        }
    }

    // Obtiene la lista de juegos que el usuario 2 tiene en su colección
    public void getGamesCollection(List<String> gameList){
        List<Game> games = new ArrayList<>();
        for(String id : gameList){
            gameConnector.getGameById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String id = documentSnapshot.getString("id");

                        // Si el juego es el mismo que se está mostrando no aparece en la lista de "Otros juegos de este usuario"
                        if(!id.equals(user2GameId)){
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
                    }
                    collectionGamesAdapter = new CollectionGamesAdapter(UserDetailActivity.this, games, user2Id, user2Name);
                    binding.recyclerCollection.setAdapter(collectionGamesAdapter);
                }
            });
        }
    }



}