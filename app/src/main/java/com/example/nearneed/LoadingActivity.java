package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    public static final String EXTRA_TARGET_CLASS = "extra_target_class";
    public static final String EXTRA_STATUS_MESSAGES = "extra_status_messages";

    private ProgressBar progressBar;
    private TextView tvStatus;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String[] statusMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.loadingProgress);
        tvStatus = findViewById(R.id.tvStatus);

        // Get target class and messages from Intent
        String targetClassName = getIntent().getStringExtra(EXTRA_TARGET_CLASS);
        statusMessages = getIntent().getStringArrayExtra(EXTRA_STATUS_MESSAGES);
        
        if (statusMessages == null) {
            statusMessages = new String[] {
                "Connecting to area...", 
                "Finding neighbors...", 
                "Broadcasting...", 
                "Finalizing..."
            };
        }

        // Simulated progress simulation logic
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    updateStatusText(progressStatus);
                });

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // After completion, transition to target
            handler.postDelayed(() -> {
                try {
                    Class<?> targetClass = targetClassName != null ? Class.forName(targetClassName) : GigSuccessActivity.class;
                    startActivity(new Intent(LoadingActivity.this, targetClass));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } catch (ClassNotFoundException e) {
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    finish();
                }
            }, 500);

        }).start();
    }

    private void updateStatusText(int progress) {
        if (statusMessages.length == 0) return;
        int index = (progress * statusMessages.length) / 101; 
        if (index < statusMessages.length) {
            tvStatus.setText(statusMessages[index]);
        }
    }
}
