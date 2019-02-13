package de.bastian.androidproject;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.bastian.androidproject.UserInterface.iconToResource;

public class WidgetUpdateJSON extends Thread {
    private static final String appid = "cfe31ebef1a89f6504ab9bac85dab8c4";

    private SharedPreferences mPrefs;
    private Context context;
    private Location location;
    private WeatherCurrent weatherCurrent;
    private SharedPreferences widgetData;


    WidgetUpdateJSON(Context context, Location location) {
        this.context = context;
        this.location = location;
        widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void run() {
        String unit = mPrefs.getString("UNIT", "metric");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        if (location != null) {
            Call<WeatherCurrent> call1 = api.getCurrentWeatherFromCoordinates(appid, location.getLatitude(), location.getLongitude(), unit, "de");

            call1.enqueue(new Callback<WeatherCurrent>() {
                @Override
                public void onResponse(@NonNull Call<WeatherCurrent> call, @NonNull Response<WeatherCurrent> response) {
                    weatherCurrent = response.body();
                    SharedPreferences.Editor edit = widgetData.edit();

                    if (weatherCurrent != null) {
                        edit.putInt("TEMPERATURE", ((int) Math.round(weatherCurrent.getMain().getTemp())));
                        edit.putLong("LAST_UPDATE", System.currentTimeMillis());

                        Geocoder geocoder = new Geocoder(context);
                        String cityName;
                        try {
                            Address address = geocoder.getFromLocation(weatherCurrent.getCoord().getLat(), weatherCurrent.getCoord().getLon(), 1).get(0);
                            cityName = address.getLocality();

                        } catch (Exception e) {
                            cityName = weatherCurrent.getName();
                        }

                        edit.putString("LOCATION", cityName);
                        edit.putInt("ICON", iconToResource(weatherCurrent.getWeather().get(0).getIcon()));
                        edit.apply();
                    }

                    AppWidgetManager man = AppWidgetManager.getInstance(context);
                    int[] ids = man.getAppWidgetIds(
                            new ComponentName(context, WeatherAppWidgetProvider.class));
                    Intent updateIntent = new Intent(context, WeatherAppWidgetProvider.class);
                    updateIntent.setAction("GET_JSON");
                    updateIntent.putExtra(WeatherAppWidgetProvider.WIDGET_IDS_KEY, ids);
                    context.sendBroadcast(updateIntent);
                }

                @Override
                public void onFailure(@NonNull Call<WeatherCurrent> call, @NonNull Throwable t) {

                }
            });

        }
    }

}