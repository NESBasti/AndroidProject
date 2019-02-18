package de.bastian.androidproject.WeatherData;

import java.util.List;

public class WeatherCurrent {
    private WeatherCoord coord;
    private List<WeatherWeather> weather;
    private String base;
    private WeatherMain main;
    private int visibility;
    private WeatherWind wind;
    private weatherClouds clouds;
    private WeatherRain rain;
    private int dt;
    private WeatherSys sys;
    private int id;
    private String name;
    private int cod;

    public WeatherCurrent(WeatherCoord coord, List<WeatherWeather> weather, String base, WeatherMain main, int visibility, WeatherWind wind, weatherClouds clouds, WeatherRain rain, int dt, WeatherSys sys, int id, String name, int cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.rain = rain;
        this.dt = dt;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }


    public WeatherCoord getCoord() {
        return coord;
    }

    public void setCoord(WeatherCoord coord) {
        this.coord = coord;
    }

    public List<WeatherWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public WeatherWind getWind() {
        return wind;
    }

    public void setWind(WeatherWind wind) {
        this.wind = wind;
    }

    public weatherClouds getClouds() {
        return clouds;
    }

    public void setClouds(weatherClouds clouds) {
        this.clouds = clouds;
    }

    public WeatherRain getRain() {
        return rain;
    }

    public void setRain(WeatherRain rain) {
        this.rain = rain;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public WeatherSys getSys() {
        return sys;
    }

    public void setSys(WeatherSys sys) {
        this.sys = sys;
    }
}
