package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

public class CommunityPreferencesActivity extends AppCompatActivity {

    private ChipGroup cgSkills;
    private Chip chipOther;
    private TextInputLayout tilOtherSkill;
    private EditText etOtherSkill;
    private ImageButton btnBack;
    private MaterialButton btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_preferences);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEnter = findViewById(R.id.btnEnter);
        cgSkills = findViewById(R.id.cgSkills);
        chipOther = findViewById(R.id.chipOther);
        tilOtherSkill = findViewById(R.id.tilOtherSkill);
        etOtherSkill = findViewById(R.id.etOtherSkill);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        // Handle "Other" chip selection
        chipOther.setOnClickListener(v -> {
            boolean isChecked = chipOther.isChecked();
            tilOtherSkill.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
            if (isChecked) {
                etOtherSkill.requestFocus();
            }
        });

        etOtherSkill.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String newSkill = etOtherSkill.getText().toString().trim();
                if (!newSkill.isEmpty()) {
                    addNewChip(cgSkills, newSkill, R.color.sel_chip_bg_red, R.color.sel_chip_stroke_red, R.color.sel_chip_text_red);
                    etOtherSkill.setText("");
                    tilOtherSkill.setVisibility(View.GONE);
                    chipOther.setChecked(false);
                }
                return true;
            }
            return false;
        });

        btnEnter.setOnClickListener(v -> {
            if (validateSelections()) {
                saveSkills();
                Intent intent = new Intent(this, IdVerificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveSkills() {
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPrefs", MODE_PRIVATE);
        String currentCsv = prefs.getString("user_offers_csv", "");
        
        java.util.Set<String> allSkills = new java.util.LinkedHashSet<>();
        if (!currentCsv.isEmpty()) {
            for (String s : currentCsv.split(",")) {
                allSkills.add(s.trim());
            }
        }
        
        for (int i = 0; i < cgSkills.getChildCount(); i++) {
            Chip chip = (Chip) cgSkills.getChildAt(i);
            if (chip.isChecked()) {
                allSkills.add(chip.getText().toString());
            }
        }
        
        String finalCsv = android.text.TextUtils.join(",", allSkills);
        prefs.edit().putString("user_offers_csv", finalCsv).apply();
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

        chip.setChecked(true);

        // Add before the '+ Other' chip
        int index = chipGroup.getChildCount() - 1;
        chipGroup.addView(chip, index);
    }

    private boolean validateSelections() {
        int checkedCount = cgSkills.getCheckedChipIds().size();
        
        if (checkedCount == 0) {
            Toast.makeText(this, "Please select at least one skill to offer", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (chipOther.isChecked()) {
            String otherText = etOtherSkill.getText().toString().trim();
            if (otherText.isEmpty()) {
                Toast.makeText(this, "Please specify your 'Other' skill", Toast.LENGTH_SHORT).show();
                etOtherSkill.requestFocus();
                return false;
            }
        }

        return true;
    }
}
