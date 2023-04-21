package com.codcalculator.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.codcalculator.R;
import com.codcalculator.main.MainActivity;
import com.codcalculator.utilities.ImageAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    public static final String LANGUAGE_PREFERENCE = "language_preference";
    public static final String SELECTED_LANGUAGE = "selected_language";
    private ViewPager viewPager;
    private int[] images = {R.drawable.atheus, R.drawable.gwanwyn, R.drawable.oso};
    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    TextView forgotPassword, createAccount;
    ImageButton googleAccess_btn;
    Button buttonLogin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.textViewForgotPassword);
        createAccount = findViewById(R.id.textViewCreateAccount);
        googleAccess_btn = findViewById(R.id.google_logo_button);
        buttonLogin = findViewById(R.id.buttonLogin);
        mAuth = FirebaseAuth.getInstance();

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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleAccess_btn.setOnClickListener(view -> signInWithGoogle());

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });
    }

    // Manejamos el login con google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Toast.makeText(this, getString(R.string.googleLoginSuccess), Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                Log.i("APIException: ", e.toString());
                Toast.makeText(this, getString(R.string.googleLoginError), Toast.LENGTH_LONG).show();
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        //Checkeamos si la info del user existe
                        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("documents/users/" + uid + "/" + "userInformation.json");
                        fileRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                            //Existe
                            updateUI(user);
                        }).addOnFailureListener(exception -> {
                            //No existe
                            //StorageUtil.createStorageUser(user, "0", "0", "0", getString(R.string.defaultStatus));
                            updateUI(user);
                        });
                    } else {
                        updateUI(null);
                    }
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
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
