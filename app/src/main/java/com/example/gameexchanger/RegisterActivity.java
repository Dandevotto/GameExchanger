package com.example.gameexchanger;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gameexchanger.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth; // Objeto que maneja el registro del usuario en la BBDD
    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        // BOTÓN "CREAR CUENTA"
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    // Método que comprueba la información del usuario y le registra
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

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = auth.getCurrentUser().getUid(); // Capturamos el UID generado del usuario que se acaba de registrar

                    // Creamos un documento en la colección "Users" que contendrá la información del usuario. El nombre de este documento será el UID del usuario
                    // que hemos recogido anteriormente.

                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);

                    fireStore.collection("Users").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente!!!", Toast.LENGTH_SHORT).show();
                            }else {
                                // Si hay un error al escribir al usuario en la base de datos borramos la cuenta del usuario de Authenticator, para que no se dé
                                // el caso de que se cree la cuenta de usuario pero no tenga datos en la BBDD
                                auth.getCurrentUser().delete();
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