package com.codcalculator.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codcalculator.login.LoginActivity;
import com.codcalculator.R;
import com.codcalculator.main.MainActivity;
import com.codcalculator.utilities.SharedPrefsUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 4000;
    boolean ToS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ToS = SharedPrefsUtil.getBoolean(this, "ToS");
        openApp(ToS);

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

    private void openApp(boolean b) {

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }, 6500);

    }

}
