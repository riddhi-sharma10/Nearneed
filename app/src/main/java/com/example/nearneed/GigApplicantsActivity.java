package com.example.nearneed;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import android.view.View;

public class GigApplicantsActivity extends AppCompatActivity {

    private boolean isSlotFilled = false;
    private TextView tvSpotsFilled;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_applicants);

        // ── UI Components ──────────────────────────────────────────────────
        tvSpotsFilled = findViewById(R.id.tvSpotsFilled);
        progressBar = findViewById(R.id.progressBar);
        
        // ── Card Containers ────────────────────────────────────────────────
        CardView cardRahul = findViewById(R.id.cardRahul);
        CardView cardPriya = findViewById(R.id.cardPriya);
        CardView cardAnika = findViewById(R.id.cardAnika);

        // ── Expandable Layouts ─────────────────────────────────────────────
        LinearLayout llRahulDetails = findViewById(R.id.llRahulDetails);
        LinearLayout llPriyaDetails = findViewById(R.id.llPriyaDetails);
        LinearLayout llAnikaDetails = findViewById(R.id.llAnikaDetails);

        // ── Toggle Logic ───────────────────────────────────────────────────
        if (cardRahul != null) cardRahul.setOnClickListener(v -> toggleDetails(llRahulDetails));
        if (cardPriya != null) cardPriya.setOnClickListener(v -> toggleDetails(llPriyaDetails));
        if (cardAnika != null) cardAnika.setOnClickListener(v -> toggleDetails(llAnikaDetails));

        // ── Back ──────────────────────────────────────────────────────────────
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // ── Rahul S. applicant buttons ───────────────────────────────────────
        MaterialButton btnAcceptRahul = findViewById(R.id.btnAcceptRahul);
        MaterialButton btnCounterRahul = findViewById(R.id.btnCounterRahul);
        MaterialButton btnDeclineRahul = findViewById(R.id.btnDeclineRahul);
        MaterialButton btnChatRahul = findViewById(R.id.btnChatRahul);

        if (btnAcceptRahul != null)
            btnAcceptRahul.setOnClickListener(v -> handleAccept("Rahul S.", 250));

        if (btnCounterRahul != null)
            btnCounterRahul.setOnClickListener(v -> showCounterOfferDialog("Rahul S.", 250));

        if (btnDeclineRahul != null)
            btnDeclineRahul.setOnClickListener(v -> handleDecline(cardRahul, "Rahul S."));

        if (btnChatRahul != null)
            btnChatRahul.setOnClickListener(v -> openChat("Rahul S.", "rahul_id", "+919876543210"));

        // ── Priya M. applicant buttons ───────────────────────────────────────
        MaterialButton btnAcceptPriya = findViewById(R.id.btnAcceptPriya);
        MaterialButton btnCounterPriya = findViewById(R.id.btnCounterPriya);
        MaterialButton btnDeclinePriya = findViewById(R.id.btnDeclinePriya);
        MaterialButton btnChatPriya = findViewById(R.id.btnChatPriya);

        if (btnAcceptPriya != null)
            btnAcceptPriya.setOnClickListener(v -> handleAccept("Priya M.", 250));

        if (btnCounterPriya != null)
            btnCounterPriya.setOnClickListener(v -> showCounterOfferDialog("Priya M.", 250));

        if (btnDeclinePriya != null)
            btnDeclinePriya.setOnClickListener(v -> handleDecline(cardPriya, "Priya M."));

        if (btnChatPriya != null)
            btnChatPriya.setOnClickListener(v -> openChat("Priya M.", "priya_id", "+919876543211"));

        // ── Anika R. applicant buttons ────────────────────────────────────────
        MaterialButton btnAcceptAnika = findViewById(R.id.btnAcceptAnika);
        MaterialButton btnCounterAnika = findViewById(R.id.btnCounterAnika);
        MaterialButton btnDeclineAnika = findViewById(R.id.btnDeclineAnika);
        MaterialButton btnChatAnika = findViewById(R.id.btnChatAnika);

        if (btnAcceptAnika != null)
            btnAcceptAnika.setOnClickListener(v -> handleAccept("Anika R.", 250));

        if (btnCounterAnika != null)
            btnCounterAnika.setOnClickListener(v -> showCounterOfferDialog("Anika R.", 250));

        if (btnDeclineAnika != null)
            btnDeclineAnika.setOnClickListener(v -> handleDecline(cardAnika, "Anika R."));

        if (btnChatAnika != null)
            btnChatAnika.setOnClickListener(v -> openChat("Anika R.", "anika_id", "+919876543212"));

        // ── Bottom Navigation ─────────────────────────────────────────────────
        NavbarHelper.setup(this, NavbarHelper.TAB_HOME);
    }

    private void toggleDetails(View detailsLayout) {
        if (detailsLayout == null) return;
        
        ViewGroup parent = (ViewGroup) detailsLayout.getParent();
        if (parent != null) {
            TransitionManager.beginDelayedTransition(parent);
        }

        if (detailsLayout.getVisibility() == View.VISIBLE) {
            detailsLayout.setVisibility(View.GONE);
        } else {
            detailsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void handleAccept(String name, int price) {
        if (isSlotFilled) {
            Toast.makeText(this, "Gig is already closed. Slot is filled.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        isSlotFilled = true;
        if (tvSpotsFilled != null) tvSpotsFilled.setText("1 of 1 slots filled");
        if (progressBar != null) {
            progressBar.setProgress(1);
            progressBar.setProgressTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.brand_success_alt)));
        }
        
        Toast.makeText(this, "Accepted " + name + "'s offer of ₹" + price + ". Gig is now closed.", Toast.LENGTH_LONG).show();
        
        // Optionally redirect to payment
        startActivity(new Intent(this, PaymentActivity.class));
    }

    private void handleDecline(View card, String name) {
        if (card != null) card.setVisibility(View.GONE);
        Toast.makeText(this, "Declined " + name + "'s application.", Toast.LENGTH_SHORT).show();
    }


    private void showCounterOfferDialog(String applicantName, int originalPrice) {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_counter_offer, null);
        dialog.setContentView(dialogView);

        // Update title dynamically
        TextView originalPriceView = dialogView.findViewById(R.id.tvOriginalPrice);
        if (originalPriceView != null) {
            originalPriceView.setText("Original offer: ₹" + originalPrice);
        }

        EditText priceInput = dialogView.findViewById(R.id.etCounterPrice);
        MaterialButton sendBtn = dialogView.findViewById(R.id.btnSendCounterOffer);
        MaterialButton cancelBtn = dialogView.findViewById(R.id.btnCancelCounterOffer);

        if (sendBtn != null) {
            sendBtn.setOnClickListener(v -> {
                String newPrice = priceInput.getText().toString().trim();
                if (newPrice.isEmpty()) {
                    Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Counter offer of ₹" + newPrice + " sent to " + applicantName,
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }

        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void openUserProfile(String name, String userId, double rating, String phone) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", name);
        intent.putExtra("userRating", rating);
        intent.putExtra("userPhone", phone);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void openChat(String name, String userId, String userPhone) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("recipientName", name);
        intent.putExtra("recipientId", userId);
        intent.putExtra("recipientPhone", userPhone);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
