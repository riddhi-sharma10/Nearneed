package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CommunityPreferencesActivity extends AppCompatActivity {

    private com.google.android.material.chip.ChipGroup cgSkills;
    private com.google.android.material.chip.Chip chipOther;
    private com.google.android.material.textfield.TextInputLayout tilOtherSkill;
    private android.widget.EditText etOtherSkill;
    private android.widget.ImageButton btnBack;
    private com.google.android.material.button.MaterialButton btnEnter;

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
        chipOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tilOtherSkill.setVisibility(isChecked ? android.view.View.VISIBLE : android.view.View.GONE);
        });

        btnEnter.setOnClickListener(v -> {
            if (validateSelections()) {
                Intent intent = new Intent(this, IdVerificationActivity.class);
                startActivity(intent);
            }
        });
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
