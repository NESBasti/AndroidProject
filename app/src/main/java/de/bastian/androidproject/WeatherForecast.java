package de.bastian.androidproject;

import java.util.List;

public class WeatherForecast {
    private int cod;
    private double message;
    private int cnt;
    private List<WeatherListElement> list;
    private City city;


    public WeatherForecast(int cod, double message, int cnt, List<WeatherListElement> list, City city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<WeatherListElement> getList() {
        return list;
    }

    public void setList(List<WeatherListElement> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}