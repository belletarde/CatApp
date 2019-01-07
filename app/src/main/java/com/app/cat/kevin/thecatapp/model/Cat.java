package com.app.cat.kevin.thecatapp.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cat {
    private String id, url;
    @SerializedName("breeds")
    private List<Breed> breed;

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
}
