package com.example.gameexchanger.connectors;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.List;

public class GameConnector {

    CollectionReference gamesCollection;

    public GameConnector(){
        gamesCollection = FirebaseFirestore.getInstance().collection("Games");
    }

    // Petición a la base de datos para que entregue todos los juegos (Colección "Games") ordenados alfabéticamente
    public Query getAllGames(){
        return gamesCollection.orderBy("title", Query.Direction.DESCENDING);
    }

    // Obtenemos un juego en concreto a través de su ID
    public Task<DocumentSnapshot> getGameById(String id){
        return gamesCollection.document(id).get();
    }

    public Query getUserGames(String id){
        return gamesCollection.whereEqualTo("id", id);
    }

    // OBTIENE LOS JUEGOS QUE COINCIDEN CON UN TÍTULO -> PARA LA BARRA DE BÚSQUEDA
    public Query getGameByTitle(String gameTitle){
        return gamesCollection.orderBy("title").startAt(gameTitle).endAt(gameTitle+'\uf8ff');
    }

    // NOS TRAEMOS DE LA BBDD TODOS LOS JUEGOS CUYO ID NO ESTÁ INCLUIDO EN LA LISTA QUE PASAMOS POR PARÁMETRO
    public Query getAllGamesWithoutAnID(List<String> ids){
        return gamesCollection.whereNotIn("id", ids);
    }

    public Query getAllGamesWithoutAnIDAndTitle(List<String> ids, String gameTitle){
        return gamesCollection.whereNotIn("id", ids).orderBy("title").startAt(gameTitle).endAt(gameTitle+'\uf8ff');
    }

    // NOS TRAEMOS DE LA BBDD TODOS LOS JUEGOS QUE CUYO ID ESTÁ INCLUIDO EN LA LISTA QUE PASAMOS POR PARÁMETRO
    public Query getAllGamesWithAnID(List<String> ids){
        return gamesCollection.whereIn("id", ids);
    }
}

