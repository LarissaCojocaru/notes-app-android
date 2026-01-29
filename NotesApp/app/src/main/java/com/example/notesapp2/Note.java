package com.example.notesapp2;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;


public class Note implements Serializable {
    private int id;
    private String title;
    private String content;
    private String color;
    @SerializedName("is_favorite")
    private int isFavorite;
    // 0 sau 1 (0 = false, 1 = true)

    public Note(int id, String title, String content, String color, int isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
        this.isFavorite = isFavorite;
    }

    // Getters și Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    // Getter boolean
    public boolean isFavorite() {
        return isFavorite == 1;
    }

    // Setter boolean (pentru cod curat în adaptere)
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite ? 1 : 0;
    }
}
