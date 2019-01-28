package de.bastian.androidproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY ="WeatherWidgetids";

    private WeatherCurrent weatherCurrent;
    private ImageView icon;
    private TextView temp;
    private Location location;
    private SharedPreferences widgetData;
    int uniqueId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        widgetData = context.getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
        uniqueId = (int) System.currentTimeMillis();

        for(int appWidgetId: appWidgetIds){
            //Intent intent = new Intent(context, MainActivity.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, 0);

            Intent serviceIntent = new Intent(context, WidgetUpdateService.class);
            Bundle extras = new Bundle();
            extras.putIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            extras.putInt("TEMPERATURE", widgetData.getInt("TEMPERATURE", 0));
            extras.putInt("ICON", widgetData.getInt("ICON", 0));
            serviceIntent.putExtras(extras);
            Log.d("yolo", "" + widgetData.getInt("TEMPERATURE", 0));
            context.startService(serviceIntent);
            PendingIntent pServiceIntent = PendingIntent.getService(context.getApplicationContext(), uniqueId, serviceIntent, Intent.FILL_IN_ACTION );

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.widget, pServiceIntent );
            Log.d("yolo", "after setOnClick");

            views.setImageViewResource(R.id.widgetIcon, R.drawable.cloud);

            //views.setTextViewText(R.id.widgetTemp, "" + widgetData.getInt("TEMPERATURE", 0));


            appWidgetManager.updateAppWidget(appWidgetId,views);
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
