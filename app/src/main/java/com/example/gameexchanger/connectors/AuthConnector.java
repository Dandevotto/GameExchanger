package com.example.gameexchanger.connectors;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthConnector {

    private FirebaseAuth auth;

    public AuthConnector(){
        auth = FirebaseAuth.getInstance();
    }

    // Método para hacer login
    public Task<AuthResult> login(String email, String password){
        return auth.signInWithEmailAndPassword(email, password);
    }

    // Obtenemos el ID del usuario logeado
    public String getUserId(){
        if(auth.getCurrentUser() != null){
            return auth.getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    // Obtenemos el email del usuario logeado
    public String getUserEmail(){
        if(auth.getCurrentUser() != null){
            return auth.getCurrentUser().getEmail();
        }else{
            return null;
        }
    }

    public Task<AuthResult> register(String email, String password){
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void deleteAccount(){
        auth.getCurrentUser().delete();
    }

}
