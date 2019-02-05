package de.bastian.androidproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.RemoteViews;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY = "WeatherWidgetids";
    private int updateFrequency = 120000;
    int uniqueId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        SharedPreferences widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        uniqueId = (int) System.currentTimeMillis();

        if ((System.currentTimeMillis() > widgetData.getLong("LAST_UPDATE", 0) + updateFrequency)) {
            Location location = new Location("widget");
            location.setLatitude(widgetData.getFloat("LATITUDE", 0));
            location.setLongitude(widgetData.getFloat("LONGITUDE", 0));
            WidgetUpdateJSON widgetUpdateJSON = new WidgetUpdateJSON(context, location);
            widgetUpdateJSON.run();

        }
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, WeatherAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(WIDGET_IDS_KEY, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent serviceIntent = new Intent(context, WidgetUpdateService.class);
            Bundle extras = new Bundle();
            extras.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            extras.putInt("TEMPERATURE", widgetData.getInt("TEMPERATURE", 0));
            extras.putInt("ICON", widgetData.getInt("ICON", 0));
            extras.putString("LOCATION", widgetData.getString("LOCATION", ""));
            extras.putLong("LAST_UPDATE", widgetData.getLong("LAST_UPDATE", 0));
            serviceIntent.putExtras(extras);
            context.startService(serviceIntent);

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
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);


        } else super.onReceive(context, intent);
    }


}
