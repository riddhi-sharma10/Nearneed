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
            Toast.makeText(this, "Volunteers Page (Coming Soon)", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Volunteers Page when created
        });

        btnViewResponses.setOnClickListener(v -> {
            Toast.makeText(this, "Responses Page (Coming Soon)", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Responses Page when created
        });

        btnUpdateStatus.setOnClickListener(v -> {
            Toast.makeText(this, "Status Change Page (Coming Soon)", Toast.LENGTH_SHORT).show();
            // TODO: Open status bottom sheet / screen when created
        });
    }
}
