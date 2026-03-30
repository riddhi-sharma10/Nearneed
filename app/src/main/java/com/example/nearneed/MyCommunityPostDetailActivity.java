package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nearneed.R;
import com.google.android.material.button.MaterialButton;

public class MyCommunityPostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community_post_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        MaterialButton btnViewVolunteers = findViewById(R.id.btnViewVolunteers);
        MaterialButton btnViewResponses = findViewById(R.id.btnViewResponses);
        MaterialButton btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        btnViewVolunteers.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MyCommunityPostDetailActivity.this, VolunteerManagementActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnViewResponses.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MyCommunityPostDetailActivity.this, VolunteerResponsesActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnUpdateStatus.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MyCommunityPostDetailActivity.this, PostStatusActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MaterialButton btnUpdateStatus = findViewById(R.id.btnUpdateStatus);
        MaterialButton btnRateVolunteers = findViewById(R.id.btnRateVolunteers);
        
        if (btnUpdateStatus != null) {
            android.content.SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            if (prefs.getBoolean("isPostCompleted", false)) {
                btnUpdateStatus.setText("Completed");
                btnUpdateStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#10B981")));
                btnUpdateStatus.setIcon(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_check_solid_white));
                btnUpdateStatus.setClickable(false);
                
                if (btnRateVolunteers != null) {
                    btnRateVolunteers.setVisibility(android.view.View.VISIBLE);
                    btnRateVolunteers.setOnClickListener(v -> {
                        android.content.Intent intent = new android.content.Intent(MyCommunityPostDetailActivity.this, CommunityRatingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    });
                }
            } else {
                btnUpdateStatus.setText("Update Status");
                btnUpdateStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#BE123C")));
                btnUpdateStatus.setIcon(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_dropdown_small));
                btnUpdateStatus.setClickable(true);
                
                if (btnRateVolunteers != null) {
                    btnRateVolunteers.setVisibility(android.view.View.GONE);
                }
            }
        }
    }
}
