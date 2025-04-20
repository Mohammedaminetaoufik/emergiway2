package com.example.emergiway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private boolean isMapnik = true;
    private boolean isNightMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );

        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Initial map style
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(16.0); // Good zoom for city-level detail
        mapView.getController().setCenter(new GeoPoint(31.6300, -8.0089)); // Marrakech, Morocco

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showUserLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize switchTileButton
        Button switchTileButton = findViewById(R.id.btnSwitchTile);
        switchTileButton.setOnClickListener(v -> {
            if (isMapnik) {
                mapView.setTileSource(TileSourceFactory.USGS_TOPO); // Switching tile source
                Toast.makeText(this, "Switched to USGS_TOPO", Toast.LENGTH_SHORT).show();
            } else {
                mapView.setTileSource(TileSourceFactory.MAPNIK);
                Toast.makeText(this, "Switched to MAPNIK", Toast.LENGTH_SHORT).show();
            }
            isMapnik = !isMapnik;
        });

        // Add marker button
        Button addMarkerButton = findViewById(R.id.btnAddMarker);
        addMarkerButton.setOnClickListener(v -> {
            GeoPoint point = (GeoPoint) mapView.getMapCenter();
            addMarker(point);
            saveLocation(point);
            Toast.makeText(this, "Marker added and saved", Toast.LENGTH_SHORT).show();
        });

        // Night mode button
        Button nightModeButton = findViewById(R.id.btnNightMode);
        nightModeButton.setOnClickListener(v -> {
            isNightMode = !isNightMode;
            mapView.setUseDataConnection(!isNightMode); // not really night mode, but a trick to trigger refresh
            Toast.makeText(this, isNightMode ? "Night mode ON (simulated)" : "Night mode OFF", Toast.LENGTH_SHORT).show();
        });

        loadSavedMarker();
    }

    private void showUserLocation() {
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);
    }

    private void addMarker(GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Saved Marker");
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void saveLocation(GeoPoint point) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("savedLat", String.valueOf(point.getLatitude()))
                .putString("savedLon", String.valueOf(point.getLongitude()))
                .apply();
    }

    private void loadSavedMarker() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lat = prefs.getString("savedLat", null);
        String lon = prefs.getString("savedLon", null);
        if (lat != null && lon != null) {
            GeoPoint point = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
            addMarker(point);
            mapView.getController().setCenter(point);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
