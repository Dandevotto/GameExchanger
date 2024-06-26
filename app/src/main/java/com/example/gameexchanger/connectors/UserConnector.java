package com.example.gameexchanger.connectors;

import com.example.gameexchanger.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserConnector {

    private CollectionReference userCollection;


    public UserConnector(){
        userCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return userCollection.document(id).get();
    }

    // Método que almacena el usuario en la base de datos
    public Task<Void> createUser(User user){
        return userCollection.document(user.getId()).set(user);
    }

    // Método para actualizar la información del usuario
    public Task<Void> updateUser(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("gamesCollection", user.getGamesCollection());
        return userCollection.document(user.getId()).update(map);
    }

    public Query getUserStoredGame(String gameId){
        return userCollection.whereArrayContains("gamesCollection", gameId);
    }


    public Task<Void> removeGameFromUser(String userId, String gameIdToRemove, String collectionName) {
        return userCollection.document(userId).update(collectionName, FieldValue.arrayRemove(gameIdToRemove));
    }

    public Task<Void> addNewGameToUserProfile(String userId, String gameId, String collectionName) {
        return userCollection.document(userId).update(collectionName, FieldValue.arrayUnion(gameId));
    }

}
