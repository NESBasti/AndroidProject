package de.bastian.androidproject;

import com.google.gson.annotations.SerializedName;

public class weatherRain {
    @SerializedName("3h")
    private double rain;

    public weatherRain(double rain) {
        this.rain = rain;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}
