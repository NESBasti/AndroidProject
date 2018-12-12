package de.bastian.androidproject;

import java.util.ArrayList;
import java.util.List;

public class WeatherListElement {
    private int dt;
    private weatherMain main;
    private List<weatherWeather> weather = new ArrayList<weatherWeather>();
    private weatherClouds clouds;
    private weatherWind wind;
    private weatherRain rain;
    private weatherSnow snow;
    private weatherSys sys;
    private String dt_text;


    public WeatherListElement(int dt, weatherMain main, List<weatherWeather> weather, weatherClouds clouds, weatherWind wind, weatherRain rain, weatherSnow snow, weatherSys sys, String dt_text) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.sys = sys;
        this.dt_text = dt_text;
    }

    public int getDt() {
        return dt;
    }

    public weatherMain getMain() {
        return main;
    }

    public List<weatherWeather> getWeather() {
        return weather;
    }

    public weatherClouds getClouds() {
        return clouds;
    }

    public weatherWind getWind() {
        return wind;
    }

    public weatherRain getRain() {
        return rain;
    }

    public weatherSnow getSnow() {
        return snow;
    }

    public weatherSys getSys() {
        return sys;
    }

    public String getDt_text() {
        return dt_text;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setMain(weatherMain main) {
        this.main = main;
    }

    public void setWeather(List<weatherWeather> weather) {
        this.weather = weather;
    }

    public void setClouds(weatherClouds clouds) {
        this.clouds = clouds;
    }

    public void setWind(weatherWind wind) {
        this.wind = wind;
    }

    public void setRain(weatherRain rain) {
        this.rain = rain;
    }

    public void setSnow(weatherSnow snow) {
        this.snow = snow;
    }

    public void setSys(weatherSys sys) {
        this.sys = sys;
    }

    public void setDt_text(String dt_text) {
        this.dt_text = dt_text;
    }
}
