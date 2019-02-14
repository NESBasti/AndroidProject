package de.bastian.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Settings extends AppCompatActivity  {
    private View linearLayout;
    private ImageButton backButton1;
    private ImageButton backButton2;
    private ImageButton backButton3;
    private ImageButton backButton4;
    private LinearLayout mySettingsBackground;
    private Switch unitSwitch;

    //Language
    private TextView textViewSettings;
    private TextView textViewUnit;
    private TextView textViewBackground;
    private TextView textViewContact;

    //Cities
    private TextView textViewLocation;
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

    private ImageView myArrowDownView;
    private ImageView myArrowUpView;

    private EditText myAddCity;
    private TextView myAddCityButton;

    //Vibration
    private Vibrator myVib;

    //Cities
    private String[] mLocationList;

    //Seekbar
    private int userTemp;
    private SeekBar seekBar;
    private TextView seekBarMin;
    private TextView seekBarMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backButton1 = findViewById(R.id.backgroundselection1);
        backButton2 = findViewById(R.id.backgroundselection2);
        backButton3 = findViewById(R.id.backgroundselection3);
        backButton4 = findViewById(R.id.backgroundselection4);
        mySettingsBackground = findViewById(R.id.MySettingsBackground);
        unitSwitch = findViewById(R.id.switch_einheit);
        myAddCity = findViewById(R.id.addCity);

        mLocationList = loadArray("myCitynames");

        if(mLocationList.length == 0)
        {
            mLocationList = new String[6];
            saveArray(mLocationList, "myCitynames");
        }

        textViewCities = findViewById(R.id.MyTextViewCities);
        myArrowUpView = findViewById(R.id.myArrowUp);
        myArrowDownView = findViewById(R.id.myArrowDown);

        //Language
        textViewSettings = findViewById(R.id.MySettingsTag);
        textViewUnit = findViewById(R.id.MyTextViewUnit);
        textViewBackground = findViewById(R.id.MyTextViewBackground);
        textViewContact = findViewById(R.id.MyTextViewContact);
        textViewLocation = findViewById(R.id.MyLocation);

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

        String TVCities = getResources().getString(R.string.TextViewCities);
        textViewCities.setText(TVCities);

        String TVLocation = getResources().getString(R.string.TextViewLocation);
        textViewLocation.setText(TVLocation);

        String TVAddCity = getResources().getString(R.string.TextViewAddCity);
        myAddCity.setHintTextColor(getResources().getColor(R.color.Gray));
        myAddCity.setHint(TVAddCity);

        String backgroundColor = preferences.getString("Background", "0");
        if (backgroundColor != null) {
            switch (backgroundColor) {
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
        backButton1.setBackgroundColor(Color.BLACK);
        backButton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
        backButton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
        backButton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        if (background != null) {
            backButton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            switch (background) {
                case "1":
                    backButton1.setBackgroundColor(Color.BLACK);
                    break;
                case "2":
                    backButton2.setBackgroundColor(Color.BLACK);
                    break;
                case "3":
                    backButton3.setBackgroundColor(Color.BLACK);
                    break;
                case "4":
                    backButton4.setBackgroundColor(Color.BLACK);
                    break;
                default:
                    break;
            }
        }
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        try {
            if (preferences.getString("UNIT", "metric").compareTo("metric") == 0) {
                unitSwitch.setChecked(false);
                userTemp = 5;
            } else
            {
                unitSwitch.setChecked(true);
                userTemp = 9;
            }
        }
        catch (NullPointerException e){
            unitSwitch.setChecked(false);
            userTemp = 5;
        }

        //Seekbar
        seekBar = findViewById(R.id.MySeekBar);
        seekBarMax = findViewById(R.id.MySliderMax);
        seekBarMin = findViewById(R.id.MySliderMin);
        seekBarMin.setText("-"+ userTemp);
        seekBarMax.setText("+"+ userTemp);
        seekBar.setProgress(preferences.getInt("UserTemp", 0) + userTemp);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() - userTemp;
                editor.putInt("UserTemp", progress);
                editor.apply();
            }
        });
    }



    //Hardware Backbutton
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Settings.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        //finish();
    }

    public void MySettingsBackToMain(View view) {
        Intent i = new Intent(Settings.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        //finish();
    }

    public void setBackground1(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backButton1.setBackgroundColor(Color.BLACK);
            backButton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        Location targetLocation1 = new Location("");//provider name is unnecessary
        targetLocation1.setLatitude(10.0d);//your coords of course
        targetLocation1.setLongitude(20.0d);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","1");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        mySettingsBackground.setBackgroundResource(R.drawable.background_image);
    }
    public void setBackground2(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backButton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton2.setBackgroundColor(Color.BLACK);
            backButton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","2");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        mySettingsBackground.setBackgroundResource(R.drawable.background_image1);
    }
    public void setBackground3(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backButton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton3.setBackgroundColor(Color.BLACK);
            backButton4.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","3");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        mySettingsBackground.setBackgroundResource(R.drawable.background_image2);
    }
    public void setBackground4(View view) {
        myVib.vibrate(50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backButton1.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton2.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton3.setBackgroundColor(getResources().getColor(R.color.lightGray));
            backButton4.setBackgroundColor(Color.BLACK);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Background","4");
        editor.apply();
        Toast.makeText(this, "Hintergrund erfolgreich geändert", Toast.LENGTH_SHORT).show();
        mySettingsBackground.setBackgroundResource(R.drawable.background_image3);
    }


    public void saveArray(String[] array, String arrayName) {
        SharedPreferences prefs = getSharedPreferences("sharedLocations", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        editor.apply();
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
        try {
            List<Address> address = geocoder.getFromLocationName(myAddCity.getText().toString(), 5);
            if (!address.isEmpty()) {
                if (address.get(0).getLatitude() != 0.0) {
                    if(address.get(0).getLocality() != null){
                        mLocationList[cityCounter] = address.get(0).getLocality();
                    }else
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

        myAddCity.setText("");

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
                myArrowDownView.setVisibility(View.VISIBLE);
                myArrowUpView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

            }
            else {
                myArrowDownView.setVisibility(View.GONE);
                myArrowUpView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            myCity1delete = findViewById(R.id.MyCityDelete1);
            myCity2delete = findViewById(R.id.MyCityDelete2);
            myCity3delete = findViewById(R.id.MyCityDelete3);
            myCity4delete = findViewById(R.id.MyCityDelete4);
            myCity5delete = findViewById(R.id.MyCityDelete5);
            myAddCityButton = findViewById(R.id.addCityButton);
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
                            myAddCityButton.setVisibility(View.VISIBLE);
                            myAddCity.setVisibility(View.VISIBLE);
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
                            myAddCityButton.setVisibility(View.GONE);
                            myAddCity.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }

                }
            }
    }

    public void ShowSliderInformations(View view) {
        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) Settings.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.informations,null);

        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, 800, 250, true);

        //Set up touch closing outside of pop-up
        pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Gray)));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        // display the pop-up in the center
        pw.showAtLocation(layout, Gravity.RIGHT, 32, -512);
    }
}
