package de.bastian.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void MySettingsBackToMain(View view) {
        Intent i = new Intent(settings.this, MainActivity.class);
        startActivity(i);
    }
}
