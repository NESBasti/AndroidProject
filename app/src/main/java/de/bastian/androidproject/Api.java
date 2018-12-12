package de.bastian.androidproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("forecast?")
    Call<Weather> getWeatherFromCity(@Query("appid") String appid,@Query("q") String city, @Query("units") String unit, @Query("lang") String lang);

    @GET("forecast?")
    Call<Weather> getWeatherFromCoordinates(@Query("appid") String appid,@Query("lat") double latitude,@Query("lon") double longitude, @Query("units") String unit, @Query("lang") String language);
}
