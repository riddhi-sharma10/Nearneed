package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Link navigation bar to respective pages
        NavbarHelper.setup(this, -1);
        
        // Handle closing the Create Post activity
        View fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null) fabAdd.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        
        // Card click listeners
        findViewById(R.id.cardGigPost).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateGigActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        });
        
        findViewById(R.id.cardCommunityPost).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateCommunityPostActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        });

        // Skip for now
        findViewById(R.id.tvSkipForNow).setOnClickListener(v -> {
            startActivity(new Intent(this, JobsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
