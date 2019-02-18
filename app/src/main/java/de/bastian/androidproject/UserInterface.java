package de.bastian.androidproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.bastian.androidproject.WeatherData.WeatherCurrent;
import de.bastian.androidproject.WeatherData.WeatherForecast;
import de.bastian.androidproject.WeatherData.WeatherListElement;

class UserInterface {
    private Activity mainActivity;
    private SharedPreferences mPrefs;

    private static WeatherCurrent weatherCurrent;
    private static WeatherForecast weatherForecast;
    private static Location location;

    private Long lastUpdate = 0L;
    private Date currentTime;
    private DateFormat dateFormatTime;
    private SimpleDateFormat dateFormatDate;

    //current weather
    private TextView cityName;
    private TextView lastRefresh;
    private TextView temperature;
    private TextView minMaxTemp;
    private TextView windchill;
    private ImageView currentIcon;

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
    private float dailyRain[];

    //daily forecast - layout
    private ArrayList<LinearLayout> myDaily;
    private ArrayList<ConstraintLayout> dailyFineLayout;
    private ScrollView myScrollView;
    private View myContainerTemp;
    private View myContainerHourly;

    //other information
    private TextView pressure;
    private TextView humidity;
    private TextView windSpeed;
    private TextView cloudiness;
    private TextView sunrise;
    private TextView sunset;

    //What to wear
    private View myClothingDay1;
    private View myClothingDay2;
    private View myClothingDay3;
    private View myClothingDay4;
    private View myClothingDay5;

    //clothes
    private ArrayList<ImageView> clothesIcon;
    private ArrayList<TextView> clothesDay;
    private TextView clothingText;
    private LinearLayout clothingLayout;



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

    TextView getCityName() {
        return cityName;
    }
    //endregion

    UserInterface(Activity mainActivity) {
        this.mainActivity = mainActivity;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);

        dateFormatTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        dateFormatDate = new SimpleDateFormat("EEE", Locale.getDefault());
        currentTime = Calendar.getInstance().getTime();

        //region set Views
        //set Views - current weather
        myScrollView = this.mainActivity.findViewById(R.id.MyScrollView);
        cityName = this.mainActivity.findViewById(R.id.MyCityName);
        lastRefresh = this.mainActivity.findViewById(R.id.MyLastRefresh);
        temperature = this.mainActivity.findViewById(R.id.MyTemperature);
        minMaxTemp = this.mainActivity.findViewById(R.id.MyMinMaxTemp);
        windchill = this.mainActivity.findViewById(R.id.MyWindchill);
        currentIcon = this.mainActivity.findViewById(R.id.MyIcon);

        //set Views - hourly forecast
        hourlyTime = new ArrayList<>();
        hourlyWeather = new ArrayList<>();
        hourlyTemp = new ArrayList<>();
        hourlyRain = new ArrayList<>();

        //What to wear
        clothingText = this.mainActivity.findViewById(R.id.clothing_informations);
        myClothingDay1 = this.mainActivity.findViewById(R.id.ClothingDay1);
        myClothingDay2 = this.mainActivity.findViewById(R.id.ClothingDay2);
        myClothingDay3 = this.mainActivity.findViewById(R.id.ClothingDay3);
        myClothingDay4 = this.mainActivity.findViewById(R.id.ClothingDay4);
        myClothingDay5 = this.mainActivity.findViewById(R.id.ClothingDay5);
        clothingLayout = this.mainActivity.findViewById(R.id.ClothingLayout);


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


        //set views - other Informations
        pressure = this.mainActivity.findViewById(R.id.MyPressure);
        humidity = this.mainActivity.findViewById(R.id.MyHumidity);
        windSpeed = this.mainActivity.findViewById(R.id.MyWindspeed);
        cloudiness = this.mainActivity.findViewById(R.id.MyCloudiness);
        sunrise = this.mainActivity.findViewById(R.id.MySunrise);
        sunset = this.mainActivity.findViewById(R.id.MySunset);

        //set views - clothes
        clothesDay = new ArrayList<>();
        clothesIcon = new ArrayList<>();

        for(int i = 1; i <= 5; i++){
            String idName = "MyClothes" + i + "Icon";
            int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            clothesIcon.add((ImageView) this.mainActivity.findViewById(resId));

            if(i != 1){
                idName = "MyClothes" + i + "Day";
                resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
                clothesDay.add((TextView) this.mainActivity.findViewById(resId));
            }
        }

        //set Views - daily forecast animations
        myDaily = new ArrayList<>();
        for(int i = 1; i <= 5; i++){
            String idName = "MyDailyD" + i;
            int resId = this.mainActivity.getResources().getIdentifier(idName, "id", this.mainActivity.getPackageName());
            myDaily.add((LinearLayout)this.mainActivity.findViewById(resId));
        }
        myContainerTemp = this.mainActivity.findViewById(R.id.container_temperature);
        myContainerHourly = this.mainActivity.findViewById(R.id.container_hourly);
        //endregion

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
        currentTime = Calendar.getInstance().getTime();
        if(weatherCurrent != null){
            updateCurrentInterface();
        }
        if(weatherForecast != null){
            updateForecastInterface();
        }
    }

    void updateCurrentInterface(){
        Geocoder geocoder = new Geocoder(this.mainActivity);
        List<Address> address;
        if(location != null) {
            try {
                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                if (address.isEmpty() || address.get(0).getLocality() == null) {
                    cityName.setText(weatherCurrent.getName());
                } else {
                    cityName.setText(address.get(0).getLocality());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            cityName.setText(weatherCurrent.getName());
        }

        lastRefresh.setText(dateFormatTime.format(lastUpdate));
        temperature.setText(String.format(Locale.getDefault(), "%d°", Math.round(weatherCurrent.getMain().getTemp())));
        minMaxTemp.setText(String.format(Locale.getDefault(), "%d° / %d°", Math.round(weatherCurrent.getMain().getTemp_max()), Math.round(weatherCurrent.getMain().getTemp_min())));

        int windchillTemp = (int) Math.round(weatherCurrent.getMain().getTemp());
        if((Math.round(weatherCurrent.getMain().getTemp()) <= 10 && mPrefs.getString("UNIT", "metric").equals("metric") || Math.round(weatherCurrent.getMain().getTemp()) <= 50 && mPrefs.getString("UNIT", "metric").equals("imperial"))&& (weatherCurrent.getWind().getSpeed() * 3.6) >= 5) { //windchill is defined with temperatures of 10°C/50°F or lower and 5km/h wind speed or more

            if(mPrefs.getString("UNIT", "metric").equals("imperial")){
                windchillTemp = (int) ((13.12 + 0.6125 * ((weatherCurrent.getMain().getTemp()) - 32) / 1.8) + (0.3965 * (((weatherCurrent.getMain().getTemp()) - 32) / 1.8) - 11.37) * Math.pow(weatherCurrent.getWind().getSpeed() * 3.6, 0.16));
                windchillTemp = (int) (windchillTemp * 1.8) + 32;
            }
            else windchillTemp = (int) ((13.12 + 0.6125 * weatherCurrent.getMain().getTemp()) + (0.3965 * weatherCurrent.getMain().getTemp() - 11.37) * Math.pow(weatherCurrent.getWind().getSpeed() * 3.6, 0.16));

        }
        String TVWindchill = this.mainActivity.getResources().getString(R.string.TextViewWindchill);
        windchill.setText(String.format(Locale.getDefault(), TVWindchill + " %d°", windchillTemp));

        currentIcon.setImageResource(iconToResource(weatherCurrent.getWeather().get(0).getIcon()));


    }

    void updateForecastInterface() {
        currentTime = Calendar.getInstance().getTime();
        updateForecastHourly();
        updateForecastDaily();
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
            hourlyTemp.get(count).setText(String.format(Locale.getDefault(), "%d°", currentTemp));

            if(k.getRain() != null) {
                precipitation += k.getRain().getRain();
            }
            if(k.getSnow() != null){
                precipitation += k.getSnow().getSnow();
            }

            hourlyRain.get(count).setText(String.format(Locale.getDefault(),"%.2f l", precipitation));
            precipitation = 0;

            series.appendData(new DataPoint(count, currentTemp), true, 6);
            count++;
        }
        graph.removeAllSeries();
        graph.addSeries(series);
    }

    private void updateForecastDaily(){
        Calendar lastDay = Calendar.getInstance();
        Calendar currentDay = Calendar.getInstance();

        //update weekdays
        lastDay.setTime(currentTime);

        for(int i = 0; i < 4; i++){
            lastDay.add(Calendar.DAY_OF_MONTH,1);
            weekday.get(i).setText(dateFormatDate.format(lastDay.getTime()));
            clothesDay.get(i).setText(dateFormatDate.format(lastDay.getTime()));
        }

        //update temperature
        lastDay.setTime(new Date(weatherForecast.getList().get(0).getDt() * 1000L));

        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();
        minTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp()));
        maxTemp.add((int)Math.round(weatherForecast.getList().get(0).getMain().getTemp()));
        int listFlag = 0;
        int midOfDay = 0;
        dailyRain = new float[5];

        for(WeatherListElement k: weatherForecast.getList()){
            currentDay.setTime(new Date(k.getDt() * 1000L));

            if(lastDay.get(Calendar.DATE) == currentDay.get(Calendar.DATE)){
                if(k.getMain().getTemp() < minTemp.get(listFlag) ){
                    minTemp.set(listFlag, (int)Math.round(k.getMain().getTemp()));
                }
                if(k.getMain().getTemp() > maxTemp.get(listFlag) ){
                    maxTemp.set(listFlag, (int)Math.round(k.getMain().getTemp()));
                }
                if((midOfDay == 3 && listFlag != 0) || (listFlag == 0 && midOfDay == 0)){
                    dailyWeather.get(listFlag).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));
                }
                if(k.getRain() != null){
                    dailyRain[listFlag] += k.getRain().getRain();
                }
                midOfDay++;
            }
            else{
                if(midOfDay == 0){ //in case current day has no entries
                    if(weatherCurrent != null) {
                        dailyWeather.get(listFlag).setImageResource(iconToResource(k.getWeather().get(0).getIcon()));
                        minTemp.set(listFlag, (int) Math.round(weatherCurrent.getMain().getTemp()));
                        maxTemp.set(listFlag, (int) Math.round(weatherCurrent.getMain().getTemp()));
                    }
                    else{
                        minTemp.set(listFlag, 0);
                        maxTemp.set(listFlag, 0);
                    }
                }
                lastDay.set(currentDay.get(Calendar.YEAR), currentDay.get(Calendar.MONTH), currentDay.get(Calendar.DAY_OF_MONTH));
                listFlag++;
                midOfDay = 0;
                if(listFlag >= 5)
                    break;
                minTemp.add((int)Math.round(k.getMain().getTemp()));
                maxTemp.add((int)Math.round(k.getMain().getTemp()));
            }
        }

        //update clothes suggestion
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.mainActivity);
        int MyUserTemp = preferences.getInt("UserTemp", 0);
        for(int i = 0; i < dailyTemp.size(); i++){
            dailyTemp.get(i).setText(String.format(Locale.getDefault(),"%d°\n%d",maxTemp.get(i), minTemp.get(i)));
            try {
                if (minTemp.get(i) < (5 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("metric") || minTemp.get(i) < (41 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("imperial")) {
                    if (dailyRain[i] > 5) {
                        clothesIcon.get(i).setImageResource(R.drawable.ic_coat_umbrella);
                    } else
                        clothesIcon.get(i).setImageResource(R.drawable.ic_coat);
                } else if (minTemp.get(i) < (13 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("metric") || minTemp.get(i) < (55 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("imperial")) {
                    if (dailyRain[i] > 5) {
                        clothesIcon.get(i).setImageResource(R.drawable.ic_jacket_umbrella);
                    } else
                        clothesIcon.get(i).setImageResource(R.drawable.ic_jacket);
                } else if (minTemp.get(i) < (20 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("metric") || minTemp.get(i) < (68 + MyUserTemp) && mPrefs.getString("UNIT", "metric").equals("imperial")) {
                    if (dailyRain[i] > 5) {
                        clothesIcon.get(i).setImageResource(R.drawable.ic_hoodie_umbrella);
                    } else
                        clothesIcon.get(i).setImageResource(R.drawable.ic_hoodie);
                } else {
                    if (dailyRain[i] > 5) {
                        clothesIcon.get(i).setImageResource(R.drawable.ic_tshirt_umbrella);
                    } else
                        clothesIcon.get(i).setImageResource(R.drawable.ic_tshirt);
                }
            }
            catch (NullPointerException e){
                break;
            }
        }

        //update other weather information
        Date sunriseTime = new Date(weatherCurrent.getSys().getSunrise()*1000L);
        Date sunsetTime = new Date(weatherCurrent.getSys().getSunset()*1000L);
        pressure.setText(String.format(Locale.getDefault(), "%.1f hPa", weatherCurrent.getMain().getPressure()));
        humidity.setText(String.format(Locale.getDefault(), "%.1f %%", weatherCurrent.getMain().getHumidity()));
        windSpeed.setText(String.format(Locale.getDefault(), "%.1f m/s", weatherCurrent.getWind().getSpeed()));
        cloudiness.setText(String.format(Locale.getDefault(), "%d %%", weatherCurrent.getClouds().getAll()));
        this.sunrise.setText(dateFormatTime.format(sunriseTime));
        sunset.setText(dateFormatTime.format(sunsetTime));

        updateForecastDailyFine();
    }

    /**
     * fill daily-popup
     */
    private void updateForecastDailyFine(){
        int indexHourToday = 0;
        int indexHour = 0;
        int indexDay = 1;

        for(int i = 0; i <= 7; i++){ //set all current day entries to GONE to only show the ones we have after
            LinearLayout parent = (LinearLayout) dailyPopupText.get(0).get(0).get(i).getParent();
            parent.setVisibility(View.GONE);
            LinearLayout parentParent = (LinearLayout) parent.getParent();
            if(i >= 1){
                parentParent.getChildAt((i*2)-1).setVisibility(View.GONE);
            }

        }

        Calendar currentDay = Calendar.getInstance();
        Calendar elementDay = Calendar.getInstance();
        currentDay.setTime(currentTime);

        for(WeatherListElement k: weatherForecast.getList()){
            elementDay.setTime(new Date(k.getDt() * 1000L));
            if(currentDay.get(Calendar.DAY_OF_YEAR) == elementDay.get(Calendar.DAY_OF_YEAR) &&
                    currentDay.get(Calendar.YEAR) == elementDay.get(Calendar.YEAR)){

                dailyPopupText.get(0).get(0).get(indexHourToday).setText(dateFormatTime.format(new Date(k.getDt() * 1000L)));
                dailyPopupText.get(0).get(1).get(indexHourToday).setText(String.format(Locale.getDefault(), "%d°", Math.round(k.getMain().getTemp())));
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
                dailyPopupText.get(indexDay).get(1).get(indexHour).setText(String.format(Locale.getDefault(), "%d°", Math.round(k.getMain().getTemp())));
                indexHour++;
            }
        }
    }

    /**
     * converts the icon String to an image resource
     */
    public static int iconToResource(String icon){
        char daytime = icon.charAt(2);

        switch (icon.substring(0,2)){
            case "01":
                if(daytime == 'd'){
                    return R.drawable.ic_sun;
                }
                else return R.drawable.ic_moon;
            case "02":
                if(daytime == 'd'){
                    return R.drawable.ic_suncloud;
                }
                else return R.drawable.ic_mooncloud;
            case "03":
                if(daytime == 'd'){
                    return R.drawable.ic_cloud;
                }
                else return R.drawable.ic_cloud;
            case "04":
                if(daytime == 'd'){
                    return R.drawable.ic_heavycloud;
                }
                else return R.drawable.ic_heavycloud;
            case "09":
                if(daytime == 'd'){
                    return R.drawable.ic_heavycloudrain;
                }
                else return R.drawable.ic_heavycloudrain;
            case "10":
                if(daytime == 'd'){
                    return R.drawable.ic_sunrain;
                }
                else return R.drawable.ic_moonrain;
            case "11":
                if(daytime == 'd'){
                    return R.drawable.ic_thunder;
                }
                else return R.drawable.ic_thunder;
            case "13":
                if(daytime == 'd'){
                    return R.drawable.ic_snow;
                }
                else return R.drawable.ic_snow;
            case "50":
                if(daytime == 'd'){
                    return R.drawable.ic_fog;
                }
                else return R.drawable.ic_fog;
            default:
                return R.drawable.ic_sun;
        }
    }

    /**
     * open daily-popup with animation
     */
    void openClothing(int day) {
        //Remove Background
        setInvisible();
        setInvisibleClothing();

        //Animation
        DisplayMetrics metrics = new DisplayMetrics();
        this.mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        TranslateAnimation animate = new TranslateAnimation(-width,0, 0, 0 );
        animate.setDuration(300);
        animate.setFillAfter(false);

        clothingLayout.startAnimation(animate);
        clothingLayout.setVisibility(View.VISIBLE);
        String textRain = this.mainActivity.getResources().getString(R.string.TextViewRain);
        String textNoRain = this.mainActivity.getResources().getString(R.string.TextViewNoRain);

        if(dailyRain != null) {
            if (dailyRain[day - 1] > 5)
                clothingText.setText(String.format(textRain, dailyRain[day - 1]));
            else
                clothingText.setText(String.format(textNoRain, dailyRain[day - 1]));
        }
        //Set Background
        switch (day){
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myClothingDay1.setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myClothingDay2.setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myClothingDay3.setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myClothingDay4.setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                break;
            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myClothingDay5.setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                break;
        }
    }

    void openDaily(int day){
        //Remove Background
        setInvisible();
        setInvisibleClothing();

        //Animation
        myScrollView.smoothScrollTo(0, myContainerHourly.getHeight() + myContainerTemp.getHeight() + 180);
        DisplayMetrics metrics = new DisplayMetrics();
        this.mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        TranslateAnimation animate = new TranslateAnimation(-width,0, 0, 0 );
        animate.setDuration(300);
        animate.setFillAfter(false);

        //Set Background
        switch (day){
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myDaily.get(0).setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                dailyFineLayout.get(0).startAnimation(animate);
                dailyFineLayout.get(0).setVisibility(View.VISIBLE);
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myDaily.get(1).setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                dailyFineLayout.get(1).startAnimation(animate);
                dailyFineLayout.get(1).setVisibility(View.VISIBLE);
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myDaily.get(2).setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                dailyFineLayout.get(2).startAnimation(animate);
                dailyFineLayout.get(2).setVisibility(View.VISIBLE);
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myDaily.get(3).setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                dailyFineLayout.get(3).startAnimation(animate);
                dailyFineLayout.get(3).setVisibility(View.VISIBLE);
                break;
            case 5:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    myDaily.get(4).setBackground(this.mainActivity.getDrawable(R.drawable.topcorners_rounded));
                dailyFineLayout.get(4).startAnimation(animate);
                dailyFineLayout.get(4).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * close daily-popup
     */
    void setInvisible(){
        for(LinearLayout daily: myDaily){
            daily.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
        }
        for(ConstraintLayout day: dailyFineLayout){
            day.setVisibility(View.GONE);
        }
    }

    void setInvisibleClothing(){
        clothingLayout.setVisibility(View.GONE);
        myClothingDay1.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
        myClothingDay2.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
        myClothingDay3.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
        myClothingDay4.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
        myClothingDay5.setBackgroundColor(this.mainActivity.getResources().getColor(android.R.color.transparent));
    }

}

