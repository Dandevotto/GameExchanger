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
import com.example.gameexchanger.activities.ExchangeFromMyCollectionActivity;
import com.example.gameexchanger.activities.OfferExchangeActivity;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.model.Game;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder>{

    Context ctx;
    List<Game> games;
    String user2Id;
    String user2Name;

    UserConnector userConnector;
    AuthConnector authConnector;

    String user2GameId;
    String user2GameTitle;
    String user2GameImageURL;
    String user2GameSystem;

    public MyCollectionAdapter(Context ctx, List<Game> games, String user2Id, String user2Name, String user2GameId, String user2GameTitle, String user2GameImageURL, String user2GameSystem) {
        this.ctx = ctx;
        this.games = games;
        this.user2Id = user2Id;
        this.user2Name = user2Name;
        this.user2GameId = user2GameId;
        this.user2GameTitle = user2GameTitle;
        this.user2GameImageURL = user2GameImageURL;
        this.user2GameSystem = user2GameSystem;
    }

    @NonNull
    @Override
    public MyCollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new MyCollectionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionAdapter.MyViewHolder holder, int position) {
        holder.tvGameTitle.setText(games.get(position).getTitle());
        holder.tvGameGenre.setText(games.get(position).getGenre());
        holder.tvGameSystem.setText(games.get(position).getSystem());
        Glide.with(ctx).load(games.get(position).getImage()).into(holder.imgGame);

        // SI SE CUMPLE SIGNIFICA QUE ESTAMOS REVISANDO NUESTRA COLECCIÓN DE JUEGOS, NO PROPONIENDO UN INTERCAMBIO, ASÍ QUE MOSTRAMOS
        // EL ICONO X PARA ELIMINAR ESE JUEGO DE NUESTRA COLECCIÓN Y AÑADIMOS SU LÓGICA
        if(user2Id == null){
            holder.deleteGame.setVisibility(View.VISIBLE);
            holder.deleteGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ELIMINAMOS UN JUEGO DE NUESTRA COLECCIÓN
                    dialogDeleteGame(games.get(position).getId(), position);
                }
            });
        }
        holder.gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user2Id != null){
                    // SI SE CUMPLE SIGNIFICA QUE ESTAMOS OFRECIENDO UN JUEGO DE NUESTRA COLECCIÓN
                    // ASÍ QUE MOSTRAMOS EL ALERT DIALOG CORRESPONDIENTE
                    dialogOfferExchange(games.get(position).getId(), games.get(position).getTitle(), games.get(position).getSystem(), games.get(position).getImage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvGameTitle;
        TextView tvGameSystem;
        TextView tvGameGenre;
        ImageView imgGame;
        CardView gameCardView;
        CardView deleteGame;

        public MyViewHolder(@NonNull View view) {
            super(view);

            tvGameTitle = view.findViewById(R.id.tvGameTitle);
            tvGameSystem = view.findViewById(R.id.tvGameSystem);
            tvGameGenre = view.findViewById(R.id.tvGameGenre);
            imgGame = view.findViewById(R.id.imgGame);
            gameCardView = view.findViewById(R.id.gameCardView);
            deleteGame = view.findViewById(R.id.deleteGame);
        }
    }

    // Mostramos un AlertDialog que pregunta al usuario si quiere proponer un intercambio al hacer click
    // sobre un juego en un perfil de usuario.
    public void dialogOfferExchange(String user1GameId, String user1GameTitle, String user1GameSystem, String user1GameImageURL){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("¿CONFIRMAR INTERCAMBIO?")
                .setMessage("Ofreces a "+user2Name+" el "+user1GameTitle+" a cambio del "+user2GameTitle)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ctx, OfferExchangeActivity.class);

                        // INFO DEL JUEGO DEL USUARIO 1 (EL QUE HACE LA OFERTA. ES EL QUE SE SELECCIONA DE LA LISTA ASOCIADA A ESTE ADAPTADOR)
                        intent.putExtra("user1GameId", user1GameId);
                        intent.putExtra("user1GameTitle", user1GameTitle);
                        intent.putExtra("user1GameImageURL",user1GameImageURL);
                        intent.putExtra("user1GameSystem", user1GameSystem);

                        // INFO DEL USUARIO 2 (EL QUE RECIBE LA OFERTA)
                        intent.putExtra("user2Id", user2Id);
                        intent.putExtra("user2Name", user2Name);

                        // INFO DEL JUEGO DEL USUARIO 2 (EL QUE RECIBE LA OFERTA)
                        intent.putExtra("user2GameId", user2GameId);
                        intent.putExtra("user2GameTitle", user2GameTitle);
                        intent.putExtra("user2GameImageURL", user2GameImageURL);

                        ctx.startActivity(intent);
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


    // MÉTODO PARA ELIMINAR UN JUEGO DE NUESTRA COLECCIÓN
    public void dialogDeleteGame(String gameId, int position){
        userConnector = new UserConnector();
        authConnector = new AuthConnector();
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("ELIMINAR JUEGO")
                .setMessage("¿Quieres eliminar este juego de tu colección?")
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // COMO USAMOS EL MÉTODO "removeGameFromUser" TANTO PARA ELIMINAR UN JUEGO DE LA COLECCIÓN DEL USUARIO COMO DE SU LISTA DE
                                // DESEOS, EL TERCER PARÁMETRO QUE PASAMOS ES UN STRING CON EL NOMBRE DE LA COLLECTION DE FIREBASE
                                userConnector.removeGameFromUser(authConnector.getUserId(), gameId, "gamesCollection").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // SI EL JUEGO SE ELIMINÓ CORRECTAMENTE DE LA BASE DE DATOS ACTUALIZAMOS LA LISTA QUE TIENE EL ADAPTADOR
                                        // PARA QUE LA RECYCLERVIEW SE ACTUALICE AUTOMÁTICAMENTE
                                        games.remove(position);
                                        notifyDataSetChanged();
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
