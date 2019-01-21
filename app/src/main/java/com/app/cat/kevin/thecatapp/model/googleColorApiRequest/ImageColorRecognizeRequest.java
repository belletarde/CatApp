package com.app.cat.kevin.thecatapp.model.googleColorApiRequest;


import java.util.List;

public class ImageColorRecognizeRequest {

    List<Request> requests;

    public ImageColorRecognizeRequest(List<Request> request) {
        this.requests = request;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
