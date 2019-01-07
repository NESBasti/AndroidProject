package de.bastian.androidproject;

import java.util.List;

public class WeatherListElement {
    private int dt;
    private weatherMain main;
    private List<weatherWeather> weather;
    private weatherClouds clouds;
    private weatherWind wind;
    private weatherRain rain;
    private weatherSnow snow;
    private forecastSys sys;
    private String dt_txt;


    public WeatherListElement(int dt, weatherMain main, List<weatherWeather> weather, weatherClouds clouds, weatherWind wind, weatherRain rain, weatherSnow snow, forecastSys sys, String dt_txt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.sys = sys;
        this.dt_txt = dt_txt;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public weatherMain getMain() {
        return main;
    }

    public void setMain(weatherMain main) {
        this.main = main;
    }

    public List<weatherWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<weatherWeather> weather) {
        this.weather = weather;
    }

    public weatherClouds getClouds() {
        return clouds;
    }

    public void setClouds(weatherClouds clouds) {
        this.clouds = clouds;
    }

    public weatherWind getWind() {
        return wind;
    }

    public void setWind(weatherWind wind) {
        this.wind = wind;
    }

    public weatherRain getRain() {
        return rain;
    }

    public void setRain(weatherRain rain) {
        this.rain = rain;
    }

    public weatherSnow getSnow() {
        return snow;
    }

    public void setSnow(weatherSnow snow) {
        this.snow = snow;
    }

    public forecastSys getSys() {
        return sys;
    }

    public void setSys(forecastSys sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
