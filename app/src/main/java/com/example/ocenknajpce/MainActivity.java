package com.example.ocenknajpce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
        String viewRestaurantUrl = "http://dev.imagit.pl/mobilne/api/restaurants/"+userId,
                removeRestaurantUrl = "https://dev.imagit.pl/mobilne/api/restaurant/delete/"+userId+"/";

        btn_addNewRestaurant.setOnClickListener(view -> {
            Intent nextIntent = new Intent(MainActivity.this, AddRestaurantActivity.class);
            startActivity(nextIntent);
        });

        client.get(viewRestaurantUrl, new AsyncHttpResponseHandler() {
            private String restaurantId, restaurantName, restaurantPhone, restaurantLatitude, restaurantLong;

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String JSON = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(JSON);
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        restaurantId = jsonObject.getString("RESTAURANT_ID");
                        restaurantName = jsonObject.getString("RESTAURANT_NAME");
                        restaurantPhone = jsonObject.getString("RESTAURANT_PHONE");
                        restaurantLatitude = jsonObject.getString("RESTAURANT_LAT");
                        restaurantLong = jsonObject.getString("RESTAURANT_LONG");

                        restaurantsList.add(restaurantName +","+restaurantPhone);
                    }
                    lv_Restaurants.setAdapter(restaurantsAdapter);

                    lv_Restaurants.setOnItemClickListener((adapterView, view, i, l) -> {
                        String optionView, optionRemove;

                        optionView = getString(R.string.option_view);
                        optionRemove = getString(R.string.option_remove);

                        final String[] Options = {
                                optionView, optionRemove
                        };

                        AlertDialog.Builder optionsBuilder = new AlertDialog.Builder(MainActivity.this);
                        optionsBuilder.setTitle(R.string.option_title);
                        optionsBuilder.setItems(Options, (dialogInterface, index) -> {
                            String removeRestaurantApiToken = removeRestaurantUrl+restaurantId;
                            String removeRestaurantConfirmation = getString(R.string.restaurant)+" "+restaurantName+" "+getString(R.string.option_remove_done);

                            switch (index) {
                                case 0:
                                    Intent viewIntent = new Intent(MainActivity.this, RestaurantViewActivity.class);
                                    viewIntent.putExtra("restaurantData", restaurantsList.get(i));
                                    startActivity(viewIntent);
                                    break;
                                case 1:
                                    client.get(removeRestaurantApiToken, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String response = new String(responseBody);

                                            if (response.equals("OK")) {
                                                Toast.makeText(MainActivity.this, removeRestaurantConfirmation , Toast.LENGTH_LONG).show();
                                                recreate();
                                            } else if (response.equals("ERROR")) {
                                                Toast.makeText(MainActivity.this, R.string.option_remove_unable, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        }
                                    });
                                    break;
                            }
                        });
                        optionsBuilder.show();
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}