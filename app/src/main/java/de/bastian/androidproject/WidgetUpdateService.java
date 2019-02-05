package de.bastian.androidproject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WidgetUpdateService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Bundle extras = intent.getExtras();
        int[] appWidgetIds = new int[0];
        if (extras != null) {
            appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        }


        assert appWidgetIds != null;
        if (appWidgetIds.length > 0) {
            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.weather_widget);

                remoteViews.setTextViewText(R.id.widgetTemp, Integer.toString(extras.getInt("TEMPERATURE", 0)) + "°");
                remoteViews.setTextViewText(R.id.widgetLocation, extras.getString("LOCATION", ""));
                remoteViews.setTextViewText(R.id.widgetLastUpdate, new SimpleDateFormat("EEE HH:mm", Locale.GERMANY).format(new java.util.Date(extras.getLong("LAST_UPDATE", 0))));
                remoteViews.setImageViewResource(R.id.widgetIcon, extras.getInt("ICON",0));



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
