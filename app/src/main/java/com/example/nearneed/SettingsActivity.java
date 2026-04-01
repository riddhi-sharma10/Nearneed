package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnIDVerification).setOnClickListener(v -> {
            // Check if already verified (mock logic: check if the verified text is visible/present)
            // In a real app, this would check a user profile flag.
            boolean isVerified = false; // Defaulting to false as requested
            
            if (isVerified) {
                Toast.makeText(this, "Your ID is already verified", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SettingsActivity.this, IdVerificationActivity.class);
                intent.putExtra("HIDE_SKIP", true);
                startActivity(intent);
            }
        });

        MaterialSwitch switchDarkMode = findViewById(R.id.switchDarkMode);
        if (switchDarkMode != null) {
            // Check current night mode state
            boolean isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
            // Also consider system setting if first time
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
                isDarkMode = false; // Default to Light Mode as requested
            }
            switchDarkMode.setChecked(isDarkMode);

            switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            });
        }

        findViewById(R.id.btnLanguage).setOnClickListener(v -> 
            Toast.makeText(this, "Language Selection coming soon", Toast.LENGTH_SHORT).show()
        );
        
        findViewById(R.id.btnDeleteAccount).setOnClickListener(v -> {
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show();
            // Redirect to welcome screen
            Intent intent = new Intent(SettingsActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        findViewById(R.id.btnSaveSettings).setOnClickListener(v -> {
            Toast.makeText(this, "Settings Saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
