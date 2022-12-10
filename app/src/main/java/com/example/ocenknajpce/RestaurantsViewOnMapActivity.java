package com.example.ocenknajpce;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ocenknajpce.databinding.ActivityRestaurantsViewOnMapBinding;

import java.util.ArrayList;

public class RestaurantsViewOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRestaurantsViewOnMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRestaurantsViewOnMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle data = getIntent().getBundleExtra("restaurantsData");
        ArrayList<Restaurant> restaurantData = (ArrayList<Restaurant>) data.getSerializable("restaurantListOfObjects");
        ArrayList<MarkerOptions> markers = new ArrayList<>();

        for(Restaurant restaurant : restaurantData) {
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(restaurant.getLat(), restaurant.getLong()))
                    .title(restaurant.resName);
            markers.add(marker);
        }

        for(MarkerOptions marker : markers) {
            mMap.addMarker(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
    }
}