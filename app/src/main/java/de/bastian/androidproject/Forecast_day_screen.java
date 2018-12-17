package de.bastian.androidproject;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Forecast_day_screen extends AppCompatActivity {

    LinearLayout tag1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_day_screen);
        this.tag1 = findViewById(R.id.MyDay1Forecast);
    }


    public void ButtonDay1Pressed(View view) {
        Intent i = new Intent(Forecast_day_screen.this, Forecast_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_day_screen.this, tag1, ViewCompat.getTransitionName(tag1));
        startActivity(i, options.toBundle());
    }
}
