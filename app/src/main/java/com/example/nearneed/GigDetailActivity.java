package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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

        sheetView.findViewById(R.id.btnSendResponse).setOnClickListener(v -> {
            dialog.dismiss();
            
            // Show confirmation toast
            Toast.makeText(this, "Application Sent!", Toast.LENGTH_SHORT).show();
            
            // Transition to Success Popup Screen
            Intent successIntent = new Intent(this, ApplySuccessActivity.class);
            startActivity(successIntent);
            finish();
        });

        dialog.show();
    }
}
