package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import android.view.View;

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
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        View navMap = findViewById(R.id.navMap);
        if (navMap != null) navMap.setOnClickListener(v -> {
            startActivity(new Intent(this, DiscoveryMapActivity.class));
            finish();
        });

        View navMessages = findViewById(R.id.navMessages);
        if (navMessages != null) navMessages.setOnClickListener(v -> {
            startActivity(new Intent(this, MessagesActivity.class));
            finish();
        });

        View navProfile = findViewById(R.id.navProfile);
        if (navProfile != null) navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
            finish();
        });

        // ── FAB ───────────────────────────────────────────────────────────────
        MaterialButton fabAdd = findViewById(R.id.fabAdd);
        if (fabAdd != null)
            fabAdd.setOnClickListener(v -> {
                startActivity(new Intent(this, CreatePostActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
    }
}
