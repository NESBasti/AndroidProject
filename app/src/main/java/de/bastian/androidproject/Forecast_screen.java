package de.bastian.androidproject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Forecast_screen extends AppCompatActivity {
    float x1, x2, y1, y2;
    TextView buttontag1;
    LinearLayout tag1;
    LinearLayout tag2;
    LinearLayout tag3;
    LinearLayout tag4;
    LinearLayout tag5;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_screen);
        buttontag1 = findViewById(R.id.MyDay1);
        tag1 = findViewById(R.id.MyDay1Forecast);
        tag2 = findViewById(R.id.MyDay2Forecast);
        tag3 = findViewById(R.id.MyDay3Forecast);
        tag4 = findViewById(R.id.MyDay4Forecast);
        tag5 = findViewById(R.id.MyDay5Forecast);
    }



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

                if(x1 < x2)
                {
                    Intent i = new Intent(Forecast_screen.this, MainActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                        //finish();
                    }
                }
                break;

            default:
                break;
        }
        return false;
    }

    public void ButtonDay1Pressed(View view) {
        Intent i = new Intent(Forecast_screen.this, Forecast_day_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_screen.this, tag1, ViewCompat.getTransitionName(tag1));
        startActivity(i, options.toBundle());

    }


    public void ButtonDay2Pressed(View view) {
        Intent i = new Intent(Forecast_screen.this, Forecast_day_2_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_screen.this, tag2, ViewCompat.getTransitionName(tag2));
        startActivity(i, options.toBundle());

    }

    public void ButtonDay3Pressed(View view) {
        Intent i = new Intent(Forecast_screen.this, Forecast_day_3_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_screen.this, tag3, ViewCompat.getTransitionName(tag3));
        startActivity(i, options.toBundle());

    }

    public void ButtonDay4Pressed(View view) {
        Intent i = new Intent(Forecast_screen.this, Forecast_day_4_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_screen.this, tag4, ViewCompat.getTransitionName(tag4));
        startActivity(i, options.toBundle());

    }

    public void ButtonDay5Pressed(View view) {
        Intent i = new Intent(Forecast_screen.this, Forecast_day_5_screen.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Forecast_screen.this, tag5, ViewCompat.getTransitionName(tag5));
        startActivity(i, options.toBundle());
    }
}
