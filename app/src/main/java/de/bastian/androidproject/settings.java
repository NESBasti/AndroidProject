package de.bastian.androidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    private Button languageButton;
    private LinearLayout linearLayout;
    private Activity mainActivity;
    private ImageButton backbutton1;
    private ImageButton backbutton2;
    private ImageButton backbutton3;
    private ImageButton backbutton4;
    private Switch switchEinheit;

    //Language
    private TextView textViewSettings;
    private TextView textViewUnit;
    private TextView textViewLanguage;
    private TextView textViewBackground;
    private TextView textViewContact;


    //Vibration
    private Vibrator myVib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        this.mainActivity = mainActivity;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        languageButton = findViewById(R.id.MyLanguageButton);
        linearLayout = findViewById(R.id.background);
        backbutton1 = findViewById(R.id.backgroundselection1);
        backbutton2 = findViewById(R.id.backgroundselection2);
        backbutton3 = findViewById(R.id.backgroundselection3);
        backbutton4 = findViewById(R.id.backgroundselection4);
        switchEinheit = findViewById(R.id.switch_einheit);
        //Language
        textViewSettings = findViewById(R.id.MySettingsTag);
        textViewUnit = findViewById(R.id.MyTextViewUnit);
        textViewLanguage = findViewById(R.id.MyTextViewLanguage);
        textViewBackground = findViewById(R.id.MyTextViewBackground);
        textViewContact = findViewById(R.id.MyTextViewContact);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        //Language
        String TVSettings = getResources().getString(R.string.TextViewSettings);
        textViewSettings.setText(TVSettings);

        String TVUnit = getResources().getString(R.string.TextViewUnit);
        textViewUnit.setText(TVUnit);

        String TVLanguage = getResources().getString(R.string.TextViewLanguage);
        textViewLanguage.setText(TVLanguage);

        String TVBackground = getResources().getString(R.string.TextViewBackground);
        textViewBackground.setText(TVBackground);

        String TVContact = getResources().getString(R.string.TextViewContact);
        textViewContact.setText(TVContact);


        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(settings.this, languageButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.language_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.one:
                                languageButton.setText("Deutsch");
                                editor.putString("Language","de");
                                editor.apply();
                                Toast.makeText(
                                        settings.this,
                                        "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.two:
                                languageButton.setText("English");
                                editor.putString("Language","en");
                                editor.apply();
                                Toast.makeText(
                                        settings.this,
                                        "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        String language = preferences.getString("Language", "en");

        if(language != null) {
            if (language.equals("en")) {
                languageButton.setText("English");
            }
            if (language.equals("de")) {
                languageButton.setText("Deutsch");
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
        switchEinheit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                //Fahrenheit
            }
            else
            {
                //Celsius
            }
        }
    });
    }





    public void MySettingsBackToMain(View view) {
        Intent i = new Intent(settings.this, MainActivity.class);
        startActivity(i);
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
    }
}
