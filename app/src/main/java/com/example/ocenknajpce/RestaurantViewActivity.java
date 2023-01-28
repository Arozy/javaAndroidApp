package com.example.ocenknajpce;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView name, phone;
    private float MapZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        MapZoom = 16;
        name = findViewById(R.id.tv_restaurantName);
        phone = findViewById(R.id.tv_restaurantPhone);

        Intent intent = getIntent();
        Restaurant restaurantObj = (Restaurant) intent.getExtras().getSerializable("OnlyOneRestaurantObject");

            double restaurantLat = restaurantObj != null ? restaurantObj.getLat() : 0.0;
            double restaurantLong = restaurantObj != null ? restaurantObj.getLong() : 0.0;

            name.setText(restaurantObj.getResName());
            phone.setText(restaurantObj.getResPhone());

            // Add a marker in Sydney and move the camera
            LatLng restaurant = new LatLng(restaurantLat, restaurantLong);
            mMap.setMinZoomPreference(MapZoom);
            mMap.addMarker(new MarkerOptions().position(restaurant).title(restaurantObj.getResName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurant));
    }

    public void onErrorGoMainActivity() {
        Toast.makeText(getApplicationContext(), R.string.restaurant_cannot_show_on_map, Toast.LENGTH_LONG).show();
        this.finish();
    }
}