package com.app.cat.kevin.thecatapp.model.googleColorApiResponse;

public class ColorsList {
    private ColorRGB color;
    private double  score, pixelFraction;

    public ColorRGB getColor() {
        return color;
    }

    public void setColor(ColorRGB color) {
        this.color = color;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getPixelFraction() {
        return pixelFraction;
    }

    public void setPixelFraction(double pixelFraction) {
        this.pixelFraction = pixelFraction;
    }
}
