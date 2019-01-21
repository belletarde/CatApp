package com.app.cat.kevin.thecatapp.model.googleColorApiRequest;

import java.util.List;

public class Request {

    RequestImage image;
    List<RequestFeatures> features;

    public Request(RequestImage image, List<RequestFeatures> features) {
        this.image = image;
        this.features = features;
    }

    public RequestImage getImage() {
        return image;
    }

    public void setImage(RequestImage image) {
        this.image = image;
    }
}


