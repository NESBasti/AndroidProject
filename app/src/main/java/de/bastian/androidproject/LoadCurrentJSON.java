package de.bastian.androidproject;

import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadCurrentJSON extends AsyncTask<Location,Boolean, Integer> {
    private MainActivity mainActivity;
    private WeatherCurrent weatherCurrent;
    private static String appid = "cfe31ebef1a89f6504ab9bac85dab8c4";

    public LoadCurrentJSON(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public WeatherCurrent getWeatherCurrent() {
        return weatherCurrent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Location... locations) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        if(locations != null) {
            Call<WeatherCurrent> call1 = api.getCurrentWeatherFromCoordinates(appid, locations[0].getLatitude(), locations[0].getLongitude(), "metric", "de");

            call1.enqueue(new Callback<WeatherCurrent>() {
                @Override
                public void onResponse(Call<WeatherCurrent> call, Response<WeatherCurrent> response) {
                    weatherCurrent = response.body();
                    publishProgress(true);

                }

                @Override
                public void onFailure(Call<WeatherCurrent> call, Throwable t) {

                    publishProgress(false);
                }
            });

        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);

        if(values[0]){
            mainActivity.updateCurrentWeather(weatherCurrent);
        }
        else Toast.makeText(mainActivity, "No Connection", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}
