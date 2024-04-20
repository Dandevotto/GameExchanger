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

public class CollectionGamesAdapter extends RecyclerView.Adapter<CollectionGamesAdapter.MyViewHolder>{

    Context ctx;
    List<Game> games;
    String userId;
    String username;
    public CollectionGamesAdapter (Context ctx, List<Game> games, String userId, String username){
        this.ctx = ctx;
        this.games = games;
        this.userId= userId;
        this.username = username;
    }

    @NonNull
    @Override
    public CollectionGamesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new CollectionGamesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionGamesAdapter.MyViewHolder holder, int position) {
        holder.tvGameTitle.setText(games.get(position).getTitle());
        holder.tvGameGenre.setText(games.get(position).getGenre());
        holder.tvGameSystem.setText(games.get(position).getSystem());
        Glide.with(ctx).load(games.get(position).getImage()).into(holder.imgGame);
        holder.gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog(games.get(position).getId(), username, userId, games.get(position).getTitle(), games.get(position).getSystem(), games.get(position).getImage());
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
    public void showAlertDialog(String gameId, String username, String userId, String gameTitle, String gameSystem, String gameImage){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("Ofrecer a "+username+" un a cambio de su "+gameTitle+" de "+gameSystem)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ctx, OfferExchangeActivity.class);
                        intent.putExtra("gameId", gameId);
                        intent.putExtra("username", username.toUpperCase());
                        intent.putExtra("userId", userId);
                        intent.putExtra("gameSystem", gameSystem);
                        intent.putExtra("gameTitle", gameTitle);
                        intent.putExtra("gameImage", gameImage);
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