package com.example.gameexchanger.adapters;


import android.content.Context;
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
import com.example.gameexchanger.model.Game;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GamesAdapter extends FirestoreRecyclerAdapter<Game, GamesAdapter.ViewHolder> {

    Context ctx;

    public GamesAdapter(FirestoreRecyclerOptions<Game> options, Context ctx){
        super(options);
        this.ctx = ctx;
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
                Intent intent = new Intent(ctx, GameDetailActivity.class);
                intent.putExtra("title", game.getTitle());
                intent.putExtra("genre", game.getGenre());
                intent.putExtra("system", game.getSystem());
                intent.putExtra("image", game.getImage());
                intent.putExtra("id", game.getId());
                ctx.startActivity(intent);
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


}
