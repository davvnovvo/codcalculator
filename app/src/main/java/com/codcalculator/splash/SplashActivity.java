package com.codcalculator.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.codcalculator.login.LoginActivity;
import com.codcalculator.R;
import com.codcalculator.main.MainActivity;
import com.codcalculator.utilities.SharedPrefsUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 5200;
    boolean ToS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ToS = SharedPrefsUtil.getBoolean(this, "ToS");
        openApp(ToS);

        ImageView logo = findViewById(R.id.logo);
        ImageView textCalculator = findViewById(R.id.textCalculator);

        // Animación del logo
        ObjectAnimator logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1.0f, 0.75f);
        ObjectAnimator logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1.0f, 0.75f);
        ObjectAnimator logoTranslationY = ObjectAnimator.ofFloat(logo, "translationY", 0, -500);
        logoTranslationY.setDuration(2000);

        // Animación del texto
        ObjectAnimator textFadeIn = ObjectAnimator.ofFloat(textCalculator, "alpha", 0f, 1f);
        ObjectAnimator textScaleX = ObjectAnimator.ofFloat(textCalculator, "scaleX", 0.5f, 1.0f);
        ObjectAnimator textScaleY = ObjectAnimator.ofFloat(textCalculator, "scaleY", 0.5f, 1.0f);
        textFadeIn.setDuration(4500);

        // Animación combinada
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(logoScaleX, logoScaleY, logoTranslationY, textFadeIn, textScaleX, textScaleY);
        animatorSet.setStartDelay(1000);
        animatorSet.start();

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

        }, SPLASH_TIMEOUT);

    }

}
