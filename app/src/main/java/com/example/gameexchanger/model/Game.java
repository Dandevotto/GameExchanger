package com.example.gameexchanger.model;

public class Game {

    private String id;
    private String title;
    private String system;
    private String image;
    private String genre;

    public Game(){

    }

    public Game(String id, String title, String system, String image, String genre) {
        this.id = id;
        this.title = title;
        this.system = system;
        this.image = image;
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
