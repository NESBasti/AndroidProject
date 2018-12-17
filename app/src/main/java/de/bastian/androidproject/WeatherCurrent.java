package de.bastian.androidproject;

import java.util.List;

public class WeatherCurrent {
    private Coord coord;
    private List<weatherWeather> weather;
    private String base;
    private weatherMain main;
    private weatherWind wind;
    private weatherClouds clouds;
    private weatherRain rain;
    private int dt;
    private weatherSys sys;
    private int id;
    private String name;
    private int cod;

    public WeatherCurrent(Coord coord, List<weatherWeather> weather, String base, weatherMain main, weatherWind wind, weatherClouds clouds, weatherRain rain, int dt, weatherSys sys, int id, String name, int cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
        this.rain = rain;
        this.dt = dt;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<weatherWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<weatherWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public weatherMain getMain() {
        return main;
    }

    public void setMain(weatherMain main) {
        this.main = main;
    }

    public weatherWind getWind() {
        return wind;
    }

    public void setWind(weatherWind wind) {
        this.wind = wind;
    }

    public weatherClouds getClouds() {
        return clouds;
    }

    public void setClouds(weatherClouds clouds) {
        this.clouds = clouds;
    }

    public weatherRain getRain() {
        return rain;
    }

    public void setRain(weatherRain rain) {
        this.rain = rain;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public weatherSys getSys() {
        return sys;
    }

    public void setSys(weatherSys sys) {
        this.sys = sys;
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
}
