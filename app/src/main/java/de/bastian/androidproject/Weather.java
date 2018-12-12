package de.bastian.androidproject;

public class Weather {
    private int cod;
    private double message;
    private int cnt;

    public Weather(int cod, double message, int cnt) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
    }

    public int getCod() {
        return cod;
    }

    public double getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}