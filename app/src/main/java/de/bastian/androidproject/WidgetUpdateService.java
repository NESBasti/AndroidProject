package de.bastian.androidproject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service {
    private SharedPreferences widgetData;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("yolo", "in onStartCommand");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        Bundle extras = intent.getExtras();
        int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        if (appWidgetIds.length > 0) {
            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.weather_widget);

                remoteViews.setTextViewText(R.id.widgetTemp, "" + extras.getInt("TEMPERATURE", 0));
                remoteViews.setImageViewResource(R.id.widgetIcon, extras.getInt("ICON",0));
                Log.d("yolo_service", "" + extras.getInt("TEMPERATURE"));
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
            stopSelf();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
