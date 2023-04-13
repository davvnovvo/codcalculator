package com.codcalculator.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codcalculator.login.LoginActivity;
import com.codcalculator.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                // Finalizamos esta actividad para evitar que el usuario pueda volver atrás
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
