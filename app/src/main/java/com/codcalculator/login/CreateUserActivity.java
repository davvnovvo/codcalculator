package com.codcalculator.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.codcalculator.R;

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
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, null);
            PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1100);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
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

    @Override
    public void onBackPressed() {
        // No hacemos nada
    }

}