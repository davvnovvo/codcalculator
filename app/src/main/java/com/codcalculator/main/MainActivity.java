package com.codcalculator.main;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.codcalculator.R;
import com.codcalculator.databinding.ActivityMainBinding;
import com.codcalculator.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final String CHANNEL_ID = "my_channel";
    private static final String CHANNEL_NAME = "My Channel";
    private static final String CHANNEL_DESCRIPTION = "My Channel Description";
    private TextView mailTextView;
    private ImageView user_icon;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        mailTextView = binding.navView.getHeaderView(0).findViewById(R.id.mailTextView);
        user_icon = binding.navView.getHeaderView(0).findViewById(R.id.user_icon);

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        if (mailTextView != null && user != null) {
            if (user.isAnonymous()) {
                mailTextView.setText(getText(R.string.guestUser));
                Glide.with(this)
                        .load(R.drawable.ic_usuario)
                        .circleCrop()
                        .into(user_icon);
            } else {
                String photoUrl = user.getPhotoUrl().toString();
                mailTextView.setText(user.getEmail());
                if (!photoUrl.isEmpty()) {
                    Glide.with(this)
                            .load(photoUrl)
                            .placeholder(R.drawable.ic_usuario) // Opcional: una imagen de marcador de posición mientras se carga la imagen
                            .error(R.drawable.ic_usuario) // Opcional: una imagen de error en caso de que no se pueda cargar la imagen
                            .circleCrop()
                            .into(user_icon);
                }
            }
        }

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_resources, R.id.nav_speedups, R.id.nav_honor, R.id.nav_tokens)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.signout_confirm);

        SpannableString spannableString = new SpannableString(message);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        builder.setMessage(spannableString);

        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, getString(R.string.closedSession), Toast.LENGTH_LONG).show();
            finish();
        });

        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.BLACK);

            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(Color.BLACK);
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulsa de nuevo para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
