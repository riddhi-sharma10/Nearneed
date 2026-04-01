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
    public static final String EXTRA_STATUS_MESSAGE = "extra_status_message";
    public static final String EXTRA_DURATION_MS = "extra_duration_ms";

    private ProgressBar progressBar;
    private TextView tvStatus;
    private int progressStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String[] statusMessages;
    private long durationMs = 3000; // Default 3 seconds for splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.loadingProgress);
        tvStatus = findViewById(R.id.tvStatus);

        // Get transition data from Intent
        String targetClassNameRaw = getIntent().getStringExtra(EXTRA_TARGET_CLASS);
        statusMessages = getIntent().getStringArrayExtra(EXTRA_STATUS_MESSAGES);
        String singleMessage = getIntent().getStringExtra(EXTRA_STATUS_MESSAGE);
        durationMs = getIntent().getLongExtra(EXTRA_DURATION_MS, 3000L);
        
        // Handle target class from intent even if using old keys (compatibility with some existing code)
        if (targetClassNameRaw == null) {
            targetClassNameRaw = getIntent().getStringExtra("TARGET_CLASS");
        }
        
        final String targetClassName = targetClassNameRaw;

        if (statusMessages == null) {
            if (singleMessage != null) {
                statusMessages = new String[] { singleMessage };
            } else if (getIntent().getStringExtra("STATUS_MESSAGE") != null) {
                statusMessages = new String[] { getIntent().getStringExtra("STATUS_MESSAGE") };
            } else {
                statusMessages = new String[] {
                    "Connecting to area...", 
                    "Finding neighbors...", 
                    "Broadcasting...", 
                    "Finalizing..."
                };
            }
        }
        
        long stepDelay = durationMs / 100;

        // Simulated progress logic
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    updateStatusText(progressStatus);
                });

                try {
                    Thread.sleep(stepDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // After completion, transition to target
            final String targetToLaunch = targetClassName;
            handler.postDelayed(() -> {
                try {
                    // Default target is OtpEnterActivity (Splash -> Login)
                    String finalTarget = (targetToLaunch != null) ? targetToLaunch : OtpEnterActivity.class.getName();
                    Class<?> targetClass = Class.forName(finalTarget);
                    Intent nextIntent = new Intent(LoadingActivity.this, targetClass);
                    
                    // Forward all other extras
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        nextIntent.putExtras(extras);
                    }
                    
                    startActivity(nextIntent);
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
