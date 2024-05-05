package com.example.gameexchanger.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gameexchanger.R;
import com.example.gameexchanger.connectors.AuthConnector;
import com.example.gameexchanger.connectors.ExchangeConnector;
import com.example.gameexchanger.connectors.GameConnector;
import com.example.gameexchanger.connectors.UserConnector;
import com.example.gameexchanger.model.Exchange;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ExchangesAdapter extends FirestoreRecyclerAdapter<Exchange, ExchangesAdapter.ViewHolder> {

    Context ctx;
    GameConnector gameConnector;
    UserConnector userConnector;
    AuthConnector authConnector;
    ExchangeConnector exchangeConnector;


    public ExchangesAdapter(FirestoreRecyclerOptions<Exchange> options, Context ctx){
        super(options);
        this.ctx = ctx;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Exchange exchange) {

        String user1GameId = exchange.getUser1GameId();
        String user2GameId = exchange.getUser2GameId();
        String user1Id = exchange.getUser1Id();
        String user2Id = exchange.getUser2Id();
        String connectedUser = authConnector.getUserId();

        // GAME 1
        getGame1Info(user1GameId, user1Id, viewHolder);
        Glide.with(ctx).load(R.drawable.ic_green_check).into(viewHolder.user1StatusImage);
        viewHolder.tvUser1Status.setText("Oferta");


        // GAME 2
        getGame2Info(user2GameId, user2Id, viewHolder);

        if(exchange.getStatus() != null){
            if (exchange.getStatus().equals("pendiente")) {
                Glide.with(ctx).load(R.drawable.ic_orange_clock).into(viewHolder.user2StatusImage);
                viewHolder.tvUser2Status.setText("Pendiente");
                viewHolder.tvUser2Status.setTextColor(ContextCompat.getColor(ctx, R.color.orange));
            }else if(exchange.getStatus().equals("rechazado")){
                Glide.with(ctx).load(R.drawable.ic_red_cancel).into(viewHolder.user2StatusImage);
                viewHolder.tvUser2Status.setText("Rechazado");
                viewHolder.tvUser2Status.setTextColor(ContextCompat.getColor(ctx, R.color.red));
            }else if (exchange.getStatus().equals("aceptado")) {
                Glide.with(ctx).load(R.drawable.ic_green_check).into(viewHolder.user2StatusImage);
                viewHolder.tvUser2Status.setText("Aceptado");
                viewHolder.tvUser2Status.setTextColor(ContextCompat.getColor(ctx, R.color.green));
            }
        }

        // SI EL USUARIO CONECTADO REPRESENTA AL USER2 DEL INTERCAMBIO, ES DECIR, ES EL USUARIO QUE RECIBE LA
        // OFERTA, ENTONCES SE MOSTRARÁN LOS BOTONES PARA ACEPTAR O RECHAZAR LA OFERTA. UNA VEZ ACEPTADA O RECHAZADA,
        // ES DECIR, EL STATUS ES DIFERENTE DE "PENDIENTE" LOS BOTONES DESAPARECERÁN
        if(user2Id.equals(connectedUser) && exchange.getStatus().equals("pendiente")){
            viewHolder.cardViewExchangeStatus.setVisibility(View.VISIBLE);
        }

        // ACEPTAR LA PROPUESTA DE INTERCAMBIO QUE NOS HAN HECHO
        viewHolder.cardViewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiamos el estado del Exchange en la BBDD a "Aceptado"
                if(exchange.getExchangeId() != null){
                    dialogDeleteGame(exchange.getExchangeId(), "aceptado");
                }
            }
        });

        // RECHAZAR LA PROPUESTA DE INTERCAMBIO QUE NOS HAN HECHO
        viewHolder.cardViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiamos el estado del Exchange en la BBDD a "Aceptado"
                if(exchange.getExchangeId() != null){
                    dialogDeleteGame(exchange.getExchangeId(), "rechazado");
                }
            }
        });
    }

    // Instanciampos la clase ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_exchange, parent, false);
        gameConnector = new GameConnector();
        userConnector = new UserConnector();
        authConnector = new AuthConnector();
        exchangeConnector = new ExchangeConnector();
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName1;
        TextView tvGameTitle1;
        TextView tvGameSystem1;
        ImageView imgGame1;
        TextView tvUserName2;
        TextView tvGameTitle2;
        TextView tvGameSystem2;
        ImageView imgGame2;
        ImageView user1StatusImage;
        ImageView user2StatusImage;
        TextView tvUser1Status;
        TextView tvUser2Status;
        ConstraintLayout cardViewAccept;
        ConstraintLayout cardViewCancel;
        CardView cardViewExchangeStatus;


        public ViewHolder(View view){
            super(view);

            tvUserName1 = view.findViewById(R.id.tvUserName1);
            tvGameTitle1 = view.findViewById(R.id.tvGameTitle1);
            tvGameSystem1 = view.findViewById(R.id.tvGameSystem1);
            imgGame1 = view.findViewById(R.id.imgGame1);
            tvUserName2 = view.findViewById(R.id.tvUserName2);
            tvGameTitle2 = view.findViewById(R.id.tvGameTitle2);
            tvGameSystem2 = view.findViewById(R.id.tvGameSystem2);
            imgGame2 = view.findViewById(R.id.imgGame2);
            user1StatusImage = view.findViewById(R.id.user1StatusImage);
            user2StatusImage = view.findViewById(R.id.user2StatusImage);
            tvUser1Status = view.findViewById(R.id.tvUser1Status);
            tvUser2Status = view.findViewById(R.id.tvUser2Status);
            cardViewAccept =  view.findViewById(R.id.cardViewAccept);
            cardViewCancel =  view.findViewById(R.id.cardViewCancel);
            cardViewExchangeStatus =  view.findViewById(R.id.cardViewExchangeStatus);

        }
    }

    // OBTENEMOS TODA LA INFORMACIÓN DEL JUEGO 1 APARTIR DE SU ID Y EL USERNAME DEL DUEÑO DE ESE JUEGO
    private void getGame1Info(String gameId, String userId, ViewHolder viewHolder){
        gameConnector.getGameById(gameId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String gameTitle = documentSnapshot.getString("title");
                    String imageURL = documentSnapshot.getString("image");
                    String system = documentSnapshot.getString("system");
                    viewHolder.tvGameTitle1.setText(gameTitle);
                    viewHolder.tvGameSystem1.setText(system);
                    Glide.with(ctx).load(imageURL).into(viewHolder.imgGame1);
                }
            }
        });

        userConnector.getUser(userId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userName1 = documentSnapshot.getString("username");
                    viewHolder.tvUserName1.setText(userName1);
                }
            }
        });
    }

    // OBTENEMOS TODA LA INFORMACIÓN DEL JUEGO 2 APARTIR DE SU ID Y EL USERNAME DEL DUEÑO DE ESE JUEGO
    private void getGame2Info(String gameId, String userId, ViewHolder viewHolder){
        gameConnector.getGameById(gameId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String gameTitle = documentSnapshot.getString("title");
                    String imageURL = documentSnapshot.getString("image");
                    String system = documentSnapshot.getString("system");
                    viewHolder.tvGameTitle2.setText(gameTitle);
                    viewHolder.tvGameSystem2.setText(system);
                    Glide.with(ctx).load(imageURL).into(viewHolder.imgGame2);
                }
            }
        });

        userConnector.getUser(userId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userName2 = documentSnapshot.getString("username");
                    viewHolder.tvUserName2.setText(userName2);
                }
            }
        });
    }

    // ALERT DIALOG QUE SE INICIA AL PULSAR SOBRE "ACEPTAR" O "RECHAZAR" EL INTERCAMBIO
    // PARA QUE EL USUARIO CONFIRME LA ACCIÓN
    public void dialogDeleteGame(String exchangeId, String status){
        userConnector = new UserConnector();
        authConnector = new AuthConnector();
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle("ACEPTAR INTERCAMBIO")
                .setMessage("¿Quieres aceptar esta propuesta de intercambio?")
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exchangeConnector.updateExchangeStatus(exchangeId, status);
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
