package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FrameLayout cardMyPost = findViewById(R.id.cardMyPost);
        FrameLayout cardGig1 = findViewById(R.id.cardGig1);
        FrameLayout cardCommunity1 = findViewById(R.id.cardCommunity1);
        FrameLayout cardCommunity2 = findViewById(R.id.cardCommunity2);
        FrameLayout cardGig2 = findViewById(R.id.cardGig2);
        ChipGroup cgFilters = findViewById(R.id.cgFilters);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        View btnVolC1 = findViewById(R.id.btnVolC1);
        if (btnVolC1 != null) {
            btnVolC1.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CommunityVolunteerActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        View btnVolC2 = findViewById(R.id.btnVolC2);
        if (btnVolC2 != null) {
            btnVolC2.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CommunityVolunteerActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        cgFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            
            int id = checkedIds.get(0);
            
            // Note: Since IDs are not strictly mapped in ChipGroup, we inspect the chip child index
            int idx = group.indexOfChild(findViewById(id));
            
            if (idx == 0) {
                // All
                cardMyPost.setVisibility(View.VISIBLE);
                cardGig1.setVisibility(View.VISIBLE);
                cardGig2.setVisibility(View.VISIBLE);
                cardCommunity1.setVisibility(View.VISIBLE);
                cardCommunity2.setVisibility(View.VISIBLE);
            } else if (idx == 1) {
                // Gigs
                cardMyPost.setVisibility(View.GONE);
                cardGig1.setVisibility(View.VISIBLE);
                cardGig2.setVisibility(View.VISIBLE);
                cardCommunity1.setVisibility(View.GONE);
                cardCommunity2.setVisibility(View.GONE);
            } else if (idx == 2) {
                // Community
                cardMyPost.setVisibility(View.GONE);
                cardGig1.setVisibility(View.GONE);
                cardGig2.setVisibility(View.GONE);
                cardCommunity1.setVisibility(View.VISIBLE);
                cardCommunity2.setVisibility(View.VISIBLE);
            } else if (idx == 4) {
                // My Posts
                cardMyPost.setVisibility(View.VISIBLE);
                cardGig1.setVisibility(View.GONE);
                cardGig2.setVisibility(View.GONE);
                cardCommunity1.setVisibility(View.GONE);
                cardCommunity2.setVisibility(View.GONE);
            } else {
                // Near By
                cardMyPost.setVisibility(View.VISIBLE); // Assuming they want their own posts nearby too
                cardGig1.setVisibility(View.VISIBLE);
                cardGig2.setVisibility(View.VISIBLE);
                cardCommunity1.setVisibility(View.VISIBLE);
                cardCommunity2.setVisibility(View.VISIBLE);
            }
        });
    }
}