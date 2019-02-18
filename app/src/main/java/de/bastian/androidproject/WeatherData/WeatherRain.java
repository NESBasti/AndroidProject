package de.bastian.androidproject.WeatherData;

import com.google.gson.annotations.SerializedName;

public class WeatherRain {
    @SerializedName("3h")
    private double rain;

    public WeatherRain(double rain) {
        this.rain = rain;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}
