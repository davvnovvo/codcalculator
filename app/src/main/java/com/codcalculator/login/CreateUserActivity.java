package com.codcalculator.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codcalculator.R;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Button infoButton = findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
                builder.setTitle("Términos y Condiciones");
                builder.setMessage("Texto de ejemplo xD");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}