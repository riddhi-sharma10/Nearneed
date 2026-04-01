package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CommunityVolunteerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_volunteer);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Map preview redirects to full map view
        findViewById(R.id.cardMapPreview).setOnClickListener(v -> {
            startActivity(new Intent(this, DiscoveryMapActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // "I'll Volunteer" bottom CTA launches Step 2 Activity
        findViewById(R.id.btnVolunteerSticky).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(CommunityVolunteerActivity.this, CommunityVolunteerStep2Activity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
