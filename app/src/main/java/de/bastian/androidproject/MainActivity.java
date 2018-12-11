package de.bastian.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button lastLocation = findViewById(R.id.btn_get_last_location);
    private TextView locationResult = findViewById(R.id.location_result);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText( this,  "yeeahhh", Toast.LENGTH_LONG).show();

        String s = this.editText.getText().toString();
        String result = "My Name is" + s;

        this.editText.setText(result);
    }
}
