package com.example.gameexchanger.connectors;

import com.google.firebase.firestore.CollectionReference;
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
}
