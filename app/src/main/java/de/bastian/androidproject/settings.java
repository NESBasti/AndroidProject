package de.bastian.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class settings extends AppCompatActivity  {
    private Button languageButton;
    private View linearLayout;
    private Activity mainActivity;
    private ImageButton backbutton1;
    private ImageButton backbutton2;
    private ImageButton backbutton3;
    private ImageButton backbutton4;
    private ConstraintLayout mySettingsBackground;
    private Switch switchUnits;

    //Language
    private TextView textViewSettings;
    private TextView textViewUnit;
    private TextView textViewLanguage;
    private TextView textViewBackground;
    private TextView textViewContact;

    //Cities
    private TextView textViewCities;
    private TextView city1;
    private TextView city2;
    private TextView city3;
    private TextView city4;
    private TextView city5;

    private View myCity1delete;
    private View myCity2delete;
    private View myCity3delete;
    private View myCity4delete;
    private View myCity5delete;

    private int cityCounter;

    private EditText MyAddCity;
    private TextView MyAddCityButton;



    //Vibration
    private Vibrator myVib;

    //Cities
    private String[] mLocationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backbutton1 = findViewById(R.id.backgroundselection1);
        backbutton2 = findViewById(R.id.backgroundselection2);
        backbutton3 = findViewById(R.id.backgroundselection3);
        backbutton4 = findViewById(R.id.backgroundselection4);
        mySettingsBackground = findViewById(R.id.MySettingsBackground);
        switchUnits = findViewById(R.id.switch_einheit);

        mLocationList = loadArray("myCitynames");

        if(mLocationList.length == 0)
        {
            mLocationList = new String[6];
            saveArray(mLocationList, "myCitynames");
        }

        textViewCities = findViewById(R.id.MyTextViewCities);

        //Language
        textViewSettings = findViewById(R.id.MySettingsTag);
        textViewUnit = findViewById(R.id.MyTextViewUnit);
        textViewBackground = findViewById(R.id.MyTextViewBackground);
        textViewContact = findViewById(R.id.MyTextViewContact);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        //Language
        String TVSettings = getResources().getString(R.string.TextViewSettings);
        textViewSettings.setText(TVSettings);

        String TVUnit = getResources().getString(R.string.TextViewUnit);
        textViewUnit.setText(TVUnit);

        String TVBackground = getResources().getString(R.string.TextViewBackground);
        textViewBackground.setText(TVBackground);

        String TVContact = getResources().getString(R.string.TextViewContact);
        textViewContact.setText(TVContact);

        String backgroundcolor = preferences.getString("Background", "0");
        if (backgroundcolor != null) {
            switch (backgroundcolor) {
                case "1":
                    mySettingsBackground.setBackgroundResource(R.drawable.background_image);
                    break;
                case "2":
                    mySettingsBackground.setBackgroundResource(R.drawable.background_image1);
                    break;
                case "3":
                    mySettingsBackground.setBackgroundResource(R.drawable.background_image2);
                    break;
                case "4":
                    mySettingsBackground.setBackgroundResource(R.drawable.background_image3);
                    break;
                default:
                    break;
            }
        }

        String background = preferences.getString("Background", "0");
        backbutton1.setBackgroundColor(Color.BLACK);
        backbutton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
        backbutton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
        backbutton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        if (background != null) {
            backbutton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            switch (background) {
                case "1":
                    backbutton1.setBackgroundColor(Color.BLACK);
                    break;
                case "2":
                    backbutton2.setBackgroundColor(Color.BLACK);
                    break;
                case "3":
                    backbutton3.setBackgroundColor(Color.BLACK);
                    break;
                case "4":
                    backbutton4.setBackgroundColor(Color.BLACK);
                    break;
                default:
                    break;
            }
        }
        switchUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                editor.putString("UNIT", "imperial");
                editor.apply();
                //Fahrenheit
            }
            else
            {
                editor.putString("UNIT", "metric");
                editor.apply();
                //Celsius
            }
        }
    });

        if( preferences.getString("UNIT", "metric").compareTo("metric") == 0){
            switchUnits.setChecked(false);
        }
        else switchUnits.setChecked(true);

    }

    //Hardware Backbutton
    @Override
    public void onBackPressed() {
        Intent i = new Intent(settings.this, MainActivity.class);
        startActivity(i);
        finish();
        return;
    }

    public void MySettingsBackToMain(View view) {
        Intent i = new Intent(settings.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void setBackground1(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backbutton1.setBackgroundColor(Color.BLACK);
            backbutton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        Location targetLocation1 = new Location("");//provider name is unnecessary
        targetLocation1.setLatitude(10.0d);//your coords of course
        targetLocation1.setLongitude(20.0d);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","1");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        recreate();
    }
    public void setBackground2(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backbutton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton2.setBackgroundColor(Color.BLACK);
            backbutton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","2");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        recreate();
    }
    public void setBackground3(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backbutton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton3.setBackgroundColor(Color.BLACK);
            backbutton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","3");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        recreate();
    }
    public void setBackground4(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backbutton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backbutton4.setBackgroundColor(Color.BLACK);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","4");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        recreate();
    }


    public boolean saveArray(String[] array, String arrayName) {
        SharedPreferences prefs = getSharedPreferences("sharedLocations", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, (String) array[i]);
        return editor.commit();
    }
    public String[] loadArray(String arrayName) {
        SharedPreferences prefs = getSharedPreferences("sharedLocations", MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public void AddCity(View view) {
        Geocoder geocoder = new Geocoder(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityCounter = preferences.getInt("CityCount", 0);
        cityCounter++;
        mLocationList = loadArray("myCitynames");
        MyAddCity = findViewById(R.id.addCity);
        try {
            List<Address> address = geocoder.getFromLocationName(MyAddCity.getText().toString(), 5);
            if (!address.isEmpty()) {
                if (address.get(0).getLatitude() != 0.0) {
                    String content = MyAddCity.getText().toString(); //gets you the contents of edit text
                    mLocationList[cityCounter] = address.get(0).getFeatureName();
                    saveArray(mLocationList, "myCitynames");

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("CityCount", cityCounter);
                    editor.apply();
                    MyCities(view);
                    MyCities(view);
                }
            }
            else
            {
                Toast.makeText(this, "Stadt konnte nicht gefunden werden", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void CityDelete1(View view)
    {
        CityDelete(1, view);
    }
    public void CityDelete2(View view)
    {
        CityDelete(2, view);
    }
    public void CityDelete3(View view)
    {
        CityDelete(3, view);
    }
    public void CityDelete4(View view)
    {
        CityDelete(4, view);
    }
    public void CityDelete5(View view)
    {
        CityDelete(5, view);
    }

    public void CityDelete(int cityNumber, View view)
    {
        mLocationList = loadArray("myCitynames");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityCounter = preferences.getInt("CityCount", 0);
        System.arraycopy(mLocationList, cityNumber + 1, mLocationList, cityNumber, cityCounter - cityNumber);
        mLocationList[cityCounter] = null;
        cityCounter--;
        saveArray(mLocationList, "myCitynames");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CityCount",cityCounter);
        editor.apply();
        MyCities(view);
        MyCities(view);
    }

    public void MyCities(View view) {
            linearLayout = findViewById(R.id.container_cities);

            if(linearLayout.getVisibility() == View.VISIBLE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textViewCities.setBackgroundColor(this.getResources().getColor(android.R.color.transparent));
                }
                linearLayout.setVisibility(View.GONE);

            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textViewCities.setBackground(this.getDrawable(R.drawable.topcorners_rounded));
                }
                linearLayout.setVisibility(View.VISIBLE);
            }

            myCity1delete = findViewById(R.id.MyCityDelete1);
            myCity2delete = findViewById(R.id.MyCityDelete2);
            myCity3delete = findViewById(R.id.MyCityDelete3);
            myCity4delete = findViewById(R.id.MyCityDelete4);
            myCity5delete = findViewById(R.id.MyCityDelete5);
            MyAddCity = findViewById(R.id.addCity);
            MyAddCityButton = findViewById(R.id.addCityButton);
            mLocationList = loadArray("myCitynames");

            myCity1delete.setVisibility(View.GONE);
            myCity2delete.setVisibility(View.GONE);
            myCity3delete.setVisibility(View.GONE);
            myCity4delete.setVisibility(View.GONE);
            myCity5delete.setVisibility(View.GONE);

            city1 = findViewById(R.id.MyCity1);
            city2 = findViewById(R.id.MyCity2);
            city3 = findViewById(R.id.MyCity3);
            city4 = findViewById(R.id.MyCity4);
            city5 = findViewById(R.id.MyCity5);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            cityCounter = preferences.getInt("CityCount", 0);

            for (int i = 1; i < mLocationList.length; i++) {
                if (i <= cityCounter) {
                    switch (i) {
                        case 1:
                            myCity1delete.setVisibility(View.VISIBLE);
                            city1.setText(mLocationList[i]);
                            MyAddCityButton.setVisibility(View.VISIBLE);
                            MyAddCity.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            myCity2delete.setVisibility(View.VISIBLE);
                            city2.setText(mLocationList[i]);
                            break;
                        case 3:
                            myCity3delete.setVisibility(View.VISIBLE);
                            city3.setText(mLocationList[i]);
                            break;
                        case 4:
                            myCity4delete.setVisibility(View.VISIBLE);
                            city4.setText(mLocationList[i]);
                            break;
                        case 5:
                            myCity5delete.setVisibility(View.VISIBLE);
                            city5.setText(mLocationList[i]);
                            MyAddCityButton.setVisibility(View.GONE);
                            MyAddCity.setVisibility(View.GONE);


                            break;
                        default:
                            break;
                    }

                }
            }
    }

}
