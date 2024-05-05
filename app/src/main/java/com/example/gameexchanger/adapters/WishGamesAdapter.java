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
import com.example.gameexchanger.activities.OfferExchangeActivity;
import com.example.gameexchanger.model.Game;
import java.util.List;

public class WishGamesAdapter extends RecyclerView.Adapter<WishGamesAdapter.MyViewHolder>{

    Context ctx;
    List<Game> games;

    // USERS
    String user2Id;
    String user2Name;
    String user1Id;


    // JUEGO DEL USUARIO 2
    String user2GameId;
    String user2GameTitle;
    String user2GameImageURL;


    public WishGamesAdapter(Context ctx, List<Game> games, String user2Id, String user2Name, String user1Id, String user2GameId, String user2GameTitle, String user2GameImageURL) {
        this.ctx = ctx;
        this.games = games;
        this.user2Id = user2Id;
        this.user2Name = user2Name;
        this.user1Id = user1Id;
        this.user2GameId = user2GameId;
        this.user2GameTitle = user2GameTitle;
        this.user2GameImageURL = user2GameImageURL;
    }

    @NonNull
    @Override
    public WishGamesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new WishGamesAdapter.MyViewHolder(view);
    }

    // RECOGEMOS VARIABLES "OFFER"
    @Override
    public void onBindViewHolder(@NonNull WishGamesAdapter.MyViewHolder holder, int position) {
        holder.tvGameTitle.setText(games.get(position).getTitle());
        holder.tvGameGenre.setText(games.get(position).getGenre());
        holder.tvGameSystem.setText(games.get(position).getSystem());
        Glide.with(ctx).load(games.get(position).getImage()).into(holder.imgGame);
        holder.gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOfferExchange(games.get(position).getTitle(), games.get(position).getId(), games.get(position).getImage());
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

        public MyViewHolder(@NonNull View view) {
            super(view);

            tvGameTitle = view.findViewById(R.id.tvGameTitle);
            tvGameSystem = view.findViewById(R.id.tvGameSystem);
            tvGameGenre = view.findViewById(R.id.tvGameGenre);
            imgGame = view.findViewById(R.id.imgGame);
            gameCardView = view.findViewById(R.id.gameCardView);

        }
    }

    // Mostramos un AlertDialog que pregunta al usuario si quiere proponer un intercambio al hacer click
    // sobre un juego en un perfil de usuario. Le pasamos por parámetros el ID del juego y del usuario
    // para enviarlo a través del Intent
    public void dialogOfferExchange(String user1GameTitle, String user1GameId, String user1GameImageURL){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("¿OFRECER INTERCAMBIO?")
                .setMessage("Ofrecer a "+user2Name+" el "+user1GameTitle+" a cambio de "+user2GameTitle)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ctx, OfferExchangeActivity.class);

                        // USUARIO AL QUE VAMOS A OFRECER UN INTERCAMBIO
                        intent.putExtra("user2Id", user2Id);
                        intent.putExtra("user2Name", user2Name);

                        // JUEGO QUE VAMOS A OFRECER
                        intent.putExtra("user1GameId", user1GameId);
                        intent.putExtra("user1GameTitle", user1GameTitle);
                        intent.putExtra("user1GameImageURL", user1GameImageURL);


                        // JUEGO POR EL CUAL LO VAMOS A CAMBIAR
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
}