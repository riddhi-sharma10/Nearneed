package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nearneed.R;
import com.google.android.material.button.MaterialButton;

public class PostStatusActivity extends AppCompatActivity {

    private boolean isMichaelChecked = false;
    private boolean isElenaChecked = true;  // Default exactly to screenshot
    private boolean isDavidChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_status);

        // Header
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Hero Buttons
        MaterialButton btnYesHelped = findViewById(R.id.btnYesHelped);
        MaterialButton btnNotYet = findViewById(R.id.btnNotYet);

        if (btnYesHelped != null) {
            btnYesHelped.setOnClickListener(v -> {
                Toast.makeText(this, "Confirmed!", Toast.LENGTH_SHORT).show();
            });
        }
        if (btnNotYet != null) {
            btnNotYet.setOnClickListener(v -> {
                Toast.makeText(this, "Marked as not yet.", Toast.LENGTH_SHORT).show();
            });
        }

        // Volunteer Checkbox Logic
        LinearLayout rowMichael = findViewById(R.id.rowMichael);
        ImageView cbMichael = findViewById(R.id.cbMichael);
        if (rowMichael != null) {
            rowMichael.setOnClickListener(v -> {
                isMichaelChecked = !isMichaelChecked;
                updateCheckbox(cbMichael, isMichaelChecked);
            });
        }

        LinearLayout rowElena = findViewById(R.id.rowElena);
        ImageView cbElena = findViewById(R.id.cbElena);
        if (rowElena != null) {
            rowElena.setOnClickListener(v -> {
                isElenaChecked = !isElenaChecked;
                updateCheckbox(cbElena, isElenaChecked);
            });
        }

        LinearLayout rowDavid = findViewById(R.id.rowDavid);
        ImageView cbDavid = findViewById(R.id.cbDavid);
        if (rowDavid != null) {
            rowDavid.setOnClickListener(v -> {
                isDavidChecked = !isDavidChecked;
                updateCheckbox(cbDavid, isDavidChecked);
            });
        }

        // Final Button
        MaterialButton btnFinalUpdateStatus = findViewById(R.id.btnFinalUpdateStatus);
        if (btnFinalUpdateStatus != null) {
            android.content.SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            if (prefs.getBoolean("isPostCompleted", false)) {
                btnFinalUpdateStatus.setText("Completed");
                btnFinalUpdateStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#10B981")));
            }

            btnFinalUpdateStatus.setOnClickListener(v -> {
                btnFinalUpdateStatus.setText("Completed");
                btnFinalUpdateStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#10B981")));
                
                prefs.edit().putBoolean("isPostCompleted", true).apply();

                Toast.makeText(this, "Status successfully updated!", Toast.LENGTH_SHORT).show();
                new android.os.Handler().postDelayed(() -> finish(), 800);
            });
        }
    }

    private void updateCheckbox(ImageView checkbox, boolean isChecked) {
        if (isChecked) {
            checkbox.setBackgroundResource(R.drawable.bg_circle_solid_red);
            checkbox.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F43F5E")));
            checkbox.setImageResource(R.drawable.ic_check_solid_white);
            checkbox.setPadding(8, 8, 8, 8);
        } else {
            checkbox.setBackgroundResource(R.drawable.bg_card_outline);
            checkbox.setBackgroundTintList(null);
            checkbox.setImageDrawable(null);
            checkbox.setPadding(0, 0, 0, 0);
        }
    }
}
