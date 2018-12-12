package de.bastian.androidproject;

import com.google.gson.annotations.SerializedName;

public class weatherSnow {

    @SerializedName("3h")
    private double snow;

    public weatherSnow(double snow) {
        this.snow = snow;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }
}
