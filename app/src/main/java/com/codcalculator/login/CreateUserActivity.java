package com.codcalculator.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.codcalculator.R;
import com.codcalculator.utilities.SharedPrefsUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class CreateUserActivity extends AppCompatActivity {

    private TextInputEditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private TextInputLayout lMail, lPasswd, lConfirmPasswd;
    ImageButton infoButton, passInfo;
    Button buttonCreate;
    TextView textViewForgotPassword, textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmailEditText = findViewById(R.id.editTextEmail);
        mPasswordEditText = findViewById(R.id.editTextPassword);
        mConfirmPasswordEditText = findViewById(R.id.editTextPassword2);
        lMail = findViewById(R.id.email_text_input);
        lPasswd = findViewById(R.id.password_text_input);
        lConfirmPasswd = findViewById(R.id.password2_text_input);
        infoButton = findViewById(R.id.buttonInfo);
        buttonCreate = findViewById(R.id.buttonCreate);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewLogin = findViewById(R.id.textViewLogin);
        passInfo = findViewById(R.id.password_infobutton);

        buttonCreate.setOnClickListener(v -> {
            lMail.setError(null);
            lPasswd.setError(null);
            lConfirmPasswd.setError(null);
            if (TextUtils.isEmpty(mEmailEditText.getText()) || mPasswordEditText.getText().toString().isEmpty() || mConfirmPasswordEditText.getText().toString().isEmpty()) {
                if (TextUtils.isEmpty(mEmailEditText.getText()))
                    lMail.setError(getString(R.string.emptyFields));
                if (TextUtils.isEmpty(mPasswordEditText.getText()))
                    lPasswd.setError(getString(R.string.emptyFields));
                if (TextUtils.isEmpty(mConfirmPasswordEditText.getText()))
                    lConfirmPasswd.setError(getString(R.string.emptyFields));
            } else if (!validarEmail(mEmailEditText.getText().toString())) {
                lMail.setError(getString(R.string.invalidEmail));
            } else if (!mPasswordEditText.getText().toString().equals(mConfirmPasswordEditText.getText().toString())) {
                lPasswd.setError(" ");
                lConfirmPasswd.setError(getString(R.string.passwordsDoNotMatch));
            } else if (!isPasswordValid(mPasswordEditText.getText().toString())) {
                String error = getPasswordError(mPasswordEditText.getText().toString());
                lPasswd.setError(" ");
                lConfirmPasswd.setError(error);
            } else {
                SharedPrefsUtil.saveString(this, "email", mEmailEditText.getText().toString());
                SharedPrefsUtil.saveString(this, "password", mPasswordEditText.getText().toString());
                createUser(SharedPrefsUtil.getString(this, "email"), SharedPrefsUtil.getString(this, "password"));
            }
        });

        infoButton.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, null);
            PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1300);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        });

        passInfo.setOnClickListener(view12 -> {
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(bottomSheetView);
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

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5 && password.matches(".*[A-Z].*[0-9].*[!@#$%^&*+=_\\-\\[\\]{}|;:'\",.<>/?].*");
    }

    private String getPasswordError(String password) {
        StringBuilder errorString = new StringBuilder();
        if (password.length() < 6) {
            errorString.append(getString(R.string.error_password_length) + "\n");
        }
        boolean hasDigit = false, hasUppercase = false, hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if ("!@#$%^&*+=_-[]{}|;:'\",.<>/?".indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        if (!hasDigit) {
            errorString.append(getString(R.string.error_password_digit) + "\n");
        }
        if (!hasUppercase) {
            errorString.append(getString(R.string.error_password_uppercase) + "\n");
        }
        if (!hasSpecial) {
            errorString.append(getString(R.string.error_password_special));
        }
        if (errorString.length() > 0) {
            errorString.insert(0, getString(R.string.error_password_header) + "\n");
            return errorString.toString();
        }
        return null;
    }

    private void createUser(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        user.sendEmailVerification();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(SharedPrefsUtil.getString(this, "username"))
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(this, getString(R.string.newUserSuccess), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.newUserFail), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // No hacemos nada
    }

}