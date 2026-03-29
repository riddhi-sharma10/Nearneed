package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CommunityPostSuccessActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private final long TOTAL_TIME = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post_success);

        findViewById(R.id.btnBackToFeed).setOnClickListener(v -> {
            cancelTimer();
            navigateToMain();
        });

        findViewById(R.id.btnClose).setOnClickListener(v -> {
            cancelTimer();
            navigateToMain();
        });
        
        findViewById(R.id.btnViewMyPost).setOnClickListener(v -> {
            cancelTimer();
            finish();
        });

        startAutoRedirect();
    }

    private void startAutoRedirect() {
        TextView tvAutoRedirect = findViewById(R.id.tvAutoRedirect);
        ProgressBar pbAutoRedirect = findViewById(R.id.pbAutoRedirect);

        if (tvAutoRedirect == null || pbAutoRedirect == null) return;

        countDownTimer = new CountDownTimer(TOTAL_TIME, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update text every roughly 1000ms equivalent, but progress bar smoothly
                int secondsLeft = (int) Math.ceil(millisUntilFinished / 1000.0);
                tvAutoRedirect.setText("auto-redirect in " + secondsLeft + "s");

                // Progress goes from 100 down to 0
                int progress = (int) ((millisUntilFinished * 100) / TOTAL_TIME);
                pbAutoRedirect.setProgress(progress);
            }

            @Override
            public void onFinish() {
                pbAutoRedirect.setProgress(0);
                tvAutoRedirect.setText("auto-redirect in 0s");
                navigateToMain();
            }
        }.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
