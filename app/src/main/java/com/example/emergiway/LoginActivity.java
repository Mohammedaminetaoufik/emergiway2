package com.example.emergiway;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText matriculeInput, passwordInput;
    Button loginBtn;
    TextView resultText;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        matriculeInput = findViewById(R.id.matriculeInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        resultText = findViewById(R.id.resultText);
        loader = findViewById(R.id.loader);

        // Set click listener for login button
        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String matricule = matriculeInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (matricule.equals("anwar") && password.equals("1234")) {
            Toast.makeText(LoginActivity.this, "Connexion test réussie", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;}
        // Check for empty input fields
        if (matricule.isEmpty() || password.isEmpty()) {
            resultText.setText("Veuillez entrer le matricule et le mot de passe.");
            return;
        }

        loader.setVisibility(View.VISIBLE);
        resultText.setText("");

        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.10:8000/api/utilisateur/login"); // li bgha yjarab o endo android ydir ip config o ychof l'adress ip li kykhdm b android emulator ydir http://10.0.2.2:8000/api/utilisateur/login
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("matricule", matricule);
                jsonInput.put("password", password);

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

                JSONObject jsonResponse = new JSONObject(response.toString());

                runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    if (responseCode == 200) {
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        // Redirect to MainActivity (Home page)
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Finish login activity to prevent user from returning to it
                    } else {
                        resultText.setText("Échec de la connexion: " + jsonResponse.optString("error", "Erreur inconnue"));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    resultText.setText("Erreur réseau: " + e.getMessage());
                });
            }
        }).start();
    }
}
