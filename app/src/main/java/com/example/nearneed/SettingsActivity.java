package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        if (findViewById(R.id.switchDarkMode) != null) {
            findViewById(R.id.switchDarkMode).setOnClickListener(v -> 
                Toast.makeText(this, "Dark mode toggled", Toast.LENGTH_SHORT).show()
            );
        }

        findViewById(R.id.btnLanguage).setOnClickListener(v -> 
            Toast.makeText(this, "Language Selection coming soon", Toast.LENGTH_SHORT).show()
        );
        
        findViewById(R.id.btnDeleteAccount).setOnClickListener(v -> {
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        findViewById(R.id.btnSaveSettings).setOnClickListener(v -> {
            Toast.makeText(this, "Settings Saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
