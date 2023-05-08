package com.codcalculator.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

        ImageView imageView = findViewById(R.id.logo);
        Animation animacion = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacion_parpadeo);

        imageView.startAnimation(animacion);
    }

    private void openApp(boolean b) {

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            Intent intent;

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }

        }, 3500);

    }

}
