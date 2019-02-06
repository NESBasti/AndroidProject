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
    private long lastUpdate = 0L;
    private long updateFrequency = 60000L;
    int uniqueId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        SharedPreferences widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        uniqueId = (int) System.currentTimeMillis();

        /*if ((System.currentTimeMillis() > lastUpdate + updateFrequency)) {
            lastUpdate = System.currentTimeMillis();
            Location location = new Location("widget");
            location.setLatitude(widgetData.getFloat("LATITUDE", 0));
            location.setLongitude(widgetData.getFloat("LONGITUDE", 0));
            WidgetUpdateJSON widgetUpdateJSON = new WidgetUpdateJSON(context, location);
            widgetUpdateJSON.run();
        }*/
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

            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, jsonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }else pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, jsonIntent, PendingIntent.FLAG_UPDATE_CURRENT);



            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            views.setImageViewResource(R.id.widgetIcon, R.drawable.cloud);

            //views.setTextViewText(R.id.widgetTemp, "" + widgetData.getInt("TEMPERATURE", 0));


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            if(intent.getAction()!= null){
                if(!intent.getAction().equals("GET_JSON")){
                    SharedPreferences widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);

                    lastUpdate = System.currentTimeMillis();
                    Location location = new Location("widget");
                    location.setLatitude(widgetData.getFloat("LATITUDE", 0));
                    location.setLongitude(widgetData.getFloat("LONGITUDE", 0));
                    WidgetUpdateJSON widgetUpdateJSON = new WidgetUpdateJSON(context, location);
                    widgetUpdateJSON.run();
                }
            }
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);

            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);


        } else super.onReceive(context, intent);
    }


}
