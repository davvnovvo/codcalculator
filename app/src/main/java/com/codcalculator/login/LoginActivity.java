package com.codcalculator.login;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final String LANGUAGE_PREFERENCE = "language_preference";
    public static final String SELECTED_LANGUAGE = "selected_language";
    private ViewPager viewPager;
    private int[] images = {R.drawable.atheus, R.drawable.oso, R.drawable.gwanwyn, R.drawable.roc_trueno};
    private int currentPage = 0;
    private boolean isFirstTime;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    private TextView forgotPassword, createAccount;
    private ImageButton googleAccess_btn, guest_button;
    private Button buttonLogin;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private TextInputLayout lMail, lPasswd;
    private EditText editmail, editpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.textViewForgotPassword);
        createAccount = findViewById(R.id.textViewCreateAccount);
        googleAccess_btn = findViewById(R.id.google_logo_button);
        guest_button = findViewById(R.id.guest_button);
        buttonLogin = findViewById(R.id.buttonLogin);
        lMail = findViewById(R.id.email_text_input);
        lPasswd = findViewById(R.id.password_text_input);
        editmail = findViewById(R.id.editTextEmail);
        editpass = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable update = () -> {
            if (currentPage == images.length) {
                currentPage = 0;
            }

            if (isFirstTime) {
                isFirstTime = false;
                viewPager.setCurrentItem(currentPage++, false);
            } else {
                viewPager.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            viewPager.setCurrentItem(currentPage++, false);
                            viewPager.setAlpha(0f);
                            viewPager.animate()
                                    .alpha(1f)
                                    .setDuration(300)
                                    .start();
                        })
                        .start();
            }
        };

        viewPager.setOnTouchListener((v, event) -> true);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        isFirstTime = true;

        // Configuramos Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleAccess_btn.setOnClickListener(view -> signInWithGoogle());

        //Botón para resetear la contraseña si se ha olvidado
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

        //Botón para iniciar sesión como invitado
        guest_button.setOnClickListener(v -> {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.guestError), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        //Botón para crear cuenta nueva
        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
            startActivity(intent);
        });

        //Manejamos el inicio de sesión del usuario com email, y contraseña
        buttonLogin.setOnClickListener(view -> {
            lMail.setError(null);
            lPasswd.setError(null);
            if (editmail.getText().toString().isEmpty() || editpass.getText().toString().isEmpty() || !validarEmail(editmail.getText().toString())) {
                if (editmail.getText().toString().isEmpty())
                    lMail.setError(getString(R.string.emptyFields));
                else if (!validarEmail(editmail.getText().toString()))
                    lMail.setError(getString(R.string.invalidEmail));
                if (editpass.getText().toString().isEmpty())
                    lPasswd.setError(getString(R.string.emptyFields));
            } else {
                mAuth.signInWithEmailAndPassword(editmail.getText().toString(), editpass.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user.isEmailVerified()) {
                                    Toast.makeText(this, getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                                    limpiar();
                                    updateUI(user);
                                } else {
                                    Toast.makeText(this, getString(R.string.mailNotVerified), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(this, getString(R.string.wrongPassword), Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(this, getString(R.string.userNotFound), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                    Toast.makeText(this, getString(R.string.loginError), Toast.LENGTH_SHORT).show();
                                }
                                updateUI(null);
                            }
                        });
            }
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
                    case R.id.turkish:
                        changeLanguage("tr");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.getMenuInflater().inflate(R.menu.menu_language, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }
        popup.show();
    }

    private void setLocale() {
        SharedPreferences prefs = getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE);
        String language = prefs.getString(SELECTED_LANGUAGE, "");
        if (TextUtils.isEmpty(language)) {
            Locale current = getResources().getConfiguration().locale;
            language = current.getLanguage();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(SELECTED_LANGUAGE, language);
            editor.apply();
        }
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void limpiar() {
        editmail.setText("");
        editpass.setText("");
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}