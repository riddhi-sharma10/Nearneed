package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GigApplicantsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_applicants);

        // ── Back ──────────────────────────────────────────────────────────────
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // ── Rahul S. applicant buttons ────────────────────────────────────────
        MaterialButton btnAcceptRahul = findViewById(R.id.btnAcceptRahul);
        MaterialButton btnCounterRahul = findViewById(R.id.btnCounterRahul);
        MaterialButton btnDeclineRahul = findViewById(R.id.btnDeclineRahul);
        MaterialButton btnChatRahul = findViewById(R.id.btnChatRahul);

        if (btnAcceptRahul != null)
            btnAcceptRahul.setOnClickListener(v ->
                Toast.makeText(this, "Accepted Rahul's offer of ₹250!", Toast.LENGTH_SHORT).show());

        if (btnCounterRahul != null)
            btnCounterRahul.setOnClickListener(v ->
                Toast.makeText(this, "Sending counter offer to Rahul...", Toast.LENGTH_SHORT).show());

        if (btnDeclineRahul != null)
            btnDeclineRahul.setOnClickListener(v ->
                Toast.makeText(this, "Declined Rahul's application.", Toast.LENGTH_SHORT).show());

        if (btnChatRahul != null)
            btnChatRahul.setOnClickListener(v -> {
                startActivity(new Intent(this, MessagesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });

        // ── Collapsed card taps ───────────────────────────────────────────────
        CardView cardPriya = findViewById(R.id.cardPriya);
        if (cardPriya != null)
            cardPriya.setOnClickListener(v ->
                Toast.makeText(this, "View Priya M.'s application", Toast.LENGTH_SHORT).show());

        CardView cardAnika = findViewById(R.id.cardAnika);
        if (cardAnika != null)
            cardAnika.setOnClickListener(v ->
                Toast.makeText(this, "View Anika R.'s application", Toast.LENGTH_SHORT).show());

        // ── Bottom Navigation ─────────────────────────────────────────────────
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (id == R.id.nav_map) {
                    startActivity(new Intent(this, DiscoveryMapActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (id == R.id.nav_messages) {
                    startActivity(new Intent(this, MessagesActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(this, UserProfileActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    return true;
                }
                return true;
            });
        }

        // ── FAB ───────────────────────────────────────────────────────────────
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null)
            fabAdd.setOnClickListener(v -> {
                startActivity(new Intent(this, CreatePostActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
    }
}
