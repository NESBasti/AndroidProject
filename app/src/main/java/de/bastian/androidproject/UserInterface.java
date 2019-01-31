package de.bastian.androidproject;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class UserInterface {

    private static WeatherCurrent weatherCurrent;
    private static WeatherForecast weatherForecast;
    private static Location location;

    private Long lastUpdate = 0L;
    private Date currentTime;
    private SharedPreferences widgetData;

    private Activity mainActivity;

    private TextView cityName;

    private TextView lastRefresh;
    private TextView temperature;
    private TextView minTemp;
    private TextView maxTemp;
    private ImageView currentIcon;
    private DateFormat dateFormatTime;
    private SimpleDateFormat dateFormatDate;

    //hourly forecast
    private ArrayList<TextView> hourlyTime;
    private ArrayList<ImageView> hourlyWeather;
    private ArrayList<TextView> hourlyTemp;
    private ArrayList<TextView> hourlyRain;


    //daily forecast
    private ArrayList<ImageView> dailyWeather;
    private ArrayList<TextView> dailyTemp;
    private ArrayList<TextView> weekday;
    private GraphView graph;
    private ArrayList<ArrayList<ArrayList<TextView>>> dailyPopupText;
    private ArrayList<ArrayList<ImageView>> dailyPopupIcon;
    private ArrayList<ConstraintLayout> dailyFineLayout;

    //other Informations
    private TextView pressure;
    private TextView humidity;
    private TextView windspeed;
    private TextView cloudiness;
    private TextView sunrisetv;
    private TextView sunsettv;



    //region getter & setter

    WeatherCurrent getWeatherCurrent() {
        return weatherCurrent;
    }

    void setWeatherCurrent(WeatherCurrent weatherCurrent) {
        UserInterface.weatherCurrent = weatherCurrent;
    }

    WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    void setWeatherForecast(WeatherForecast weatherForecast) {
        UserInterface.weatherForecast = weatherForecast;
    }

    Long getLastUpdate() {
        return lastUpdate;
    }

    void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        UserInterface.location = location;
    }

    //endregion

    UserInterface(Activity mainActivity) {
        this.mainActivity = mainActivity;

        dateFormatTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        dateFormatDate = new SimpleDateFormat("EEE", Locale.getDefault());
        currentTime = Calendar.getInstance().getTime();

        //set Views
        cityName = this.mainActivity.findViewById(R.id.MyCityName);
        lastRefresh = this.mainActivity.findViewById(R.id.MyLastRefresh);
        temperature = this.mainActivity.findViewById(R.id.MyTemperature);
        minTemp = this.mainActivity.findViewById(R.id.MyMinTemp);
        maxTemp = this.mainActivity.findViewById(R.id.MyMaxTemp);
        currentIcon = this.mainActivity.findViewById(R.id.MyIcon);

        //set Views - hourly forecast
        hourlyTime = new ArrayList<>();
        hourlyWeather = new ArrayList<>();
        hourlyTemp = new ArrayList<>();
        hourlyRain = new ArrayList<>();

        for(int i = 1; i <= 6; i++){
            String idName = "hourly" + i + "time";
            int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            hourlyTime.add((TextView)this.mainActivity.findViewById(resId));

            idName = "hourly" + i + "weather";
            resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            hourlyWeather.add((ImageView)this.mainActivity.findViewById(resId));

            idName = "hourly" + i + "temp";
            resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            hourlyTemp.add((TextView)this.mainActivity.findViewById(resId));

            idName = "hourly" + i +"rain";
            resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            hourlyRain.add((TextView)this.mainActivity.findViewById(resId));
        }


        //set Views - daily forecast
        dailyWeather = new ArrayList<>();
        dailyTemp = new ArrayList<>();
        weekday = new ArrayList<>();

        for(int i = 1; i <= 5; i++) {
            String idName = "daily" + i + "weather";
            int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            dailyWeather.add((ImageView)this.mainActivity.findViewById(resId));

            idName = "daily" + i + "temp";
            resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            dailyTemp.add((TextView)this.mainActivity.findViewById(resId));

            idName = "daily" + i + "day";
            resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            if(i > 1) weekday.add((TextView)this.mainActivity.findViewById(resId));
        }

        //set Views - daily Forecast fine
        dailyPopupText = new ArrayList<>();
        dailyPopupIcon = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            dailyPopupText.add(new ArrayList<ArrayList<TextView>>());
            dailyPopupIcon.add(new ArrayList<ImageView>());
            dailyPopupText.get(i).add(new ArrayList<TextView>());
            dailyPopupText.get(i).add(new ArrayList<TextView>());

            for (int j = 0; j < 8; j++) {
                String idName = "MyPopUpD" + (i+1) + "H" + (j+1);
                int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());

                dailyPopupText.get(i).get(0).add((TextView)this.mainActivity.findViewById(resId));

                idName = "MyPopUpD" + (i+1) + "H" + (j+1) + "T";
                resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());

                dailyPopupText.get(i).get(1).add((TextView)this.mainActivity.findViewById(resId));

                idName = "MyPopUpD" + (i+1) + "H" + (j+1) + "W";
                resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
                dailyPopupIcon.get(i).add((ImageView)this.mainActivity.findViewById(resId));
            }
        }

        dailyFineLayout = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            String idName = "container_popup_day" + i;
            int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            dailyFineLayout.add((ConstraintLayout) this.mainActivity.findViewById(resId));
        }


        //other Informations
        pressure = this.mainActivity.findViewById(R.id.MyPressure);
        humidity = this.mainActivity.findViewById(R.id.MyHumidity);
        windspeed = this.mainActivity.findViewById(R.id.MyWindspeed);
        cloudiness = this.mainActivity.findViewById(R.id.MyCloudiness);
        sunrisetv = this.mainActivity.findViewById(R.id.MySunrise);
        sunsettv = this.mainActivity.findViewById(R.id.MySunset);

        //init daily temp graph
        graph = this.mainActivity.findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinX(0);

        widgetData = this.mainActivity.getApplicationContext().getSharedPreferences("WIDGET_DATA", Context.MODE_PRIVATE);
    }

    /**
     *      Updates the UI if a new JSON was fetched
     */
    void updateInterface(){
        currentTime = Calendar.getInstance().getTime();
        if(weatherCurrent != null){
            updateCurrentInterface();
        }
        if(weatherForecast != null){
            updateForecastInterface();
        }
    }


    private void updateCurrentInterface(){
        cityName.setText(weatherCurrent.getName());
        lastRefresh.setText(new SimpleDateFormat("EEE HH:mm", Locale.GERMANY).format(new java.util.Date(lastUpdate)));
        temperature.setText(String.valueOf((int) Math.round(weatherCurrent.getMain().getTemp())) + "°");
        minTemp.setText("Min " + String.valueOf((int) Math.round(weatherCurrent.getMain().getTemp_min())) + "°");
        maxTemp.setText("Max " + String.valueOf((int) Math.round(weatherCurrent.getMain().getTemp_max())) + "°");
        currentIcon.setImageResource(iconToResource(weatherCurrent.getWeather().get(0).getIcon()));

        updateWeatherWidget();
    }

    private void updateForecastInterface(){
        try {
            updateForecastHourly();
            updateForecastDaily();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void updateForecastHourly(){
        //update time
        List<WeatherListElement> hourlyForecast = new ArrayList<>();
        int maxHours = 6;
        int arrayIndex = 0;

        for(int j = 0; j < maxHours && j < weatherForecast.getCnt(); j++){
            if(currentTime.after(new Date(weatherForecast.getList().get(j).getDt() * 1000L))){
                maxHours++;
            }
            else{
                hourlyTime.get(arrayIndex).setText(dateFormatTime.format(weatherForecast.getList().get(j).getDt() * 1000L));
                hourlyForecast.add(weatherForecast.getList().get(j));
                arrayIndex++;
            }
        }


        //update weather
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        series.setThickness(4);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setAnimated(true);
        series.setColor(Color.parseColor("#66EFEFEF"));

        int count = 0;
        float precipitation = 0;

        for(WeatherListElement k: hourlyForecast){
            if(count > 5) break;
            hourlyWeather.get(count).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));

            int currentTemp = (int) Math.round(k.getMain().getTemp());
            hourlyTemp.get(count).setText(currentTemp + "°");

            if(k.getRain() != null) {
                precipitation += k.getRain().getRain();
                hourlyRain.get(count).setText(k.getRain().getRain() + " mm");
            }
            if(k.getSnow() != null){
                precipitation += k.getSnow().getSnow();
            }

            hourlyRain.get(count).setText(String.format("%.2f mm", precipitation));
            precipitation = 0;

            series.appendData(new DataPoint(count, currentTemp), true, 6);
            count++;
        }

        graph.removeAllSeries();
        graph.addSeries(series);
    }

    private void updateForecastDaily() throws ParseException {
        Calendar lastDay = Calendar.getInstance();
        Calendar currentDay = Calendar.getInstance();

        //update weekdays
        lastDay.setTime(currentTime);

        for(int i = 0; i < 4; i++){
            lastDay.add(Calendar.DAY_OF_MONTH,1);
            weekday.get(i).setText(dateFormatDate.format(lastDay.getTime()));
        }



        //update temperature
        lastDay.setTime(new Date(weatherForecast.getList().get(0).getDt() * 1000L));

        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();
        minTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp_min()));
        maxTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp_max()));
        int listFlag = 0;
        int midOfDay = 0;

        for(WeatherListElement k: weatherForecast.getList()){
            currentDay.setTime(new Date(k.getDt() * 1000L));

            if(lastDay.get(Calendar.DATE) == currentDay.get(Calendar.DATE)){
                if(k.getMain().getTemp_min() < minTemp.get(listFlag) ){
                    minTemp.set(listFlag, (int)Math.round(k.getMain().getTemp_min()));
                }
                if(k.getMain().getTemp_max() > maxTemp.get(listFlag) ){
                    maxTemp.set(listFlag, (int)Math.round(k.getMain().getTemp_max()));
                }
                if(midOfDay == 4 || (listFlag == 0 && midOfDay == 0)){
                    dailyWeather.get(listFlag).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));
                }
                midOfDay++;
            }
            else{
                lastDay.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH));
                listFlag++;
                midOfDay = 0;
                if(listFlag >= 5)
                    break;
                minTemp.add((int)Math.round(k.getMain().getTemp_min()));
                maxTemp.add((int)Math.round(k.getMain().getTemp_max()));
            }
        }

        for(int i = 0; i < dailyTemp.size(); i++){
            dailyTemp.get(i).setText(maxTemp.get(i) + "°\n" + minTemp.get(i) + "°");
        }

        Date sunrise = new Date(weatherCurrent.getSys().getSunrise()*1000L);
        Date sunset = new Date(weatherCurrent.getSys().getSunset()*1000L);
        pressure.setText(weatherCurrent.getMain().getPressure() + " hPa");
        humidity.setText(weatherCurrent.getMain().getHumidity() + " %");
        windspeed.setText(weatherCurrent.getWind().getSpeed() + " m/s");
        cloudiness.setText(weatherCurrent.getClouds().getAll() + " %");
        sunrisetv.setText(dateFormatTime.format(sunrise));
        sunsettv.setText(dateFormatTime.format(sunset));

        updateForecastDailyFine();
    }

    private void updateForecastDailyFine(){
        int indexHourToday = 0;
        int indexHour = 0;
        int indexDay = 1;

        for(int i = 0; i <= 7; i++){
            LinearLayout parent = (LinearLayout) dailyPopupText.get(0).get(0).get(i).getParent();
            parent.setVisibility(View.GONE);
            LinearLayout parentParent = (LinearLayout) parent.getParent();
            if(i >= 1){
                parentParent.getChildAt((i*2)-1).setVisibility(View.GONE);
            }

        }


        for(WeatherListElement k: weatherForecast.getList()){
            if(DateUtils.isToday(k.getDt() * 1000L)){
                dailyPopupText.get(0).get(0).get(indexHourToday).setText(dateFormatTime.format(new Date(k.getDt() * 1000L)));
                dailyPopupText.get(0).get(1).get(indexHourToday).setText(Math.round(k.getMain().getTemp()) + "°");
                dailyPopupIcon.get(0).get(indexHourToday).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));
                LinearLayout parent = (LinearLayout) dailyPopupText.get(0).get(0).get(indexHourToday).getParent();
                parent.setVisibility(View.VISIBLE);
                LinearLayout parentParent = (LinearLayout) parent.getParent();
                if(indexHourToday >= 1){
                    parentParent.getChildAt((indexHourToday*2)-1).setVisibility(View.VISIBLE);
                }
                indexHourToday++;
            }
            else{
                if(indexHour >= 8){
                    indexHour = 0;
                    indexDay++;
                    if(indexDay >= 5){
                        break;
                    }
                }
                dailyPopupText.get(indexDay).get(0).get(indexHour).setText(dateFormatTime.format(new Date(k.getDt() * 1000L)));
                dailyPopupIcon.get(indexDay).get(indexHour).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));
                dailyPopupText.get(indexDay).get(1).get(indexHour).setText(Math.round(k.getMain().getTemp()) + "°");
                indexHour++;
            }

        }

    }

    /**
     * converts the icon String to an image resource
     */
    static int iconToResource(String icon){
        char daytime = icon.charAt(2);

        switch (icon.substring(0,2)){
            case "01":
                if(daytime == 'd'){
                    return R.drawable.sun;
                }
                else return R.drawable.moon;
            case "02":
                if(daytime == 'd'){
                    return R.drawable.suncloud;
                }
                else return R.drawable.mooncloud;
            case "03":
                if(daytime == 'd'){
                    return R.drawable.cloud;
                }
                else return R.drawable.cloud;
            case "04":
                if(daytime == 'd'){
                    return R.drawable.heavycloud;
                }
                else return R.drawable.heavycloud;
            case "09":
                if(daytime == 'd'){
                    return R.drawable.heavycloudrain;
                }
                else return R.drawable.heavycloudrain;
            case "10":
                if(daytime == 'd'){
                    return R.drawable.sunrain;
                }
                else return R.drawable.moonrain;
            case "11":
                if(daytime == 'd'){
                    return R.drawable.thunder;
                }
                else return R.drawable.thunder;
            case "13":
                if(daytime == 'd'){
                    return R.drawable.snow;
                }
                else return R.drawable.snow;
            case "50":
                if(daytime == 'd'){
                    return R.drawable.sun;//TODO mist icon
                }
                else return R.drawable.sun;
            default:
                return R.drawable.sun;
        }
    }

    /**
     * updates all AppWidgets on the home screen with latest data
     */
    private void updateWeatherWidget(){
        SharedPreferences.Editor edit = widgetData.edit();
        if(weatherCurrent != null) {
            edit.putInt("TEMPERATURE", ((int) Math.round(weatherCurrent.getMain().getTemp())));
            edit.putFloat("LATITUDE", (float)weatherCurrent.getCoord().getLat());
            edit.putFloat("LONGITUDE", (float)weatherCurrent.getCoord().getLon());
            edit.putLong("LAST_UPDATE", lastUpdate);
            edit.putString("LOCATION", weatherCurrent.getName());
            edit.putInt("ICON", iconToResource(weatherCurrent.getWeather().get(0).getIcon()));
            edit.apply();
        }

        AppWidgetManager man = AppWidgetManager.getInstance(this.mainActivity.getApplicationContext());
        int[] ids = man.getAppWidgetIds(
                new ComponentName(this.mainActivity.getApplicationContext(),WeatherAppWidgetProvider.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(WeatherAppWidgetProvider.WIDGET_IDS_KEY, ids);
        this.mainActivity.getApplicationContext().sendBroadcast(updateIntent);
    }

    void openDaily(int day){
        switch (day){
            case 1: setInvisible();
                dailyFineLayout.get(0).setVisibility(View.VISIBLE);
                break;
            case 2: setInvisible();
                dailyFineLayout.get(1).setVisibility(View.VISIBLE);
                break;
            case 3: setInvisible();
                dailyFineLayout.get(2).setVisibility(View.VISIBLE);
                break;
            case 4: setInvisible();
                dailyFineLayout.get(3).setVisibility(View.VISIBLE);
                break;
            case 5: setInvisible();
                dailyFineLayout.get(4).setVisibility(View.VISIBLE);
                break;
            default:
                setInvisible();
                break;
        }
    }

    void setInvisible(){
    for(ConstraintLayout day: dailyFineLayout){
        day.setVisibility(View.GONE);
        }
    }

}

