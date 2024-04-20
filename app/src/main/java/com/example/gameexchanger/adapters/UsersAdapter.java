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
import com.example.gameexchanger.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.lang.reflect.Array;

public class UsersAdapter extends FirestoreRecyclerAdapter<User, UsersAdapter.ViewHolder> {

    Context ctx;

    public UsersAdapter(FirestoreRecyclerOptions<User> options, Context ctx){
        super(options);
        this.ctx = ctx;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull User user) {

        viewHolder.tvUsername.setText(user.getUsername());

        viewHolder.userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, UserDetailActivity.class);
                intent.putExtra("username", user.getUsername());
                intent.putStringArrayListExtra("gamesCollection", user.getGamesCollection());
                intent.putStringArrayListExtra("wishList", user.getWishList());
                intent.putExtra("userId", user.getId());
                ctx.startActivity(intent);

            }
        });
    }

    // Instanciampos la clase ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user, parent, false);
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

