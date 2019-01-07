package de.bastian.androidproject;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView time;

    private TextView lastRefresh;
    private TextView temperature;
    private TextView minTemp;
    private TextView maxTemp;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormatFixed;

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

    private TextView pressure;

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

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        dateFormatFixed = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        currentTime = Calendar.getInstance().getTime();

        //set Views
        cityName = this.mainActivity.findViewById(R.id.MyCityName);
        time = this.mainActivity.findViewById(R.id.MyTime);
        lastRefresh = this.mainActivity.findViewById(R.id.MyLastRefresh);
        temperature = this.mainActivity.findViewById(R.id.MyTemperature);
        minTemp = this.mainActivity.findViewById(R.id.MyMinTemp);
        maxTemp = this.mainActivity.findViewById(R.id.MyMaxTemp);

        //set View - hourly forecast
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

        pressure = this.mainActivity.findViewById(R.id.MyPressure);
    }

    /**
     *      Updates the UI if a new JSON was fetched
     */
    void updateInterface(){
        time.setText(new SimpleDateFormat("EE, dd. MMMM HH:mm", Locale.GERMANY).format(Calendar.getInstance().getTime()));

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
    }

    void updateForecastInterface(){
        //update time
        List<WeatherListElement> hourlyForecast = new ArrayList<>();
        Date[] hourly = {new Date(0), new Date(0), new Date(0), new Date(0), new Date(0), new Date(0)};
        int maxHours = 6;
        int arrayIndex = 0;

        try {
            for(int j = 0; j < maxHours && j < weatherForecast.getCnt(); j++){
                if(currentTime.after(dateFormat.parse(weatherForecast.getList().get(j).getDt_txt()))){
                    maxHours++;
                }
                else{
                    hourly[arrayIndex] = dateFormat.parse(weatherForecast.getList().get(j).getDt_txt());
                    hourlyForecast.add(weatherForecast.getList().get(j));
                    arrayIndex++;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hourlyTime1.setText(dateFormatFixed.format(hourly[0]));
        hourlyTime2.setText(dateFormatFixed.format(hourly[1]));
        hourlyTime3.setText(dateFormatFixed.format(hourly[2]));
        hourlyTime4.setText(dateFormatFixed.format(hourly[3]));
        hourlyTime5.setText(dateFormatFixed.format(hourly[4]));
        hourlyTime6.setText(dateFormatFixed.format(hourly[5]));

        //update weather
        List<Integer> hourlyIcons = new ArrayList<>();
        List<Double> hourlyTemps = new ArrayList<>();

        for(WeatherListElement k: hourlyForecast){
            char daytime = k.getWeather().get(0).getIcon().charAt(2);
            hourlyTemps.add(k.getMain().getTemp());

            switch (k.getWeather().get(0).getIcon().substring(0,2)){
                case "01":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.sun);
                    }
                    else hourlyIcons.add(R.drawable.moon);
                    break;
                case "02":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.suncloud);
                    }
                    else hourlyIcons.add(R.drawable.mooncloud);
                    break;
                case "03":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.cloud);
                    }
                    else hourlyIcons.add(R.drawable.cloud);
                    break;
                case "04":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.heavycloud);
                    }
                    else hourlyIcons.add(R.drawable.heavycloud);
                    break;
                case "09":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.heavycloudrain);
                    }
                    else hourlyIcons.add(R.drawable.heavycloudrain);
                    break;
                case "10":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.sunrain);
                    }
                    else hourlyIcons.add(R.drawable.moonrain);
                    break;
                case "11":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.thunder);
                    }
                    else hourlyIcons.add(R.drawable.thunder);
                    break;
                case "13":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.snow);
                    }
                    else hourlyIcons.add(R.drawable.snow);
                    break;
                case "50":
                    if(daytime == 'd'){
                        hourlyIcons.add(R.drawable.sun);//TODO mist icon
                    }
                    else hourlyIcons.add(R.drawable.sun);
                    break;
                default:
                    hourlyIcons.add(R.drawable.sun);
                    break;
            }

        }
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

        pressure.setText("Pressure " + weatherCurrent.getMain().getPressure() + " hPa");
        pressure.append("\nHumidity " + weatherCurrent.getMain().getHumidity() + " %");
        pressure.append("\nWindspeed " + weatherCurrent.getWind().getSpeed() + " m/s");
        pressure.append("\nCloudiness " + weatherCurrent.getClouds().getAll() + " %");
        Date sunrise = new Date(weatherCurrent.getSys().getSunrise()*1000L);
        Date sunset = new Date(weatherCurrent.getSys().getSunset()*1000L);

        pressure.append("\nSunrise " + dateFormatFixed.format(sunrise));
        pressure.append("\nSunset " + dateFormatFixed.format(sunset));


    }

}
