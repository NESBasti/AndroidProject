package de.bastian.androidproject;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    // GPS
    private Location location;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 120000, FASTEST_INTERVAL = 120000; // = 120 seconds
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    // XML
    private TextView cityName;
    private TextView lastRefresh;
    private TextView temperature;
    private TextView minTemp;
    private TextView maxTemp;
    private ListView weatherData;
    float x1, x2, y1, y2;

    // JSON
    private static String appid = "cfe31ebef1a89f6504ab9bac85dab8c4";
    private WeatherForecast weatherForecast;
    private WeatherCurrent weatherCurrent;
    private Long lastUpdate = 0L;
    private int updateFrequency = 120000; // = 120 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Views
        cityName = findViewById(R.id.MyCityName);
        lastRefresh = findViewById(R.id.MyLastRefresh);
        temperature = findViewById(R.id.MyTemperature);
        minTemp = findViewById(R.id.MyMinTemp);
        maxTemp = findViewById(R.id.MyMaxTemp);
        weatherData = findViewById(R.id.weatherData);

        // adding permission to request the location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[0]), ALL_PERMISSIONS_RESULT);
            }
        }

        //initialize fusedLocationProviderClient and locationCallback
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                onLocationChanged(locationResult.getLastLocation());
            }

        };

        //building api client
        //buildGoogleApiClient();

    }

    /**
     *      Uses Retrofit and GSON Converter to grab a JSON of current weather and the
     *      5-day-weather-forecast from openweathermap.org
     */
    private void getJSON(){
        if(System.currentTimeMillis() > lastUpdate + updateFrequency){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            if(location != null) {
                Call<WeatherForecast> call1 = api.getWeatherForecastFromCoordinates(appid, location.getLatitude(), location.getLongitude(), "metric", "de");

                call1.enqueue(new Callback<WeatherForecast>() {
                    @Override
                    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                        lastUpdate = System.currentTimeMillis();
                        weatherForecast = response.body();
                        updateInterface();

                    }

                    @Override
                    public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                    }
                });

                Call<WeatherCurrent> call2 = api.getCurrentWeatherFromCoordinates(appid, location.getLatitude(), location.getLongitude(), "metric","de");

                call2.enqueue(new Callback<WeatherCurrent>() {
                    @Override
                    public void onResponse(Call<WeatherCurrent> call, Response<WeatherCurrent> response) {
                        weatherCurrent = response.body();
                        lastUpdate = System.currentTimeMillis();
                        updateInterface();
                    }

                    @Override
                    public void onFailure(Call<WeatherCurrent> call, Throwable t) {

                    }
                });
            }
        }

    }


    /**
     *      Updates the UI if a new JSON was fetched
     */
    private void updateInterface(){
        if(weatherCurrent != null){
            cityName.setText(weatherCurrent.getName());
            lastRefresh.setText(new java.util.Date(lastUpdate).toString());
            temperature.setText(String.valueOf((int) weatherCurrent.getMain().getTemp()) + "°C");
            minTemp.setText("Min " + String.valueOf((int) weatherCurrent.getMain().getTemp_min()) + "°C");
            maxTemp.setText("Max " + String.valueOf((int) weatherCurrent.getMain().getTemp_max()) + "°C");
        }


        if(weatherForecast != null){
            String[] values = new String[]{"cod " + String.valueOf(weatherForecast.getCod()), "message " + String.valueOf(weatherForecast.getMessage()), "cnt " + String.valueOf(weatherForecast.getCnt()), "list " + String.valueOf(weatherForecast.getList().get(1).getMain().getTemp())};

            weatherData.setAdapter(
                    new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            values
                    )

            );
        }

    }

    /****************************************

            GPS functions

     ***************************************/

    private void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        googleApiClient.connect();
    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (googleApiClient != null && fusedLocationProviderClient != null) {
            startLocationUpdates();
        }else if(!checkPlayServices()) {
            Toast.makeText(this, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }else {
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient != null  &&  googleApiClient.isConnected()) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation();

        if (location != null) {
            getJSON();
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            getJSON();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /**
     *
     *
     * @param touchevent
     * @return
     */

    //Swap Sides
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch(touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if(x1 > x2)
                {
                    Intent i = new Intent(MainActivity.this, Forecast_screen.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                        return true;
                        //finish();
                    }
                }
                break;

            default:
                break;
        }
        return false;
    }
}