package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

/**
 * Success confirmation activity shown after a neighbor applies for a gig.
 * Mirrors the District premium success overlay aesthetic.
 */
public class ApplySuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_success);

        MaterialButton btnDone = findViewById(R.id.btnDone);
        
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prepare transition to loading screen
                Intent intent = new Intent(ApplySuccessActivity.this, LoadingActivity.class);
                // Passing the target class back to Dashboard
                intent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, MainActivity.class.getName());
                intent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGE, "Finalizing Application...");
                intent.putExtra(LoadingActivity.EXTRA_DURATION_MS, 3000L); 
                startActivity(intent);
                finish();
            }
        });
    }
}
