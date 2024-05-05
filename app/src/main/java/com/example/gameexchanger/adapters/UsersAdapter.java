package com.example.gameexchanger.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gameexchanger.R;
import com.example.gameexchanger.activities.UserDetailActivity;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.rpc.context.AttributeContext;

import org.checkerframework.checker.units.qual.A;

import java.util.Objects;

public class UsersAdapter extends FirestoreRecyclerAdapter<User, UsersAdapter.ViewHolder> {

    Context ctx;
    String gameImage;
    String gameId;
    String gameTitle;
    String gameSystem;
    String gameGenre;
    AuthConnector authConnector;


    public UsersAdapter(FirestoreRecyclerOptions<User> options, Context ctx, String gameImage, String gameId, String gameTitle, String gameSystem, String gameGenre){
        super(options);
        this.ctx = ctx;
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.gameImage = gameImage;
        this.gameSystem =gameSystem;
        this.gameGenre = gameGenre;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull User user) {

        // OCULTAMOS LA CELDA DE USUARIO SI SE TRATA DEL USUARIO QUE ESTÁ CONECTADO NAVEGANDO
        if(user.getId().equals(authConnector.getUserId())){
            viewHolder.userCardView.setVisibility(View.GONE);
        }else{
            viewHolder.tvUsername.setText(user.getUsername());
        }

        viewHolder.userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, UserDetailActivity.class);

                // GAME
                intent.putExtra("gameId", gameId);
                intent.putExtra("gameImage", gameImage);
                intent.putExtra("gameTitle", gameTitle);
                intent.putExtra("gameSystem", gameSystem);
                intent.putExtra("GameGenre", gameGenre);

                // INFO DEL USUARIO 2 (EL QUE VA A RECIBIR LA POSIBLE OFERTA)
                intent.putExtra("user2Id", user.getId());
                intent.putExtra("user2Name", user.getUsername());

                // Array con los ID de los juegos que el usuario tiene en su colección
                intent.putStringArrayListExtra("user2GameCollection", user.getGamesCollection());

                // Array con los ID de los juegos que el usuario está interesado en tener
                intent.putStringArrayListExtra("user2WishList", user.getWishList());
                ctx.startActivity(intent);

            }
        });
    }

    // Instanciampos la clase ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user, parent, false);
        authConnector = new AuthConnector();
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        CardView userCardView;


        public ViewHolder(View view){
            super(view);

            tvUsername = view.findViewById(R.id.tvUserName);
            userCardView = view.findViewById(R.id.userCardView);

        }
    }
}

