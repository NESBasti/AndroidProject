package de.bastian.androidproject;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Forecast_day_4_screen extends AppCompatActivity {

    LinearLayout tag4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_day_4_screen);
        this.tag4 = findViewById(R.id.MyDay4Forecast);
    }

    public void ButtonDay4Pressed(View view) {
        Intent i = new Intent(Forecast_day_4_screen.this, Forecast_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_day_4_screen.this, tag4, ViewCompat.getTransitionName(tag4));
        startActivity(i, options.toBundle());
    }
}
