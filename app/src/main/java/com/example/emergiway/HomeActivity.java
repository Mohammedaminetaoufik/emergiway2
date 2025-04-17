package com.example.emergiway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button mapButton;
    private Button httpCommandsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        mapButton = findViewById(R.id.mapButton);
        httpCommandsButton = findViewById(R.id.httpCommandsButton);

        // Set click listeners
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MapActivity.class);
            startActivity(intent);
        });

        httpCommandsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HttpCommandsActivity.class);
            startActivity(intent);
        });
    }
}
