package de.bastian.androidproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("forecast?q=erlangen&appid=cfe31ebef1a89f6504ab9bac85dab8c4")
    Call<Weather> getWeather();
}
