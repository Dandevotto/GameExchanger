package com.example.gameexchanger.connectors;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
    public Task<DocumentSnapshot> getGame(String id){
        return gamesCollection.document(id).get();
    }

    public Query getUserGames(String id){
        return gamesCollection.whereEqualTo("id", id);
    }
}
