package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CommunityRatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_rating);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView tvRatingStatus = findViewById(R.id.tvRatingStatus);
        MaterialButton btnSubmitRating = findViewById(R.id.btnSubmitRating);

        if (btnSubmitRating != null) {
            btnSubmitRating.setEnabled(false); // Initially disabled
            btnSubmitRating.setAlpha(0.6f);
            btnSubmitRating.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(CommunityRatingActivity.this, RatingSubmittedActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            });
        }

        if (ratingBar != null) {
            ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
                if (rating > 0) {
                    btnSubmitRating.setEnabled(true);
                    btnSubmitRating.setAlpha(1.0f);
                    
                    if (tvRatingStatus != null) {
                        String statusText = "";
                        if (rating == 1) statusText = "Terrible";
                        else if (rating == 2) statusText = "Bad";
                        else if (rating == 3) statusText = "Average";
                        else if (rating == 4) statusText = "Good";
                        else if (rating == 5) statusText = "Excellent!";
                        tvRatingStatus.setText(statusText);
                    }
                } else {
                    btnSubmitRating.setEnabled(false);
                    btnSubmitRating.setAlpha(0.6f);
                    if (tvRatingStatus != null) tvRatingStatus.setText("");
                }
            });
        }

        TextView btnSkip = findViewById(R.id.btnSkip);
        if (btnSkip != null) {
            btnSkip.setOnClickListener(v -> {
                finish();
            });
        }
    }
}
