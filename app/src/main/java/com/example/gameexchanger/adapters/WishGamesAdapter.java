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
    String exchangeGameTitle;
    String exchangeImageGameURL;
    String username;
    public WishGamesAdapter(Context ctx, List<Game> games, String username, String exchangeGameTitle, String exchangeImageGameURL){
        this.ctx = ctx;
        this.games = games;
        this.username = username;
        this.exchangeGameTitle = exchangeGameTitle;
        this.exchangeImageGameURL = exchangeImageGameURL;
    }

    @NonNull
    @Override
    public WishGamesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new WishGamesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishGamesAdapter.MyViewHolder holder, int position) {
        holder.tvGameTitle.setText(games.get(position).getTitle());
        holder.tvGameGenre.setText(games.get(position).getGenre());
        holder.tvGameSystem.setText(games.get(position).getSystem());
        Glide.with(ctx).load(games.get(position).getImage()).into(holder.imgGame);
        holder.gameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOfferExchange(username, exchangeGameTitle, games.get(position).getTitle(), games.get(position).getImage(), exchangeImageGameURL);
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
    public void dialogOfferExchange(String username, String exchangeGameTitle, String offerGameTtile, String offerImageGameURL, String exchangeImageGameURL){
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("Ofrecer a "+username+" el "+offerGameTtile+" a cambio de "+exchangeGameTitle)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ctx, OfferExchangeActivity.class);

                        intent.putExtra("username", username);
                        intent.putExtra("exchangeGameTitle", exchangeGameTitle);
                        intent.putExtra("offerGameTitle", offerGameTtile);
                        intent.putExtra("offerImageGameURL", offerImageGameURL);
                        intent.putExtra("exchangeImageGameURL", exchangeImageGameURL);

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