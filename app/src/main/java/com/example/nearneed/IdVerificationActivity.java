package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class IdVerificationActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FRONT = 1001;
    private static final int REQUEST_PICK_BACK  = 1002;

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
        
        if (getIntent().getBooleanExtra("HIDE_SKIP", false)) {
            btnSkip.setVisibility(View.GONE);
        }

        // Initial state: submit button disabled/dimmed
        btnSubmit.setEnabled(false);
        btnSubmit.setAlpha(0.6f);

        setupListeners();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        cardUploadFront.setOnClickListener(v -> openImagePicker(REQUEST_PICK_FRONT));
        cardUploadBack.setOnClickListener(v  -> openImagePicker(REQUEST_PICK_BACK));

        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, IdVerifiedActivity.class);
            startActivity(intent);
        });

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileSuccessActivity.class);
            startActivity(intent);
        });
    }

    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select ID Image"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == REQUEST_PICK_FRONT) {
                frontUploaded = true;
                setCardUploadedState(cardUploadFront, "Front side uploaded");
            } else if (requestCode == REQUEST_PICK_BACK) {
                backUploaded = true;
                setCardUploadedState(cardUploadBack, "Back side uploaded");
            }
            checkReadyToSubmit();
        }
    }

    private void setCardUploadedState(android.view.View card, String message) {
        // Change background to blue field
        card.setBackgroundResource(R.drawable.bg_id_uploaded);
        
        // Find icon and text to change color to deeper blue
        if (card instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) card;
            for (int i = 0; i < group.getChildCount(); i++) {
                android.view.View child = group.getChildAt(i);
                if (child instanceof android.widget.ImageView) {
                    ((android.widget.ImageView) child).setColorFilter(0xFF4F46E5); // Indigo/Blue icon
                    ((android.widget.ImageView) child).setImageResource(R.drawable.ic_check_circle_green); // Show checkmark
                }
                if (child instanceof android.widget.TextView) {
                    ((android.widget.TextView) child).setTextColor(0xFF4F46E5); // Deep blue text
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
