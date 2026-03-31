package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GigDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PRICE = "extra_price";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_DISTANCE = "extra_distance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnApply).setOnClickListener(v -> showApplySheet());

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
