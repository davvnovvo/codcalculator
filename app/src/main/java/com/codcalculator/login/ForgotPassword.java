package com.codcalculator.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.codcalculator.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText editResetEmail;
    private Button buttonForgot;
    private TextInputLayout lResetMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editResetEmail = findViewById(R.id.editResetEmail);
        buttonForgot = findViewById(R.id.buttonForgot);
        lResetMail = findViewById(R.id.lResetMail);

    }

    private void limpiar() {//Limpiamos el campo de email
        buttonForgot.setText("");
    }

    private boolean validarEmail(String email) {//Validamos el email introducido
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        // No hacemos nada
    }

}