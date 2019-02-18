package de.bastian.androidproject.WeatherData;

import java.util.List;

public class WeatherListElement {
    private int dt;
    private WeatherMain main;
    private List<WeatherWeather> weather;
    private weatherClouds clouds;
    private WeatherWind wind;
    private WeatherRain rain;
    private WeatherSnow snow;
    private WeatherForecastSys sys;
    private String dt_txt;


    public WeatherListElement(int dt, WeatherMain main, List<WeatherWeather> weather, weatherClouds clouds, WeatherWind wind, WeatherRain rain, WeatherSnow snow, WeatherForecastSys sys, String dt_txt) {
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

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public List<WeatherWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherWeather> weather) {
        this.weather = weather;
    }

    public weatherClouds getClouds() {
        return clouds;
    }

    public void setClouds(weatherClouds clouds) {
        this.clouds = clouds;
    }

    public WeatherWind getWind() {
        return wind;
    }

    public void setWind(WeatherWind wind) {
        this.wind = wind;
    }

    public WeatherRain getRain() {
        return rain;
    }

    public void setRain(WeatherRain rain) {
        this.rain = rain;
    }

    public WeatherSnow getSnow() {
        return snow;
    }

    public void setSnow(WeatherSnow snow) {
        this.snow = snow;
    }

    public WeatherForecastSys getSys() {
        return sys;
    }

    public void setSys(WeatherForecastSys sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
