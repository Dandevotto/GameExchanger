package com.example.gameexchanger.connectors;

import com.example.gameexchanger.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        map.put("username", user.getUsername());
        return userCollection.document(user.getId()).update(map);
    }

}
