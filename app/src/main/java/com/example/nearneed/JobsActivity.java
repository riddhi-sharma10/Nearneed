package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class JobsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // "Post a Repair" button
        findViewById(R.id.btnPostRepair).setOnClickListener(v -> {
            navigateToCreatePost();
        });

        // Middle FAB
        MaterialButton fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                navigateToCreatePost();
            });
        }

        // Bottom Navigation
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        View navMap = findViewById(R.id.navMap);
        if (navMap != null) navMap.setOnClickListener(v -> {
            startActivity(new Intent(this, DiscoveryMapActivity.class));
            finish();
        });

        View navMessages = findViewById(R.id.navMessages);
        if (navMessages != null) navMessages.setOnClickListener(v -> {
            startActivity(new Intent(this, MessagesActivity.class));
            finish();
        });

        View navProfile = findViewById(R.id.navProfile);
        if (navProfile != null) navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
            finish();
        });
    }

    private void navigateToCreatePost() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
