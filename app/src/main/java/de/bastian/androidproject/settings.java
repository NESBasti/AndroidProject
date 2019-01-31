package de.bastian.androidproject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity  {
    private Button languageButton;
    private LinearLayout linearLayout;
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



    //Vibration
    private Vibrator myVib;


    //Hardware Backbutton
    @Override
    public void onBackPressed() {
        Intent i = new Intent(settings.this, MainActivity.class);
        startActivity(i);
        finish();
        return;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        this.mainActivity = mainActivity;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        linearLayout = findViewById(R.id.background);
        backbutton1 = findViewById(R.id.backgroundselection1);
        backbutton2 = findViewById(R.id.backgroundselection2);
        backbutton3 = findViewById(R.id.backgroundselection3);
        backbutton4 = findViewById(R.id.backgroundselection4);
        mySettingsBackground = findViewById(R.id.MySettingsBackground);
        switchUnits = findViewById(R.id.switch_einheit);

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


}
