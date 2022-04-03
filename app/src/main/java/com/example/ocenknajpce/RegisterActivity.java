package com.example.ocenknajpce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText newLogin = findViewById(R.id.et_newLogin);
        EditText newPassword = findViewById(R.id.et_newPassword);
        EditText rptPassword = findViewById(R.id.et_rptPassword);
        Button register = findViewById(R.id.btn_Register);
        TextView goBack = findViewById(R.id.tv_Goback);

        register.setOnClickListener(view -> {
            String userLogin = newLogin.getText().toString();
            String userPassword = newPassword.getText().toString();
            String userRptdPass = rptPassword.getText().toString();

            if (userLogin.isEmpty() || userPassword.isEmpty() || userRptdPass.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.error_Empty)
                        .setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        goBack.setOnClickListener(view -> {
            Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginActivity);
        });

    }
}