package de.bastian.androidproject;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class UserInterface {

    private WeatherCurrent weatherCurrent;
    private WeatherForecast weatherForecast;

    private Long lastUpdate = 0L;
    private Date currentTime;

    private Activity mainActivity;

    private TextView cityName;

    private TextView lastRefresh;
    private TextView temperature;
    private TextView minTemp;
    private TextView maxTemp;
    private ImageView currentIcon;
    private TextView weatherInfo;
    private SimpleDateFormat dateFormatJSON;
    private SimpleDateFormat dateFormatTime;
    private SimpleDateFormat dateFormatDate;

    //hourly forecast
    private TextView hourlyTime1;
    private TextView hourlyTime2;
    private TextView hourlyTime3;
    private TextView hourlyTime4;
    private TextView hourlyTime5;
    private TextView hourlyTime6;
    private ImageView hourlyWeather1;
    private ImageView hourlyWeather2;
    private ImageView hourlyWeather3;
    private ImageView hourlyWeather4;
    private ImageView hourlyWeather5;
    private ImageView hourlyWeather6;
    private TextView hourlyTemp1;
    private TextView hourlyTemp2;
    private TextView hourlyTemp3;
    private TextView hourlyTemp4;
    private TextView hourlyTemp5;
    private TextView hourlyTemp6;

    //daily forecast
    private ImageView dailyWeather1;
    private ImageView dailyWeather2;
    private ImageView dailyWeather3;
    private ImageView dailyWeather4;
    private ImageView dailyWeather5;
    private TextView  dailyTemp1;
    private TextView  dailyTemp2;
    private TextView  dailyTemp3;
    private TextView  dailyTemp4;
    private TextView  dailyTemp5;
    private TextView  weekday2;
    private TextView  weekday3;
    private TextView  weekday4;
    private TextView  weekday5;
    private GraphView graph;

    //region getter & setter

    WeatherCurrent getWeatherCurrent() {
        return weatherCurrent;
    }

    void setWeatherCurrent(WeatherCurrent weatherCurrent) {
        this.weatherCurrent = weatherCurrent;
    }

    WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    Long getLastUpdate() {
        return lastUpdate;
    }

    void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    //endregion

    UserInterface(Activity mainActivity) {
        this.mainActivity = mainActivity;

        dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        dateFormatTime = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        dateFormatDate = new SimpleDateFormat("EEE",Locale.GERMANY);
        currentTime = Calendar.getInstance().getTime();

        //set Views
        cityName = this.mainActivity.findViewById(R.id.MyCityName);
        lastRefresh = this.mainActivity.findViewById(R.id.MyLastRefresh);
        temperature = this.mainActivity.findViewById(R.id.MyTemperature);
        minTemp = this.mainActivity.findViewById(R.id.MyMinTemp);
        maxTemp = this.mainActivity.findViewById(R.id.MyMaxTemp);
        currentIcon = this.mainActivity.findViewById(R.id.MyIcon);
        weatherInfo = this.mainActivity.findViewById(R.id.MyPressure);

        //set Views - hourly forecast
        hourlyTime1 = this.mainActivity.findViewById(R.id.hourly1time);
        hourlyTime2 = this.mainActivity.findViewById(R.id.hourly2time);
        hourlyTime3 = this.mainActivity.findViewById(R.id.hourly3time);
        hourlyTime4 = this.mainActivity.findViewById(R.id.hourly4time);
        hourlyTime5 = this.mainActivity.findViewById(R.id.hourly5time);
        hourlyTime6 = this.mainActivity.findViewById(R.id.hourly6time);

        hourlyWeather1 = this.mainActivity.findViewById(R.id.hourly1weather);
        hourlyWeather2 = this.mainActivity.findViewById(R.id.hourly2weather);
        hourlyWeather3 = this.mainActivity.findViewById(R.id.hourly3weather);
        hourlyWeather4 = this.mainActivity.findViewById(R.id.hourly4weather);
        hourlyWeather5 = this.mainActivity.findViewById(R.id.hourly5weather);
        hourlyWeather6 = this.mainActivity.findViewById(R.id.hourly6weather);

        hourlyTemp1 = this.mainActivity.findViewById(R.id.hourly1temp);
        hourlyTemp2 = this.mainActivity.findViewById(R.id.hourly2temp);
        hourlyTemp3 = this.mainActivity.findViewById(R.id.hourly3temp);
        hourlyTemp4 = this.mainActivity.findViewById(R.id.hourly4temp);
        hourlyTemp5 = this.mainActivity.findViewById(R.id.hourly5temp);
        hourlyTemp6 = this.mainActivity.findViewById(R.id.hourly6temp);

        //set Views - daily forecast
        dailyWeather1 = this.mainActivity.findViewById(R.id.daily1weather);
        dailyWeather2 = this.mainActivity.findViewById(R.id.daily2weather);
        dailyWeather3 = this.mainActivity.findViewById(R.id.daily3weather);
        dailyWeather4 = this.mainActivity.findViewById(R.id.daily4weather);
        dailyWeather5 = this.mainActivity.findViewById(R.id.daily5weather);

        dailyTemp1 = this.mainActivity.findViewById(R.id.daily1temp);
        dailyTemp2 = this.mainActivity.findViewById(R.id.daily2temp);
        dailyTemp3 = this.mainActivity.findViewById(R.id.daily3temp);
        dailyTemp4 = this.mainActivity.findViewById(R.id.daily4temp);
        dailyTemp5 = this.mainActivity.findViewById(R.id.daily5temp);

        weekday2 = this.mainActivity.findViewById(R.id.daily2day);
        weekday3 = this.mainActivity.findViewById(R.id.daily3day);
        weekday4 = this.mainActivity.findViewById(R.id.daily4day);
        weekday5 = this.mainActivity.findViewById(R.id.daily5day);

        //init daily temp graph
        graph = this.mainActivity.findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinX(0);

    }

    /**
     *      Updates the UI if a new JSON was fetched
     */
    void updateInterface(){
        if(weatherCurrent != null){
            updateCurrentInterface();
        }
        if(weatherForecast != null){
            updateForecastInterface();
        }
    }

    void updateCurrentInterface(){
        cityName.setText(weatherCurrent.getName());
        lastRefresh.setText(new SimpleDateFormat("EEE HH:mm", Locale.GERMANY).format(new java.util.Date(lastUpdate)));
        temperature.setText(String.valueOf((int) weatherCurrent.getMain().getTemp()) + "°");
        minTemp.setText("Min " + String.valueOf((int) weatherCurrent.getMain().getTemp_min()) + "°");
        maxTemp.setText("Max " + String.valueOf((int) weatherCurrent.getMain().getTemp_max()) + "°");
        currentIcon.setImageResource(iconToResource(weatherCurrent.getWeather().get(0).getIcon()));


        weatherInfo.setText("Pressure " + weatherCurrent.getMain().getPressure() + " hPa");
        weatherInfo.append("\nHumidity " + weatherCurrent.getMain().getHumidity() + " %");
        weatherInfo.append("\nWindspeed " + weatherCurrent.getWind().getSpeed() + " m/s");
        weatherInfo.append("\nCloudiness " + weatherCurrent.getClouds().getAll() + " %");
        Date sunrise = new Date(weatherCurrent.getSys().getSunrise()*1000L);
        Date sunset = new Date(weatherCurrent.getSys().getSunset()*1000L);

        weatherInfo.append("\nSunrise " + dateFormatTime.format(sunrise));
        weatherInfo.append("\nSunset " + dateFormatTime.format(sunset));
    }

    void updateForecastInterface(){
        updateForecastHourly();

        try {
            updateForecastDaily();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    void updateForecastHourly(){
        //update time
        List<WeatherListElement> hourlyForecast = new ArrayList<>();
        Date[] hourly = {new Date(0), new Date(0), new Date(0), new Date(0), new Date(0), new Date(0)};
        int maxHours = 6;
        int arrayIndex = 0;

        try {
            for(int j = 0; j < maxHours && j < weatherForecast.getCnt(); j++){
                if(currentTime.after(dateFormatJSON.parse(weatherForecast.getList().get(j).getDt_txt()))){
                    maxHours++;
                }
                else{
                    hourly[arrayIndex] = dateFormatJSON.parse(weatherForecast.getList().get(j).getDt_txt());
                    hourlyForecast.add(weatherForecast.getList().get(j));
                    arrayIndex++;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        hourlyTime1.setText(dateFormatTime.format(hourly[0]));
        hourlyTime2.setText(dateFormatTime.format(hourly[1]));
        hourlyTime3.setText(dateFormatTime.format(hourly[2]));
        hourlyTime4.setText(dateFormatTime.format(hourly[3]));
        hourlyTime5.setText(dateFormatTime.format(hourly[4]));
        hourlyTime6.setText(dateFormatTime.format(hourly[5]));

        //update weather
        List<Integer> hourlyIcons = new ArrayList<>();
        List<Double> hourlyTemps = new ArrayList<>();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.setThickness(4);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setAnimated(true);
        series.setColor(Color.parseColor("#66EFEFEF"));

        int count = 0;

        for(WeatherListElement k: hourlyForecast){
            hourlyIcons.add(iconToResource(k.getWeather().get(0).getIcon()));
            hourlyTemps.add(k.getMain().getTemp());
            series.appendData(new DataPoint(count, k.getMain().getTemp()), true, 6);
            count++;
        }

        graph.removeAllSeries();
        graph.addSeries(series);
        hourlyWeather1.setImageResource(hourlyIcons.get(0));
        hourlyWeather2.setImageResource(hourlyIcons.get(1));
        hourlyWeather3.setImageResource(hourlyIcons.get(2));
        hourlyWeather4.setImageResource(hourlyIcons.get(3));
        hourlyWeather5.setImageResource(hourlyIcons.get(4));
        hourlyWeather6.setImageResource(hourlyIcons.get(5));

        hourlyTemp1.setText(String.valueOf(Math.round(hourlyTemps.get(0))) + "°");
        hourlyTemp2.setText(String.valueOf(Math.round(hourlyTemps.get(1))) + "°");
        hourlyTemp3.setText(String.valueOf(Math.round(hourlyTemps.get(2))) + "°");
        hourlyTemp4.setText(String.valueOf(Math.round(hourlyTemps.get(3))) + "°");
        hourlyTemp5.setText(String.valueOf(Math.round(hourlyTemps.get(4))) + "°");
        hourlyTemp6.setText(String.valueOf(Math.round(hourlyTemps.get(5))) + "°");
    }

    void updateForecastDaily() throws ParseException {
        Calendar lastDay = Calendar.getInstance();
        Calendar currentDay = Calendar.getInstance();

        //update weekdays
        lastDay.setTime(currentTime);

        lastDay.add(Calendar.DAY_OF_MONTH,1);
        weekday2.setText(dateFormatDate.format(lastDay.getTime()));
        lastDay.add(Calendar.DAY_OF_MONTH,1);
        weekday3.setText(dateFormatDate.format(lastDay.getTime()));
        lastDay.add(Calendar.DAY_OF_MONTH,1);
        weekday4.setText(dateFormatDate.format(lastDay.getTime()));
        lastDay.add(Calendar.DAY_OF_MONTH,1);
        weekday5.setText(dateFormatDate.format(lastDay.getTime()));


        //update temperature
        lastDay.setTime(dateFormatJSON.parse(weatherForecast.getList().get(0).getDt_txt()));

        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();
        List<Integer> weatherIcons = new ArrayList<>();
        minTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp_min()));
        maxTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp_max()));
        int listFlag = 0;

        for(int i = 0; i < weatherForecast.getCnt(); i++){
            currentDay.setTime(dateFormatJSON.parse(weatherForecast.getList().get(i).getDt_txt()));

            if(lastDay.get(Calendar.DATE) == currentDay.get(Calendar.DATE)){
                if(weatherForecast.getList().get(i).getMain().getTemp_min() < minTemp.get(listFlag) ){
                    minTemp.set(listFlag, (int)Math.round(weatherForecast.getList().get(i).getMain().getTemp_min()));
                }
                if(weatherForecast.getList().get(i).getMain().getTemp_max() > maxTemp.get(listFlag) ){
                    maxTemp.set(listFlag, (int)Math.round(weatherForecast.getList().get(i).getMain().getTemp_max()));
                }
                if(dateFormatTime.format(dateFormatJSON.parse(weatherForecast.getList().get(i).getDt_txt())).compareTo(dateFormatTime.format(dateFormatTime.parse("12:00"))) == 0){
                    weatherIcons.add(iconToResource(weatherForecast.getList().get(i).getWeather().get(0).getIcon()));
                }
            }
            else{
                lastDay.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH));
                listFlag++;
                if(listFlag > 5)
                    break;
                minTemp.add((int)Math.round(weatherForecast.getList().get(i).getMain().getTemp_min()));
                maxTemp.add((int)Math.round(weatherForecast.getList().get(i).getMain().getTemp_max()));
            }
        }


        dailyTemp1.setText(maxTemp.get(0) + "°\n" + minTemp.get(0) + "°");
        dailyTemp2.setText(maxTemp.get(1) + "°\n" + minTemp.get(1) + "°");
        dailyTemp3.setText(maxTemp.get(2) + "°\n" + minTemp.get(2) + "°");
        dailyTemp4.setText(maxTemp.get(3) + "°\n" + minTemp.get(3) + "°");
        dailyTemp5.setText(maxTemp.get(4) + "°\n" + minTemp.get(4) + "°");

        dailyWeather1.setImageResource(weatherIcons.get(0));
        dailyWeather2.setImageResource(weatherIcons.get(1));
        dailyWeather3.setImageResource(weatherIcons.get(2));
        dailyWeather4.setImageResource(weatherIcons.get(3));
        dailyWeather5.setImageResource(weatherIcons.get(4));



    }

    private int iconToResource(String icon){
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
}

