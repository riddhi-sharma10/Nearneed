package com.example.nearneed;

import android.content.Intent;
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

        // Share button functionality
        ImageButton btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Community Volunteer Opportunity");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this volunteer opportunity on NearNeed: Emergency food support!");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            });
        }

        // More options menu functionality
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        if (btnOptions != null) {
            btnOptions.setOnClickListener(v -> {
                androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(this, v);
                popup.getMenuInflater().inflate(R.menu.menu_volunteer_more, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.menu_save) {
                        // Handle Save action
                        android.widget.Toast.makeText(this, "Post Saved", android.widget.Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.menu_help) {
                        startActivity(new Intent(this, HelpSupportActivity.class));
                        return true;
                    } else if (id == R.id.menu_report) {
                        android.widget.Toast.makeText(this, "Report Submitted", android.widget.Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.menu_settings) {
                        startActivity(new Intent(this, SettingsActivity.class));
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }

        // Map preview redirects to full map view
        findViewById(R.id.cardMapPreview).setOnClickListener(v -> {
            startActivity(new Intent(this, DiscoveryMapActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // "I'll Volunteer" bottom CTA launches Step 2 Activity
        findViewById(R.id.btnVolunteerSticky).setOnClickListener(v -> {
            startActivity(new Intent(CommunityVolunteerActivity.this, CommunityVolunteerStep2Activity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
