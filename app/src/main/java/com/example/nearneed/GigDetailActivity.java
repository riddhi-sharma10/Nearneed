package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.nearneed.R;
import com.google.android.material.button.MaterialButton;

public class GigDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PRICE = "extra_price";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_DISTANCE = "extra_distance";
    public static final String EXTRA_IS_OWNER = "extra_is_owner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnOptions = findViewById(R.id.btnOptions);
        boolean isOwner = getIntent().getBooleanExtra(EXTRA_IS_OWNER, false);
        
        if (btnOptions != null) {
            btnOptions.setOnClickListener(v -> {
                if (isOwner) {
                    showManagementMenu(v);
                } else {
                    Toast.makeText(this, "More options coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageButton btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> {
                Toast.makeText(this, "Sharing Gig details...", Toast.LENGTH_SHORT).show();
            });
        }

        MaterialButton btnGigAction = findViewById(R.id.btnGigAction);
        if (isOwner) {
            btnGigAction.setText("Posted");
            btnGigAction.setOnClickListener(v -> {
                Toast.makeText(this, "You have posted this gig.", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnGigAction.setText("Apply for this Gig →");
            btnGigAction.setOnClickListener(v -> showApplySheet());
        }

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String price = intent.getStringExtra(EXTRA_PRICE);
            String desc = intent.getStringExtra(EXTRA_DESC);
            String distance = intent.getStringExtra(EXTRA_DISTANCE);

            if (title != null) {
                TextView tvTitle = findViewById(R.id.tvGigTitle);
                if (tvTitle != null) tvTitle.setText(title);
            }
            if (price != null) {
                TextView tvPrice = findViewById(R.id.tvGigPrice);
                if (tvPrice != null) tvPrice.setText(price);
            }
            if (desc != null) {
                TextView tvDesc = findViewById(R.id.tvGigDesc);
                if (tvDesc != null) tvDesc.setText(desc);
            }
            if (distance != null) {
                TextView tvDistance = findViewById(R.id.tvGigDistance);
                if (tvDistance != null) tvDistance.setText(distance);
            }
        }
    }

    private void showManagementMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_gig_detail_owner, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_view_responses) {
                Intent intent = new Intent(this, GigApplicantsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_update_status) {
                Intent intent = new Intent(this, PostStatusActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_save) {
                Toast.makeText(this, "Gig saved", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_help) {
                Toast.makeText(this, "Opening Help...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_report) {
                Toast.makeText(this, "Opening Report...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_settings) {
                Toast.makeText(this, "Opening Settings...", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void showApplySheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_apply_gig, null);
        dialog.setContentView(sheetView);

        Intent intent = getIntent();
        String postedPriceStr = intent.getStringExtra(EXTRA_PRICE);
        if (postedPriceStr == null) postedPriceStr = "₹350";
        
        TextView tvAcceptLabel = sheetView.findViewById(R.id.tvAcceptPostedPrice);
        tvAcceptLabel.setText("Accept posted price (" + postedPriceStr + ")");

        // Parse numerical value (e.g. ₹350 -> 350)
        int initialPrice = 350;
        try {
            initialPrice = Integer.parseInt(postedPriceStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) { /* fallback */ }

        final int[] proposedPrice = {initialPrice + 50};
        TextView tvProposedPrice = sheetView.findViewById(R.id.tvProposedPrice);
        tvProposedPrice.setText("₹ " + proposedPrice[0]);
        
        final String finalPostedPriceStr = postedPriceStr;
        RadioButton rbPropose = sheetView.findViewById(R.id.rbProposeDifferent);
        RadioButton rbAccept = sheetView.findViewById(R.id.rbAcceptPosted);
        TextView tvPropose = sheetView.findViewById(R.id.tvProposeDifferent);
        TextView tvAccept = sheetView.findViewById(R.id.tvAcceptPostedPrice);
        com.google.android.material.card.MaterialCardView cardPropose = sheetView.findViewById(R.id.cardProposeDifferent);
        com.google.android.material.card.MaterialCardView cardAccept = sheetView.findViewById(R.id.cardAcceptPosted);
        View negotiator = sheetView.findViewById(R.id.llPriceNegotiator);

        View.OnClickListener selectPropose = v -> {
            rbPropose.setChecked(true);
            rbAccept.setChecked(false);

            // Selected: Propose card
            cardPropose.setStrokeColor(0xFF1A6FD4);
            cardPropose.setStrokeWidth(3);
            cardPropose.setCardBackgroundColor(0xFFE8F1FC);
            tvPropose.setTextColor(0xFF1A6FD4);

            // Unselected: Accept card
            cardAccept.setStrokeColor(getResources().getColor(R.color.border_standard));
            cardAccept.setStrokeWidth(2);
            cardAccept.setCardBackgroundColor(0xFFFFFFFF);
            tvAccept.setTextColor(getResources().getColor(R.color.text_subheadline));

            negotiator.setVisibility(View.VISIBLE);
            negotiator.setAlpha(1.0f);
            negotiator.setEnabled(true);
        };

        View.OnClickListener selectAccept = v -> {
            rbAccept.setChecked(true);
            rbPropose.setChecked(false);

            // Selected: Accept card
            cardAccept.setStrokeColor(0xFF1A6FD4);
            cardAccept.setStrokeWidth(3);
            cardAccept.setCardBackgroundColor(0xFFE8F1FC);
            tvAccept.setTextColor(0xFF1A6FD4);

            // Unselected: Propose card
            cardPropose.setStrokeColor(getResources().getColor(R.color.border_standard));
            cardPropose.setStrokeWidth(2);
            cardPropose.setCardBackgroundColor(0xFFFFFFFF);
            tvPropose.setTextColor(getResources().getColor(R.color.text_header));

            negotiator.setVisibility(View.GONE);
            negotiator.setEnabled(false);
        };

        rbPropose.setOnClickListener(selectPropose);
        cardPropose.setOnClickListener(selectPropose);

        rbAccept.setOnClickListener(selectAccept);
        cardAccept.setOnClickListener(selectAccept);

        // Set initial state
        selectAccept.onClick(null);

        sheetView.findViewById(R.id.btnIncreasePrice).setOnClickListener(v1 -> {
            if (negotiator.isEnabled()) {
                proposedPrice[0] += 50;
                tvProposedPrice.setText("₹ " + proposedPrice[0]);
            }
        });

        sheetView.findViewById(R.id.btnDecreasePrice).setOnClickListener(v1 -> {
            if (negotiator.isEnabled() && proposedPrice[0] > 50) {
                proposedPrice[0] -= 50;
                tvProposedPrice.setText("₹ " + proposedPrice[0]);
            }
        });

        sheetView.findViewById(R.id.btnSendResponse).setOnClickListener(v -> {
            dialog.dismiss();
            
            String finalMsg = rbAccept.isChecked() ? "Application Sent at posted price: " + finalPostedPriceStr 
                                                   : "Application Sent with proposed price: ₹" + proposedPrice[0];
            Toast.makeText(this, finalMsg, Toast.LENGTH_SHORT).show();
            
            // Transition to Success Popup Screen
            Intent successIntent = new Intent(this, ApplySuccessActivity.class);
            startActivity(successIntent);
            finish();
        });

        dialog.show();
    }
}
