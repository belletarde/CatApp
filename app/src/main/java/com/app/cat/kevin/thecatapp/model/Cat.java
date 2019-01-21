package com.app.cat.kevin.thecatapp.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cat {
    private String id, url, color;
    @SerializedName("breeds")
    private List<Breed> breed;
    long tag;


    private boolean like;

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Breed> getBreed() {
        return breed;
    }

    public void setBreed(List<Breed> breed) {
        this.breed = breed;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setTag(long tag) {
        this.tag = tag;
    }

    public long getTag() {
        return tag;
    }
}
