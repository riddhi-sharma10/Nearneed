package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateCommunityPostStep2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community_post_step2);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnPostNow = findViewById(R.id.btnPostNow);
        if (btnPostNow != null) {
            btnPostNow.setOnClickListener(v -> {
                // Determine if it was an emergency post. Hardcoding true here to show the dialog
                boolean isEmergency = true;
                
                if (isEmergency) {
                    showEmergencyDialogOverlay();
                } else {
                    navigateToSuccess();
                }
            });
        }
    }

    private void showEmergencyDialogOverlay() {
        View overlay = findViewById(R.id.overlayEmergency);
        if (overlay != null) {
            overlay.setVisibility(View.VISIBLE);

            View btnYes = overlay.findViewById(R.id.btnYesPostNow);
            if (btnYes != null) {
                btnYes.setOnClickListener(v -> {
                    overlay.setVisibility(View.GONE);
                    navigateToSuccess();
                });
            }

            View btnCancel = overlay.findViewById(R.id.btnCancel);
            if (btnCancel != null) {
                btnCancel.setOnClickListener(v -> {
                    overlay.setVisibility(View.GONE);
                });
            }
        }
    }

    private void navigateToSuccess() {
        startActivity(new Intent(this, CommunityPostSuccessActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        finish();
    }
}
