package com.example.ocenknajpce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv_Restaurants = findViewById(R.id.lv_Restaurants);
        Button btn_addNewRestaurant = findViewById(R.id.btn_addRestaurant);
        SharedPreferences preferences = getSharedPreferences("userPreferences", Activity.MODE_PRIVATE);
        String userId = preferences.getString("userId", "0");

        final ArrayList<String> restaurantsList = new ArrayList<>();
        final ArrayAdapter<String> restaurantsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, restaurantsList);
        lv_Restaurants.setAdapter(restaurantsAdapter);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://dev.imagit.pl/mobilne/api/restaurants/"+userId;

        btn_addNewRestaurant.setOnClickListener(view -> {
            Intent nextIntent = new Intent(MainActivity.this, AddRestaurantActivity.class);
            startActivity(nextIntent);
        });

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSON = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(JSON);
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String restaurantName = jsonObject.getString("RESTAURANT_NAME");
                        String restaurantPhone = jsonObject.getString("RESTAURANT_PHONE");

                        restaurantsList.add(restaurantName +", "+restaurantPhone);
                    }
                    lv_Restaurants.setAdapter(restaurantsAdapter);

                    lv_Restaurants.setOnItemClickListener((adapterView, view, i, l) -> {
                        Toast.makeText(MainActivity.this, "you clicked item"+i+" "+restaurantsList.get(i), Toast.LENGTH_SHORT).show();
                        Intent viewIntent = new Intent(MainActivity.this, RestaurantViewActivity.class);
                        startActivity(viewIntent);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}