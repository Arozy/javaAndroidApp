package com.example.ocenknajpce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText newLogin = findViewById(R.id.et_newLogin);
        EditText newPassword = findViewById(R.id.et_newPassword);
        EditText rptPassword = findViewById(R.id.et_rptPassword);
        EditText email = findViewById(R.id.et_email);
        Button register = findViewById(R.id.btn_Register);
        TextView goBack = findViewById(R.id.tv_Goback);

        final AsyncHttpClient client = new AsyncHttpClient();

        register.setOnClickListener(view -> {
            String userLogin = newLogin.getText().toString();
            String userPassword = newPassword.getText().toString();
            String userRptdPass = rptPassword.getText().toString();
            String userEmail = email.getText().toString();

            if (userLogin.isEmpty() || userPassword.isEmpty() || userRptdPass.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.error_Empty);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if(!userPassword.equals(userRptdPass)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(R.string.error)
                        .setMessage("Hasła nie są identyczne!");
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                String url = "http://dev.imagit.pl/mobilne/api/register";
                RequestParams params = new RequestParams();
                params.put("login", userLogin);
                params.put("pass", userPassword);
                params.put("email", userEmail);

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        if(response.equals("OK")) {
                            Toast.makeText(RegisterActivity.this, R.string.info_register_successful, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.error_account_exists, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });

        goBack.setOnClickListener(view -> {
            Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginActivity);
        });

    }
}