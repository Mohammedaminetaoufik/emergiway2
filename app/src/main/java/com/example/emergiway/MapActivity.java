package com.example.emergiway;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        // Add back button functionality
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Go back to previous activity
        });
        
        // Here you would typically initialize your map
        // This is a placeholder for map initialization
        Toast.makeText(this, "Map loaded", Toast.LENGTH_SHORT).show();
    }
}
