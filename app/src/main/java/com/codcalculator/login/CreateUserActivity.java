package com.codcalculator.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codcalculator.R;
import com.codcalculator.main.MainActivity;

public class CreateUserActivity extends AppCompatActivity {

    ImageButton infoButton;
    TextView textViewForgotPassword, textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        infoButton = findViewById(R.id.buttonInfo);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewLogin = findViewById(R.id.textViewLogin);

        infoButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
            builder.setTitle("Términos y Condiciones");
            builder.setMessage("Texto de ejemplo xD");
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        textViewForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUserActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}