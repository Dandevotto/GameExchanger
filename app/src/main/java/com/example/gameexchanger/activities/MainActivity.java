package com.example.gameexchanger.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //private FirebaseAuth auth;
    private AuthConnector authConnector;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //auth = FirebaseAuth.getInstance();
        authConnector = new AuthConnector();

         dialog = new ProgressDialog(this);
         dialog.setMessage("Cargando....");


        // TEXTO DE REGISTRO
        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // BOTÓN DE LOGIN
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void login(){

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Introduce email y contraseña", Toast.LENGTH_SHORT).show();
        }else{
            dialog.show();
            authConnector.login(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    dialog.dismiss();
                    if(task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Email y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}