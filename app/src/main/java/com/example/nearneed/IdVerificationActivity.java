package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class IdVerificationActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnSubmit;
    private TextView btnSkip;
    private android.view.View cardUploadFront, cardUploadBack;
    private boolean frontUploaded = false;
    private boolean backUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_verification);

        btnBack = findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSkip = findViewById(R.id.btnSkip);
        cardUploadFront = findViewById(R.id.cardUploadFront);
        cardUploadBack = findViewById(R.id.cardUploadBack);

        // Initial state: submit button disabled/dimmed
        btnSubmit.setEnabled(false);
        btnSubmit.setAlpha(0.6f);

        setupListeners();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        cardUploadFront.setOnClickListener(v -> {
            frontUploaded = true;
            markCardUploaded(cardUploadFront, "Front Uploaded");
            checkReadyToSubmit();
        });

        cardUploadBack.setOnClickListener(v -> {
            backUploaded = true;
            markCardUploaded(cardUploadBack, "Back Uploaded");
            checkReadyToSubmit();
        });

        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, IdVerifiedActivity.class);
            startActivity(intent);
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileSuccessActivity.class);
            startActivity(intent);
        });
    }

    private void markCardUploaded(android.view.View card, String message) {
        card.setBackgroundResource(R.drawable.bg_verified_blue_shield); // Solid light blue
        // Find icon and text to change color
        if (card instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) card;
            for (int i = 0; i < group.getChildCount(); i++) {
                android.view.View child = group.getChildAt(i);
                if (child instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) child).setColorFilter(0xFF1D5EF3); // Blue icon
                }
                if (child instanceof android.widget.TextView) {
                    ((android.widget.TextView) child).setTextColor(0xFF1D5EF3); // Blue text
                }
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkReadyToSubmit() {
        if (frontUploaded && backUploaded) {
            btnSubmit.setEnabled(true);
            btnSubmit.setAlpha(1.0f);
            btnSubmit.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1D5EF3)); // Brand blue
        }
    }
}
