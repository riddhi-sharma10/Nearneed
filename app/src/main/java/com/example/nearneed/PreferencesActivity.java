package com.example.nearneed;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
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
        chipOtherNeed.setOnClickListener(v -> {
            boolean isChecked = chipOtherNeed.isChecked();
            tilOtherNeed.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
            if (isChecked) {
                etOtherNeed.requestFocus();
            }
        });

        etOtherNeed.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String newSkill = etOtherNeed.getText().toString().trim();
                if (!newSkill.isEmpty()) {
                    addNewChip(cgNeeds, newSkill, R.color.sel_chip_bg_blue, R.color.sel_chip_stroke_blue, R.color.sel_chip_text_blue);
                    etOtherNeed.setText("");
                    tilOtherNeed.setVisibility(android.view.View.GONE);
                    chipOtherNeed.setChecked(false);
                }
                return true;
            }
            return false;
        });

        chipOtherOffer.setOnClickListener(v -> {
            boolean isChecked = chipOtherOffer.isChecked();
            tilOtherOffer.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
            if (isChecked) {
                etOtherOffer.requestFocus();
            }
        });

        etOtherOffer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String newSkill = etOtherOffer.getText().toString().trim();
                if (!newSkill.isEmpty()) {
                    addNewChip(cgOffers, newSkill, R.color.sel_chip_bg_blue, R.color.sel_chip_stroke_blue, R.color.sel_chip_text_blue);
                    etOtherOffer.setText("");
                    tilOtherOffer.setVisibility(android.view.View.GONE);
                    chipOtherOffer.setChecked(false);
                }
                return true;
            }
            return false;
        });

        btnEnter.setOnClickListener(v -> {
            if (validateSelections()) {
                savePreferences();
                Intent intent = new Intent(this, CommunityPreferencesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePreferences() {
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPrefs", MODE_PRIVATE);
        java.util.List<String> selectedOffers = new java.util.ArrayList<>();
        
        for (int i = 0; i < cgOffers.getChildCount(); i++) {
            Chip chip = (Chip) cgOffers.getChildAt(i);
            if (chip.isChecked()) {
                selectedOffers.add(chip.getText().toString());
            }
        }
        
        String csv = android.text.TextUtils.join(",", selectedOffers);
        prefs.edit().putString("user_offers_csv", csv).apply();
    }

    private void addNewChip(ChipGroup chipGroup, String text, int bgColorId, int strokeColorId, int textColorId) {
        // Check if chip with same text already exists
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip existingChip = (Chip) chipGroup.getChildAt(i);
            if (existingChip.getText().toString().equalsIgnoreCase(text)) {
                existingChip.setChecked(true);
                return;
            }
        }

        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        
        // Match dimensions of pre-defined Choice chips
        float density = getResources().getDisplayMetrics().density;
        chip.setChipMinHeight(48 * density);
        chip.setChipCornerRadius(24 * density);
        
        // Clean appearance matching Choice style
        chip.setChipBackgroundColor(ContextCompat.getColorStateList(this, bgColorId));
        chip.setChipStrokeColor(ContextCompat.getColorStateList(this, strokeColorId));
        chip.setChipStrokeWidth(density * 1.0f);
        
        // Text styling
        chip.setTextAppearanceResource(com.google.android.material.R.style.TextAppearance_MaterialComponents_Chip);
        chip.setTextColor(ContextCompat.getColorStateList(this, textColorId));
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        
        chip.setChipStartPadding(12 * density);
        chip.setChipEndPadding(12 * density);
        chip.setTextStartPadding(4 * density);
        chip.setTextEndPadding(4 * density);

        // Automatically select the new entry
        chip.setChecked(true);

        // Add before the '+ Other' chip
        int index = chipGroup.getChildCount() - 1;
        chipGroup.addView(chip, index);
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
