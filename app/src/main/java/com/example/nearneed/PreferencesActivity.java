package com.example.nearneed;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.LinkedHashMap;
import java.util.Map;

public class PreferencesActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnEnter;
    private ChipGroup cgNeeds, cgOffers;

    // Define all available needs and their corresponding base icon
    private final Map<String, String> needsData = new LinkedHashMap<String, String>() {{
        put("Moving Help", "truck");
        put("Plumbing", "wrench");
        put("Electrical", "plug");
        put("Cleaning", "broom");
        put("Cooking", "oven");
        put("Gardening", "plant");
        put("Pet Sitting", "paw");
    }};

    // Define all available offers and their corresponding base icon
    private final Map<String, String> offersData = new LinkedHashMap<String, String>() {{
        put("Moving Help", "truck");
        put("Plumbing", "wrench");
        put("Electrical", "plug");
        put("Cleaning", "broom");
        put("Cooking", "oven");
        put("IT Support", "laptop");
        put("Car Wash", "car");
    }};

    private com.google.android.material.chip.Chip chipOtherNeed, chipOtherOffer;
    private com.google.android.material.textfield.TextInputLayout tilOtherNeed, tilOtherOffer;
    private android.widget.EditText etOtherNeed, etOtherOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEnter = findViewById(R.id.btnEnter);
        cgNeeds = findViewById(R.id.cgNeeds);
        cgOffers = findViewById(R.id.cgOffers);

        chipOtherNeed = findViewById(R.id.chipOtherNeed);
        tilOtherNeed = findViewById(R.id.tilOtherNeed);
        etOtherNeed = findViewById(R.id.etOtherNeed);

        chipOtherOffer = findViewById(R.id.chipOtherOffer);
        tilOtherOffer = findViewById(R.id.tilOtherOffer);
        etOtherOffer = findViewById(R.id.etOtherOffer);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        // Handle "Other" toggles
        chipOtherNeed.setOnCheckedChangeListener((bv, isChecked) -> {
            tilOtherNeed.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
        });

        chipOtherOffer.setOnCheckedChangeListener((bv, isChecked) -> {
            tilOtherOffer.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
        });

        btnEnter.setOnClickListener(v -> {
            if (validateSelections()) {
                Intent intent = new Intent(this, CommunityPreferencesActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateSelections() {
        // Validate Needs
        if (cgNeeds.getCheckedChipIds().size() == 0) {
            Toast.makeText(this, "Please select at least one thing you need", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (chipOtherNeed.isChecked() && etOtherNeed.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please specify your custom need", Toast.LENGTH_SHORT).show();
            etOtherNeed.requestFocus();
            return false;
        }

        // Validate Offers
        if (cgOffers.getCheckedChipIds().size() == 0) {
            Toast.makeText(this, "Please select at least one task you can offer", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (chipOtherOffer.isChecked() && etOtherOffer.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please specify your custom task", Toast.LENGTH_SHORT).show();
            etOtherOffer.requestFocus();
            return false;
        }

        return true;
    }
}
