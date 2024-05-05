package com.example.gameexchanger.model;

import java.util.ArrayList;

public class Exchange {

    private String exchangeId;
    private String user1Id;
    private String user2Id;
    private String user1GameId;
    private String user2GameId;
    private String status;
    private ArrayList<String> usersIds;

    public Exchange(){

    }

    public Exchange(String exchangeId, String user1Id, String user2Id, String user1GameId, String user2GameId, String status, ArrayList<String> usersIds) {
        this.exchangeId = exchangeId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.user1GameId = user1GameId;
        this.user2GameId = user2GameId;
        this.status = status;
        this.usersIds = usersIds;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getUser1GameId() {
        return user1GameId;
    }

    public void setUser1GameId(String user1GameId) {
        this.user1GameId = user1GameId;
    }

    public String getUser2GameId() {
        return user2GameId;
    }

    public void setUser2GameId(String user2GameId) {
        this.user2GameId = user2GameId;
    }

    public ArrayList<String> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(ArrayList<String> usersIds) {
        this.usersIds = usersIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }
}
