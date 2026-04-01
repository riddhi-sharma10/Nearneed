package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DiscoveryMapActivity extends AppCompatActivity {

    private CardView cardPinPreview;
    private View pinGig1, pinNeed1, pinNeed2, btnClosePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_map);

        cardPinPreview = findViewById(R.id.cardPinPreview);
        pinGig1 = findViewById(R.id.pin_gig_premium);
        pinNeed1 = findViewById(R.id.pin_need_premium);
        pinNeed2 = findViewById(R.id.pin_need_tools);
        btnClosePreview = findViewById(R.id.btnClosePreview);

        setupPins();
        setupNavigation();
    }

    private void setupPins() {
        View.OnClickListener previewOpener = v -> showPreview();
        pinGig1.setOnClickListener(previewOpener);
        pinNeed1.setOnClickListener(previewOpener);
        pinNeed2.setOnClickListener(previewOpener);

        btnClosePreview.setOnClickListener(v -> hidePreview());

        findViewById(R.id.btnViewFullPostPreview).setOnClickListener(v -> {
            startActivity(new Intent(DiscoveryMapActivity.this, GigDetailActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void showPreview() {
        if (cardPinPreview.getVisibility() == View.VISIBLE) return;
        
        cardPinPreview.setVisibility(View.VISIBLE);
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left); 
        cardPinPreview.startAnimation(slideUp);
    }

    private void hidePreview() {
        cardPinPreview.setVisibility(View.GONE);
    }

    private void setupNavigation() {
        NavbarHelper.setup(this, NavbarHelper.TAB_MAP);

        // Floating FAB on map canvas
        View fabAddMap = findViewById(R.id.fabAddFloating);
        if (fabAddMap != null) {
            fabAddMap.setOnClickListener(v -> {
                startActivity(new Intent(this, CreatePostActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }
}
