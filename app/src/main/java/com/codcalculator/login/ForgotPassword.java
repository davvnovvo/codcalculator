package com.codcalculator.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codcalculator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText editResetEmail;
    private Button buttonForgot;
    private TextInputLayout lResetMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editResetEmail = findViewById(R.id.editResetEmail);
        buttonForgot = findViewById(R.id.buttonForgot);
        lResetMail = findViewById(R.id.lResetMail);

        // Manejamos el envío de correo de recuperación
        buttonForgot.setOnClickListener(view -> {

            if (editResetEmail.getText().toString().isEmpty()) {
                lResetMail.setError(getString(R.string.emptyFields));
            } else if (!validarEmail(editResetEmail.getText().toString())) {
                lResetMail.setError(getString(R.string.invalidEmail));
            } else {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(editResetEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(ForgotPassword.this, ForgotPassword.class);
                                    startActivity(i);

                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    Toast.makeText(ForgotPassword.this, getString(R.string.sentEmail), Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        Toast.makeText(ForgotPassword.this, getString(R.string.mailNotFound), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                        Toast.makeText(ForgotPassword.this, "Error!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    //Validamos el email introducido
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        // No hacemos nada
    }

}