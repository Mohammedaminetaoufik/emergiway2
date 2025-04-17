package com.example.emergiway;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCommandsActivity extends AppCompatActivity {

    private Button getButton, postButton, deleteButton, backButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_commands);

        // Initialize UI components
        getButton = findViewById(R.id.getButton);
        postButton = findViewById(R.id.postButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Set click listeners
        getButton.setOnClickListener(v -> executeGetRequest());
        postButton.setOnClickListener(v -> executePostRequest());
        deleteButton.setOnClickListener(v -> executeDeleteRequest());
        backButton.setOnClickListener(v -> finish());
    }

    private void executeGetRequest() {
        new Thread(() -> {
            try {
                // Replace with your API endpoint
                URL url = java.net.URI.create("http://192.168.1.10:8000/api/test/get").toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                }

                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                final String result = response.toString();
                runOnUiThread(() -> {
                    resultTextView.setText("GET Response: " + result);
                    Toast.makeText(HttpCommandsActivity.this, "GET Request Executed", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    resultTextView.setText("GET Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void executePostRequest() {
        new Thread(() -> {
            try {
                // Replace with your API endpoint
                URL url = java.net.URI.create("http://192.168.1.10:8000/api/test/post").toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Sample data to send
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("key", "value");
                jsonInput.put("action", "test");

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                }

                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                final String result = response.toString();
                runOnUiThread(() -> {
                    resultTextView.setText("POST Response: " + result);
                    Toast.makeText(HttpCommandsActivity.this, "POST Request Executed", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    resultTextView.setText("POST Error: " + e.getMessage());
                });
            }
        }).start();
    }

    private void executeDeleteRequest() {
        new Thread(() -> {
            try {
                // Replace with your API endpoint
                URL url = java.net.URI.create("http://192.168.1.10:8000/api/test/delete").toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                }

                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                final String result = response.toString();
                runOnUiThread(() -> {
                    resultTextView.setText("DELETE Response: " + result);
                    Toast.makeText(HttpCommandsActivity.this, "DELETE Request Executed", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    resultTextView.setText("DELETE Error: " + e.getMessage());
                });
            }
        }).start();
    }
}
