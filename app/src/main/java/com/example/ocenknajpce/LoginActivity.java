package com.example.ocenknajpce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        final EditText et_Login = findViewById(R.id.et_Login);
        final EditText et_Password = findViewById(R.id.et_Password);
        Button btn_Login = findViewById(R.id.btn_Login);
        TextView tv_Register = findViewById(R.id.tv_Register);
        final AsyncHttpClient client = new AsyncHttpClient();
        SharedPreferences preferences = getSharedPreferences("userPreferences", Activity.MODE_PRIVATE);

        tv_Register.setOnClickListener(view -> {
            Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(register);
        });

        btn_Login.setOnClickListener(view -> {
            String login = et_Login.getText().toString();
            String password = et_Password.getText().toString();

            if(login.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.error_Empty)
                        .setPositiveButton("OK", (dialogInterface, i) -> {});
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                String URL = "http://dev.imagit.pl/mobilne/api/login/"+login+"/"+password;

                client.get(URL, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);

                        if (TextUtils.isDigitsOnly(response)) {
                            SharedPreferences.Editor preferencesEditor = preferences.edit();
                            preferencesEditor.putString("userId", response);
                            preferencesEditor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }
}