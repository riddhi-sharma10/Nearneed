package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CommunityPostSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post_success);

        findViewById(R.id.btnBackToFeed).setOnClickListener(v -> navigateToMain());
        findViewById(R.id.btnClose).setOnClickListener(v -> navigateToMain());
        
        findViewById(R.id.btnViewMyPost).setOnClickListener(v -> {
            Intent detailIntent = new Intent(this, MyCommunityPostDetailActivity.class);
            if (getIntent().getExtras() != null) {
                detailIntent.putExtras(getIntent().getExtras());
            }
            startActivity(detailIntent);
            finish();
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
