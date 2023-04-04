package com.codcalculator.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.codcalculator.R;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    public static final String LANGUAGE_PREFERENCE = "language_preference";
    public static final String SELECTED_LANGUAGE = "selected_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_login);
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

    private void setLocale() {
        SharedPreferences preferences = getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE);
        String lang = preferences.getString(SELECTED_LANGUAGE, "");
        if (!lang.isEmpty()) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
