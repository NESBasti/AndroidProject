package de.bastian.androidproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY = "WeatherWidgetids";
    int uniqueId;
    int currentCity;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        uniqueId = (int) System.currentTimeMillis();

        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, WidgetUpdateService.class);

            Bundle extras = new Bundle();
            extras.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            extras.putInt("TEMPERATURE", widgetData.getInt("TEMPERATURE", 0));
            extras.putInt("ICON", widgetData.getInt("ICON", 0));
            extras.putString("LOCATION", widgetData.getString("LOCATION", ""));
            extras.putLong("LAST_UPDATE", widgetData.getLong("LAST_UPDATE", 0));
            extras.putFloat("LATITUDE", widgetData.getFloat("LATITUDE",0));
            extras.putFloat("LONGITUDE", widgetData.getFloat("LONGITUDE",0));
            serviceIntent.putExtras(extras);

            ContextCompat.startForegroundService(context, serviceIntent);

            Intent jsonIntent = new Intent(context, WeatherAppWidgetProvider.class);
            jsonIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            jsonIntent.putExtra(WeatherAppWidgetProvider.WIDGET_IDS_KEY, appWidgetIds);

            Intent nextCityIntent = new Intent(context, WeatherAppWidgetProvider.class);
            nextCityIntent.setAction("NEXT_CITY");
            nextCityIntent.putExtra(WeatherAppWidgetProvider.WIDGET_IDS_KEY, appWidgetIds);


            PendingIntent jsonPI;
            jsonPI = PendingIntent.getBroadcast(context, appWidgetId, jsonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent nextCityPI;
            nextCityPI = PendingIntent.getBroadcast(context, appWidgetId, nextCityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent openApp = new Intent(context, MainActivity.class);
            PendingIntent openAppPI = PendingIntent.getActivity(context, 0, openApp, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.widgetLastUpdate, jsonPI);
            views.setOnClickPendingIntent(R.id.widgetLastUpdateIcon, jsonPI);
            views.setOnClickPendingIntent(R.id.widgetTemp, openAppPI);
            views.setOnClickPendingIntent(R.id.widgetIcon, openAppPI);
            views.setOnClickPendingIntent(R.id.widgetLocation, openAppPI);
            views.setOnClickPendingIntent(R.id.MyNextCityButton, nextCityPI);

            //views.setTextViewText(R.id.widgetTemp, "" + widgetData.getInt("TEMPERATURE", 0));


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        currentCity = widgetData.getInt("CURRENTCITY", 0);

        if(intent.getAction()!= null){
            if(intent.getAction().equals("NEXT_CITY")){
                currentCity++;
                if(currentCity > widgetData.getInt("CITYCOUNTER", 0)){
                    currentCity = 0;
                }
                SharedPreferences.Editor widgetDataEdit = widgetData.edit();
                widgetDataEdit.putInt("CURRENTCITY", currentCity);
                widgetDataEdit.apply();
            }
           if(!intent.getAction().equals("GET_JSON")){
                Location location = new Location("widget");
                switch (currentCity){
                    case 0:
                        location.setLatitude(widgetData.getFloat("LATITUDE", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE", 0));
                        break;
                    case 1:
                        location.setLatitude(widgetData.getFloat("LATITUDE2", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE2", 0));
                        break;
                    case 2:
                        location.setLatitude(widgetData.getFloat("LATITUDE3", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE3", 0));
                        break;
                    case 3:
                        location.setLatitude(widgetData.getFloat("LATITUDE4", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE4", 0));
                        break;
                    case 4:
                        location.setLatitude(widgetData.getFloat("LATITUDE5", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE5", 0));
                        break;
                    case 5:
                        location.setLatitude(widgetData.getFloat("LATITUDE6", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE6", 0));
                        break;
                    default:
                        location.setLatitude(widgetData.getFloat("LATITUDE", 0));
                        location.setLongitude(widgetData.getFloat("LONGITUDE", 0));
                        break;
                }
                WidgetUpdateJSON widgetUpdateJSON = new WidgetUpdateJSON(context, location);
                widgetUpdateJSON.run();
            }
        }

        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);

            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);


        } else super.onReceive(context, intent);
    }

}
