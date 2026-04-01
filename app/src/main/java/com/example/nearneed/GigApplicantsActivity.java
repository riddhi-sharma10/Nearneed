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
            btnAcceptRahul.setOnClickListener(v -> {
                startActivity(new Intent(this, PaymentActivity.class));
            });

        if (btnCounterRahul != null)
            btnCounterRahul.setOnClickListener(v ->
                Toast.makeText(this, "Sending counter offer to Rahul...", Toast.LENGTH_SHORT).show());

        if (btnDeclineRahul != null)
            btnDeclineRahul.setOnClickListener(v -> {
                CardView cardRahul = findViewById(R.id.cardRahul);
                if (cardRahul != null) {
                    cardRahul.setVisibility(View.GONE);
                }
                Toast.makeText(this, "Declined Rahul's application.", Toast.LENGTH_SHORT).show();
            });

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
        // Bottom Navigation handled via universal helper
        NavbarHelper.setup(this, NavbarHelper.TAB_HOME);
    }
}
