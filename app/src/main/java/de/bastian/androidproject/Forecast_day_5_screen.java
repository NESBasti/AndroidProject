package de.bastian.androidproject;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Forecast_day_5_screen extends AppCompatActivity {

    LinearLayout tag5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_day_5_screen);
        this.tag5 = findViewById(R.id.MyDay5Forecast);
    }

    public void ButtonDay5Pressed(View view) {
        Intent i = new Intent(Forecast_day_5_screen.this, Forecast_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_day_5_screen.this, tag5, ViewCompat.getTransitionName(tag5));
        startActivity(i, options.toBundle());
    }
}
