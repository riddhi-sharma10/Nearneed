package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateCommunityPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community_post);
        
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        MaterialButton btnContinue = findViewById(R.id.btnContinueCommunity);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                // Future transition to Step 2
            });
        }
    }
}
