package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DiscoveryMapCommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_map_community);

        ImageView btnClosePreview = findViewById(R.id.btnClosePreview);
        if (btnClosePreview != null) {
            btnClosePreview.setOnClickListener(v -> finish());
        }

        MaterialButton btnViewFullPost = findViewById(R.id.btnViewFullPost);
        if (btnViewFullPost != null) {
            btnViewFullPost.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(DiscoveryMapCommunityActivity.this, MyCommunityPostDetailActivity.class);
                startActivity(intent);
            });
        }
        
        android.view.View btnQuickRespond = findViewById(R.id.btnQuickRespond);
        if (btnQuickRespond != null) {
             btnQuickRespond.setOnClickListener(v -> {
                 // Action for quick respond
             });
        }
    }
}
