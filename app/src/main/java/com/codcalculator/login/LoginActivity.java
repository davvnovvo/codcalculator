package com.codcalculator.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codcalculator.R;
import com.codcalculator.main.MainActivity;
import com.codcalculator.utilities.ImageAdapter;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    public static final String LANGUAGE_PREFERENCE = "language_preference";
    public static final String SELECTED_LANGUAGE = "selected_language";

    private ViewPager viewPager;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    TextView forgotPassword, createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.textViewForgotPassword);
        createAccount = findViewById(R.id.textViewCreateAccount);

        viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == images.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });
    }

    public void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        saveLocale(languageCode);
        recreate();
    }

    private void saveLocale(String lang) {
        SharedPreferences.Editor editor = getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    public void showLanguagePopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.spanish:
                        changeLanguage("es");
                        return true;
                    case R.id.english:
                        changeLanguage("en");
                        return true;
                    case R.id.french:
                        changeLanguage("fr");
                        return true;
                    case R.id.russian:
                        changeLanguage("ru");
                        return true;
                    case R.id.portuguese:
                        changeLanguage("pt");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_language);
        popup.show();
    }

    public void setLocale() {
        SharedPreferences preferences = getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE);
        String lang = preferences.getString(SELECTED_LANGUAGE, "en");
        if (!lang.isEmpty()) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> finish())
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

}
