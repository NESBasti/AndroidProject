package de.bastian.androidproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
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
import com.google.gson.Gson;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // GPS
    private Location location;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long UPDATE_INTERVAL = 120000, FASTEST_INTERVAL = 120000; // = 120 seconds
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private LocationManager locationManager;

    // JSON
    private static String appid = "cfe31ebef1a89f6504ab9bac85dab8c4";
    private int updateFrequency = 120000; // = 120 seconds
    private SharedPreferences  mPrefs;


    //Swipe Refresh
    private SwipeRefreshLayout swipeLayout;

    //User Interface
    private UserInterface ui;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mPrefs = getPreferences(MODE_PRIVATE);

        ui = new UserInterface(this);

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

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                location = locationResult.getLastLocation();
                onLocationChanged(locationResult.getLastLocation());
            }

        };

        //Swipe Refresh
        // Getting SwipeContainerLayout
        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                ui.setLastUpdate(0L);
                getJSON();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false); //stop animation after 4 seconds
                    }
                }, 4000);
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

    }


    /**
     *      Uses Retrofit and GSON Converter to grab a JSON of current weather and the
     *      5-day-weather-forecast from openweathermap.org
     */
    private void getJSON(){
        if((System.currentTimeMillis() > ui.getLastUpdate() + updateFrequency) & location != null){
            LoadCurrentJSON loadCurrentJSON = new LoadCurrentJSON(MainActivity.this);
            loadCurrentJSON.execute(location);
            LoadForecastJSON loadForecastJSON = new LoadForecastJSON(MainActivity.this);
            loadForecastJSON.execute(location);
            ui.setLastUpdate(System.currentTimeMillis());
            ui.setLocation(this.location);
        }
        else if(location == null & locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) & locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     *      sets currentWeather when it got fetched,
     *      called in LoadCurrentJSON
     */
    public void updateCurrentWeather(WeatherCurrent current){
        ui.setWeatherCurrent(current);
        ui.updateInterface();
    }

    /**
     *      sets weatherForecast when it got fetched,
     *      called in LoadForecastJSON
     */
    public void updateWeatherForecast(WeatherForecast forecast){
        ui.setWeatherForecast(forecast);
        ui.updateInterface();
    }



    //region GPS functions

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
        startLocationUpdates();

        if (location != null) {
            getJSON();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        if(!(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) & location == null) {
            highAccuracyDisabled();
        }
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
                                            requestPermissions(permissionsRejected.
                                                toArray(new String[0]), ALL_PERMISSIONS_RESULT);

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
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    /**
     *      gets called when high accuracy for location is disabled,
     *      opens settings for user to enable it
     */
    private void highAccuracyDisabled(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enable high accuracy for location");
        builder.setMessage("Do you want to open settings?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
                googleApiClient.disconnect();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                googleApiClient.disconnect();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }
    // endregion

    //region save content
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        if(ui.getWeatherCurrent() != null){
            String jsonCurrent = gson.toJson(ui.getWeatherCurrent());
            prefsEditor.putString("weatherCurrent", jsonCurrent);
        }
        if(ui.getWeatherForecast() != null){
            String jsonForecast = gson.toJson(ui.getWeatherForecast());
            prefsEditor.putString("weatherForecast", jsonForecast);
        }
        String jsonUpdate = gson.toJson(ui.getLastUpdate());
        prefsEditor.putString("lastUpdate", jsonUpdate);
        //String jsonLocation = gson.toJson(location);
        //prefsEditor.putString("location", jsonLocation);
        prefsEditor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        String jsonCurrent = mPrefs.getString("weatherCurrent", "");
        if(gson.fromJson(jsonCurrent, WeatherCurrent.class) != null){
            ui.setWeatherCurrent(gson.fromJson(jsonCurrent, WeatherCurrent.class));
        }
        String jsonForecast = mPrefs.getString("weatherForecast", "");
        if(gson.fromJson(jsonForecast, WeatherForecast.class) != null){
            ui.setWeatherForecast(gson.fromJson(jsonForecast, WeatherForecast.class));
        }
        String jsonUpdate = mPrefs.getString("lastUpdate", "0");
        ui.setLastUpdate(gson.fromJson(jsonUpdate, Long.class));
        ui.updateInterface();
    }
    //endregion

      public void MySettingsOC(View view)
    {
        Intent i = new Intent(MainActivity.this, settings.class);
        startActivity(i);
    }



}