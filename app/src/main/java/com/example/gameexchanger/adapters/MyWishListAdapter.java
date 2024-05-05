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
import com.example.gameexchanger.activities.GameDetailActivity;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.rpc.context.AttributeContext;

import java.util.List;

public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyViewHolder>{

    Context ctx;
    List<Game> games;
    UserConnector userConnector;
    AuthConnector authConnector;


    public MyWishListAdapter(Context ctx, List<Game> games) {
        this.ctx = ctx;
        this.games = games;
    }

    @NonNull
    @Override
    public MyWishListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_game, parent, false);
        return new MyWishListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWishListAdapter.MyViewHolder holder, int position) {
        holder.tvGameTitle.setText(games.get(position).getTitle());
        holder.tvGameGenre.setText(games.get(position).getGenre());
        holder.tvGameSystem.setText(games.get(position).getSystem());
        Glide.with(ctx).load(games.get(position).getImage()).into(holder.imgGame);

        // HACEMOS VISIBLE LA X PARA QUE EL USUARIO PUEDA ELIMINAR EL JUEGO DE SU LISTA DE DESEOS
        holder.deleteGame.setVisibility(View.VISIBLE);

        holder.deleteGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteGame(games.get(position).getId(), position);
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

    // ALERT DIALOG QUE SE MUESTRA AL PULSAR SOBRE LA X EN LA PARTE SUPERIOR DERACHA DEL CARDIVIEW DEL JUEGO
    // PIDE CONFIRMACIÓN AL USUARIO DE SI QUIERE ELIMINAR EL JUEGO DE SU LISTA DE DESEOS
    public void dialogDeleteGame(String gameId, int position){
        userConnector = new UserConnector();
        authConnector = new AuthConnector();
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("ELIMINAR JUEGO")
                .setMessage("¿Quieres eliminar este juego de tu lista de deseos?")
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userConnector.getUser(authConnector.getUserId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // COMO USAMOS EL MÉTODO "removeGameFromUser" TANTO PARA ELIMINAR UN JUEGO DE LA COLECCIÓN DEL USUARIO COMO DE SU LISTA DE
                                // DESEOS, EL TERCER PARÁMETRO QUE PASAMOS ES UN STRING CON EL NOMBRE DE LA COLLECTION DE FIREBASE
                                userConnector.removeGameFromUser(authConnector.getUserId(), gameId, "wishList").addOnSuccessListener(new OnSuccessListener<Void>() {
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
