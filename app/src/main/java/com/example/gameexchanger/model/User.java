package com.example.gameexchanger.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    String username;
    String email;

    ArrayList<String> gamesCollection;
    ArrayList<String> wishList;
    public User(){

    }

    public User(String id, String username, String email, ArrayList<String> gamesCollection, ArrayList<String> wishList) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gamesCollection = gamesCollection;
        this.wishList = wishList;
    }

    public ArrayList<String> getWishList() {
        return wishList;
    }

    public void setWishList(ArrayList<String> wishList) {
        this.wishList = wishList;
    }

    public ArrayList<String> getGamesCollection() {
        return gamesCollection;
    }

    public void setGamesCollection(ArrayList<String> gamesCollection) {
        this.gamesCollection = gamesCollection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
