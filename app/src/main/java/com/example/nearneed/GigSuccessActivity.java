package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GigSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_success);

        // All CTAs go back to the main feed
        findViewById(R.id.btnClose).setOnClickListener(v -> goHome());
        findViewById(R.id.btnBackToFeed).setOnClickListener(v -> goHome());
        findViewById(R.id.btnViewMyPost).setOnClickListener(v -> {
            // TODO: navigate to the created post's detail page
            goHome();
        });
    }

    private void goHome() {
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, MainActivity.class.getName());
        intent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGE, "Updating Dashboard...");
        intent.putExtra(LoadingActivity.EXTRA_DURATION_MS, 1500L); // Shorter duration for simple navigation
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
