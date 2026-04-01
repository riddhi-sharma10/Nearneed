package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // ── Bottom Navigation ────────────────────────────────────────────────
        // The menu XML (bottom_nav_menu_messages) already pre-checks nav_messages.
        // We only need to wire up the click listener here.
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_messages);
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(MessagesActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_map) {
                    startActivity(new Intent(MessagesActivity.this, DiscoveryMapActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(MessagesActivity.this, UserProfileActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    // Already on Messages
                    return true;
                }
                return false;
            });
        }

        // FAB – open CreatePost
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                startActivity(new Intent(MessagesActivity.this, CreatePostActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }

        // ── Conversation click listeners ─────────────────────────────────────
        if (findViewById(R.id.convRachel) != null)
            findViewById(R.id.convRachel).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Rachel...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convRahul) != null)
            findViewById(R.id.convRahul).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Rahul Singh...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convManya) != null)
            findViewById(R.id.convManya).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Manya Awasthi...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convVishu) != null)
            findViewById(R.id.convVishu).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Vishu Singh...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convRiddhi) != null)
            findViewById(R.id.convRiddhi).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Riddhi Sharma...", Toast.LENGTH_SHORT).show());
    }
}
