package de.bastian.androidproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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




        if ((appWidgetIds != null ? appWidgetIds.length : 0) > 0) {
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
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "de.bastian.androidproject";
            String channelName = "My background service";
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("APPNAME")
                    .setContentText("foreground service")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("APPNAME")
                    .setContentText("foreground service")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

