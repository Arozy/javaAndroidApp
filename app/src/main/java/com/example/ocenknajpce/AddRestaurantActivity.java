package com.example.ocenknajpce;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AddRestaurantActivity extends AppCompatActivity {
    String gps_lat = "";
    String gps_long = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        Button btn_addRestaurantSubmit = findViewById(R.id.btn_addRestaurantSubmit);
        Button btn_addRestaurantGetGPS = findViewById(R.id.btn_addRestaurantGetGPS);
        TextView tv_Position = findViewById(R.id.tv_coordinates);
        EditText et_RestaurantName = findViewById(R.id.et_addRestaurantName);
        EditText et_RestaurantPhone = findViewById(R.id.et_addRestaurantPhone);

        SharedPreferences preferences = getSharedPreferences("userPreferences", Activity.MODE_PRIVATE);
        String userId = preferences.getString("userId", "0");

        String locationProvider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        btn_addRestaurantGetGPS.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(AddRestaurantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddRestaurantActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddRestaurantActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 100);
            }
            locationManager.requestLocationUpdates(locationProvider, 5000, 50, location -> {
                gps_lat = ""+location.getLatitude();
                gps_long = ""+location.getLongitude();

                tv_Position.setText(gps_lat+" "+gps_long);
            });
        });

        btn_addRestaurantSubmit.setOnClickListener(view -> {
            String userName = et_RestaurantName.getText().toString();
            String userPhone = et_RestaurantPhone.getText().toString();

            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://dev.imagit.pl/mobilne/api/restaurant/add";

            RequestParams params = new RequestParams();
            params.put("user", userId);
            params.put("name", userName);
            params.put("phone", userPhone);
            params.put("lat", gps_lat);
            params.put("long", gps_long);

            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);

                    if (response.equals("OK")) {
                        Toast.makeText(AddRestaurantActivity.this, R.string.restuarant_add_confirmation, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddRestaurantActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddRestaurantActivity.this, R.string.error_api, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        });
    }
}