package com.example.nearneed;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nearneed.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class VolunteerManagementActivity extends AppCompatActivity {

    private int slotsFilled = 4;
    private int totalSlots = 8;
    private TextView tvSlotsFilled;
    private ProgressBar pbSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_management);

        // Bind Dynamic UI Components
        tvSlotsFilled = findViewById(R.id.tvSlotsFilled);
        pbSlots = findViewById(R.id.pbSlots);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnOptions = findViewById(R.id.btnOptions);
        if (btnOptions != null) {
            btnOptions.setOnClickListener(this::showOptionsMenu);
        }

        // Waitlist Management
        MaterialButton btnPromoteParth = findViewById(R.id.btnPromoteParth);
        MaterialButton btnPromoteMehak = findViewById(R.id.btnPromoteMehak);
        MaterialCardView cardParthWait = findViewById(R.id.cardWaitlistParth);
        MaterialCardView cardMehakWait = findViewById(R.id.cardWaitlistMehak);

        // Accepted List Placeholders (Hidden)
        View rowParthAccepted = findViewById(R.id.rowAcceptedParth);
        View divParthAccepted = findViewById(R.id.divAcceptedParth);
        View rowMehakAccepted = findViewById(R.id.rowAcceptedMehak);
        View divMehakAccepted = findViewById(R.id.divAcceptedMehak);

        if (btnPromoteParth != null) {
            btnPromoteParth.setOnClickListener(v -> {
                if (slotsFilled < totalSlots) {
                    cardParthWait.setVisibility(View.GONE);
                    rowParthAccepted.setVisibility(View.VISIBLE);
                    divParthAccepted.setVisibility(View.VISIBLE);
                    promoteHeaderUpdate("Parth Sharma");
                } else {
                    Toast.makeText(this, "No slots available! Add a slot first.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnPromoteMehak != null) {
            btnPromoteMehak.setOnClickListener(v -> {
                if (slotsFilled < totalSlots) {
                    cardMehakWait.setVisibility(View.GONE);
                    rowMehakAccepted.setVisibility(View.VISIBLE);
                    divMehakAccepted.setVisibility(View.VISIBLE);
                    promoteHeaderUpdate("Mehak");
                } else {
                    Toast.makeText(this, "No slots available! Add a slot first.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Global Actions
        MaterialButton btnAddSlot = findViewById(R.id.btnAddSlot);
        if (btnAddSlot != null) {
            btnAddSlot.setOnClickListener(v -> {
                totalSlots++;
                updateProgressUI();
                Toast.makeText(this, "Total slots increased to " + totalSlots, Toast.LENGTH_SHORT).show();
            });
        }

        MaterialButton btnCloseApps = findViewById(R.id.btnCloseApps);
        if (btnCloseApps != null) {
            btnCloseApps.setOnClickListener(v -> {
                Toast.makeText(this, "Applications closed.", Toast.LENGTH_SHORT).show();
                if (btnPromoteParth != null) btnPromoteParth.setEnabled(false);
                if (btnPromoteMehak != null) btnPromoteMehak.setEnabled(false);
            });
        }

        // Initial UI Sync
        updateProgressUI();
    }

    private void promoteHeaderUpdate(String name) {
        slotsFilled++;
        updateProgressUI();
        Toast.makeText(this, name + " promoted to Accepted Volunteers!", Toast.LENGTH_SHORT).show();
    }

    private void updateProgressUI() {
        if (tvSlotsFilled != null) {
            tvSlotsFilled.setText(slotsFilled + " of " + totalSlots + " slots filled");
        }
        if (pbSlots != null) {
            int progress = (int) (((float) slotsFilled / totalSlots) * 100);
            pbSlots.setProgress(progress);
        }
    }

    private void showOptionsMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add("Message All Volunteers");
        popup.getMenu().add("Export Participation Data");
        popup.getMenu().add("Share Group Link");
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(this, item.getTitle() + " feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        });
        popup.show();
    }
}
