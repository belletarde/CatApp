package com.app.cat.kevin.thecatapp.model;


import com.google.gson.annotations.SerializedName;

public class FavouriteRequest {
    @SerializedName("image_id")
    private String image_id;
    @SerializedName("sub_id")
    private String sub_id;

    public FavouriteRequest(String image_id, String sub_id) {
        this.image_id = image_id;
        this.sub_id = sub_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }
}
