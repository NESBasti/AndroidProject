package de.bastian.androidproject.WeatherData;

import com.google.gson.annotations.SerializedName;

public class WeatherSnow {

    @SerializedName("3h")
    private double snow;

    public WeatherSnow(double snow) {
        this.snow = snow;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }
}
