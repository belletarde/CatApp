package com.app.cat.kevin.thecatapp.model.googleColorApiResponse;

import java.util.List;

public class ImageColorRecognizeResponse {
    private List<ImageProperties> responses;

    public List<ImageProperties> getResponses() {
        return responses;
    }

    public void setResponses(List<ImageProperties> responses) {
        this.responses = responses;
    }
}
