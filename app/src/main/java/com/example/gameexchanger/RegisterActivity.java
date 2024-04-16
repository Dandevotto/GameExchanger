package com.example.gameexchanger;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gameexchanger.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // BOTÓN "CREAR CUENTA"
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();

            }
        });


    }

    private void register(){
        String email = binding.email.getText().toString();
        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();

        // Comprobamos que todos los campos están completos
        if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            // Confirmamos que las contraseñas coinciden
            if(password.equals(confirmPassword)){

            }else{
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}