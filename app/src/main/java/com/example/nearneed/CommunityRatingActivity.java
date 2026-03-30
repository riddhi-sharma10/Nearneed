package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;
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

        MaterialButton btnSubmitRating = findViewById(R.id.btnSubmitRating);
        if (btnSubmitRating != null) {
            btnSubmitRating.setOnClickListener(v -> {
                Toast.makeText(this, "Rating Submitted!", Toast.LENGTH_SHORT).show();
                finish();
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
