package com.example.gameexchanger.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gameexchanger.R;
import com.example.gameexchanger.activities.HomeActivity;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class AddGameAdapter extends FirestoreRecyclerAdapter<Game, AddGameAdapter.ViewHolder> {

    Context ctx;
    AuthConnector authConnector = new AuthConnector();
    UserConnector userConnector = new UserConnector();
    String collectionName;

    public AddGameAdapter(FirestoreRecyclerOptions<Game> options, Context ctx, String collectionName){
        super(options);
        this.ctx = ctx;
        this.collectionName = collectionName;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Game game) {

        viewHolder.tvGameTitle.setText(game.getTitle());
        viewHolder.tvGameGenre.setText(game.getGenre());
        viewHolder.tvGameSystem.setText(game.getSystem());
        Glide.with(ctx).load(game.getImage()).into(viewHolder.imgGame);

        viewHolder.gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddNewGame(game.getId());
            }
        });
    }

    // Instanciampos la clase ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGameTitle;
        TextView tvGameSystem;
        TextView tvGameGenre;
        ImageView imgGame;
        CardView gameCardView;


        public ViewHolder(View view){
            super(view);

            tvGameTitle = view.findViewById(R.id.tvGameTitle);
            tvGameSystem = view.findViewById(R.id.tvGameSystem);
            tvGameGenre = view.findViewById(R.id.tvGameGenre);
            imgGame = view.findViewById(R.id.imgGame);
            gameCardView = view.findViewById(R.id.gameCardView);

        }
    }

    // ALERT DIALOG QUE SE MUESTRA AL PULSAR SOBRE LA DEL CARDIVIEW DEL JUEGO
    // PIDE CONFIRMACIÓN AL USUARIO DE SI QUIERE AÑADIR EL JUEGO A SU LISTA DE DESEOS O COLECCIÓN DE JUEGOS
    private void dialogAddNewGame(String gameId){

        // EL MENSAJE DEL ALERT DIALOG CAMBIARÁ DEPENDIENDO DE SI LAS MODIFICACIONES SE ESTAN HACIENDO EN LA LISTA DE
        //DESEOS O EN LA COLECCIÓN
        String message;

        if(collectionName.equals("wishList")){
            message = "¿Quieres añadir este juego a tu lista de deseos?";
        }else{
            message = "¿Quieres añadir este juego a tu colección?";
        }

        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("AÑADIR JUEGO")
                .setMessage(message)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // COMO USAMOS EL MÉTODO "addNewGameToUserProfile" TANTO PARA AÑADIR UN JUEGO A LA COLECCIÓN DEL USUARIO COMO A SU LISTA DE
                                // DESEOS, EL TERCER PARÁMETRO QUE PASAMOS ES UN STRING CON EL NOMBRE DE LA COLLECTION DE FIREBASE
                                userConnector.addNewGameToUserProfile(authConnector.getUserId(), gameId, collectionName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // SI EL JUEGO SE AÑADIÓ CORRECTAMENTE A LA BASE DE DATOS DIRIGIMOS AL USUARIO AL FRAGMENT DE SU COLECCIÓN
                                        Intent intent = new Intent(ctx, HomeActivity.class);
                                        intent.putExtra("fragment", collectionName);
                                        ctx.startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}