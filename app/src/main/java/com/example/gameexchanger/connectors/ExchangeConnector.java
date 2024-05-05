package com.example.gameexchanger.connectors;

import com.example.gameexchanger.model.Exchange;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ExchangeConnector {

    CollectionReference exchangeCollection;

    public ExchangeConnector(){
        exchangeCollection = FirebaseFirestore.getInstance().collection("Exchanges");
    }

    /*
    // Crea un intercambio entre 2 usuarios
    public Task<Void> createExchange(Exchange exchange){
        return exchangeCollection.document().set(exchange);
    }
    */

    // Crear un Exchange (Propuesta de intercambio) en Firestore
    // Queremos que el atrubuto ID del documento creado tenga el ID que genera
    // Firestore aleatoriamente
    public Task<Void> createExchange(Exchange exchange){
        // Obtenemos el ID del documento que va a asignar Firestore
        String exchangeId = exchangeCollection.document().getId();

        // Lo seteamos al objeto que acabamos de crear
        exchange.setExchangeId(exchangeId);

        // Agregamos el documento a la colección "Exchanges"
        return exchangeCollection.document(exchangeId).set(exchange);
    }

    public Query getAllExchangesByUser(String userId){
        return exchangeCollection.whereArrayContains("usersIds", userId);
    }

    // Actualiza el estado del intercambio propuesto (Aceptado, rechazado, etc.)
    public Task<Void> updateExchangeStatus(String exchangeId, String status) {
        return exchangeCollection.document(exchangeId).update("status", status);
    }

}
