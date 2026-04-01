package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ProfileSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_success);

        MaterialButton btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoadingActivity.class);
            intent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, MainActivity.class.getName());
            intent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGE, "Setting up your home...");
            intent.putExtra(LoadingActivity.EXTRA_DURATION_MS, 2000L);
            startActivity(intent);
            finishAffinity();
        });
    }
}
