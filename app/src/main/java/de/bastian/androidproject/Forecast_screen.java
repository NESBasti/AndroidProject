package de.bastian.androidproject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Forecast_screen extends AppCompatActivity {
    float x1, x2, y1, y2;
    TextView buttontag1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_screen);
        buttontag1 = findViewById(R.id.MyDay1);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }
}
