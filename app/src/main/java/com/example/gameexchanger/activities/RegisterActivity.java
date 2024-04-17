package com.example.gameexchanger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.databinding.ActivityRegisterBinding;
import com.example.gameexchanger.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthConnector auth;
    private UserConnector userConnector;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = new AuthConnector();
        userConnector = new UserConnector();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando....");

        // BOTÓN "CREAR CUENTA"
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    // Método que comprueba la información del usuario y crea una cuenta en Authenticator
    private void createAccount(){
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
                if(password.length() >= 8){
                    newUser(email, password, username);
                }else{
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener como mínimo 8 carácteres", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para crear un usuario
    private void newUser(String email, String password, String username){
        dialog.show();
        auth.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    String userId = auth.getUserId(); // Capturamos el UID generado del usuario que se acaba de registrar

                    // Creamos un documento en la colección "Users" que contendrá la información del usuario. El nombre de este documento será el UID del usuario
                    // que hemos recogido anteriormente.

                    User user = new User();
                    user.setId(userId);
                    user.setUsername(username);
                    user.setEmail(email);

                    userConnector.createUser(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                // Para que después de registrarse el usuario no pueda volver atrás limpiamos el historial de pantallas
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            }else {
                                // Si hay un error al escribir al usuario en la base de datos borramos la cuenta del usuario de Authenticator, para que no se dé
                                // el caso de que se cree la cuenta de usuario pero no tenga datos en la BBDD
                                auth.deleteAccount();
                                Toast.makeText(RegisterActivity.this, "Ocurrió un problema al intentar registrarte", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Ocurrió un problema al intentar registrarte", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}