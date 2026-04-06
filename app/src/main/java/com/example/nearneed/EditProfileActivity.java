package com.example.nearneed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivProfilePicture;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivProfilePicture = findViewById(R.id.ivProfilePicture);

        // Photo Picker Setup
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    ivProfilePicture.setImageURI(uri);
                    Toast.makeText(this, "Profile photo updated!", Toast.LENGTH_SHORT).show();
                }
            }
        );

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Photo Click
        View.OnClickListener photoClick = v -> galleryLauncher.launch("image/*");
        findViewById(R.id.profileImageContainer).setOnClickListener(photoClick);
        findViewById(R.id.btnChangePhoto).setOnClickListener(photoClick);
        
        Chip btnAddService = findViewById(R.id.btnAddService);
        ChipGroup cgServices = findViewById(R.id.cgServices);
        
        // Initial load from SharedPreferences
        loadSavedServices(cgServices);
        
        // Setup existing static chips and newly loaded ones
        if (cgServices != null) {
            for (int i = 0; i < cgServices.getChildCount(); i++) {
                View child = cgServices.getChildAt(i);
                if (child instanceof Chip && child.getId() != R.id.btnAddService) {
                    ((Chip) child).setOnCloseIconClickListener(v -> cgServices.removeView(child));
                }
            }
        }

        if (btnAddService != null) {
            btnAddService.setOnClickListener(v -> showServiceSelectionPopup(cgServices));
        }

        TextView btnForgotPassword = findViewById(R.id.btnForgotPassword);
        if (btnForgotPassword != null) {
            btnForgotPassword.setOnClickListener(v -> showEmailPromptPopup());
        }

        EditText etBio = findViewById(R.id.etBio);
        TextView tvBioCount = findViewById(R.id.tvBioCount);

        if (etBio != null && tvBioCount != null) {
            etBio.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tvBioCount.setText((s != null ? s.length() : 0) + "/150");
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
            // Initial count
            tvBioCount.setText(etBio.getText().length() + "/150");
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            saveServices(cgServices);
            Toast.makeText(this, "Profile changes saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadSavedServices(ChipGroup group) {
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPrefs", MODE_PRIVATE);
        String savedString = prefs.getString("user_offers_csv", "");
        
        if (savedString.trim().isEmpty()) {
            // Failsafe: Add mock tags so user can test delete feature
            addNewServiceChip(group, "House Help");
            addNewServiceChip(group, "Plumbing");
            return;
        }
        
        String[] services = savedString.split(",");
        for (String service : services) {
            String trimmed = service.trim();
            if (!trimmed.isEmpty()) {
                addNewServiceChip(group, trimmed);
            }
        }
    }

    private void saveServices(ChipGroup group) {
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPrefs", MODE_PRIVATE);
        List<String> offersToSave = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof Chip && child.getId() != R.id.btnAddService) {
                offersToSave.add(((Chip) child).getText().toString());
            }
        }
        String savedString = TextUtils.join(",", offersToSave);
        prefs.edit().putString("user_offers_csv", savedString).apply();
    }

    private void showServiceSelectionPopup(ChipGroup parentGroup) {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_select_services, null);
        dialog.setContentView(dialogView);

        ChipGroup cgAllServices = dialogView.findViewById(R.id.cgAllServices);
        
        // Pre-select existing
        Set<String> currentServices = new HashSet<>();
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            View child = parentGroup.getChildAt(i);
            if (child instanceof Chip && child.getId() != R.id.btnAddService) {
                currentServices.add(((Chip) child).getText().toString());
            }
        }

        for (int i = 0; i < cgAllServices.getChildCount(); i++) {
            Chip chip = (Chip) cgAllServices.getChildAt(i);
            if (currentServices.contains(chip.getText().toString())) {
                chip.setChecked(true);
            }
        }
        
        Chip chipOthers = dialogView.findViewById(R.id.chipOthers);
        View tilOtherService = dialogView.findViewById(R.id.tilOtherService);
        EditText etOtherService = dialogView.findViewById(R.id.etOtherService);
        
        chipOthers.setOnCheckedChangeListener((v, isChecked) -> {
            tilOtherService.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) {
                etOtherService.requestFocus();
            }
        });

        etOtherService.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || 
                (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                String newService = etOtherService.getText().toString().trim();
                if (!newService.isEmpty()) {
                    addChoiceChipToDialog(cgAllServices, newService);
                    etOtherService.setText("");
                    tilOtherService.setVisibility(View.GONE);
                    chipOthers.setChecked(false);
                }
                return true;
            }
            return false;
        });

        dialogView.findViewById(R.id.btnAddSelected).setOnClickListener(v -> {
            View addMoreBtn = parentGroup.findViewById(R.id.btnAddService);
            parentGroup.removeAllViews();
            
            for (int i = 0; i < cgAllServices.getChildCount(); i++) {
                Chip chip = (Chip) cgAllServices.getChildAt(i);
                if (chip.getId() != R.id.chipOthers && chip.isChecked()) {
                    addNewServiceChip(parentGroup, chip.getText().toString());
                }
            }
            
            if (parentGroup.findViewById(R.id.btnAddService) == null && addMoreBtn != null) {
                if (addMoreBtn.getParent() != null) {
                    ((android.view.ViewGroup) addMoreBtn.getParent()).removeView(addMoreBtn);
                }
                parentGroup.addView(addMoreBtn);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addChoiceChipToDialog(ChipGroup group, String text) {
        // Check for duplicates
        for (int i = 0; i < group.getChildCount(); i++) {
            Chip existing = (Chip) group.getChildAt(i);
            if (existing.getText().toString().equalsIgnoreCase(text)) {
                existing.setChecked(true);
                return;
            }
        }

        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        
        float density = getResources().getDisplayMetrics().density;
        chip.setChipMinHeight(48 * density);
        chip.setChipCornerRadius(24 * density);
        
        chip.setChipBackgroundColor(ContextCompat.getColorStateList(this, R.color.sel_chip_bg_blue));
        chip.setChipStrokeColor(ContextCompat.getColorStateList(this, R.color.sel_chip_stroke_blue));
        chip.setChipStrokeWidth(density * 1.0f);
        
        chip.setTextAppearanceResource(com.google.android.material.R.style.TextAppearance_MaterialComponents_Chip);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.sel_chip_text_blue));
        chip.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
        
        // Exact padding to match Choice design
        chip.setChipStartPadding(12 * density);
        chip.setChipEndPadding(12 * density);
        chip.setTextStartPadding(4 * density);
        chip.setTextEndPadding(4 * density);

        chip.setChecked(true);

        // Add before "+ Others"
        View others = group.findViewById(R.id.chipOthers);
        group.addView(chip, group.indexOfChild(others));
    }

    private void addNewServiceChip(ChipGroup parent, String serviceName) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Chip && ((Chip) child).getText().toString().equalsIgnoreCase(serviceName)) {
                return;
            }
        }

        Chip chip = new Chip(this);
        chip.setText(serviceName);
        chip.setCloseIconVisible(true);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setChipStrokeColorResource(R.color.brand_primary);
        chip.setChipStrokeWidth(getResources().getDisplayMetrics().density * 1);
        chip.setTextColor(getResources().getColor(R.color.brand_primary));
        chip.setCloseIconTintResource(R.color.brand_primary);

        float density = getResources().getDisplayMetrics().density;
        chip.setChipMinHeight(48 * density);
        chip.setChipCornerRadius(24 * density);
        chip.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
        
        chip.setChipStartPadding(12 * density);
        chip.setChipEndPadding(12 * density);
        chip.setTextStartPadding(4 * density);
        chip.setTextEndPadding(4 * density);
        chip.setGravity(android.view.Gravity.CENTER);
        
        chip.setOnCloseIconClickListener(v -> parent.removeView(chip));
        
        View addButton = parent.findViewById(R.id.btnAddService);
        if (addButton != null) {
            parent.addView(chip, parent.indexOfChild(addButton));
        } else {
            parent.addView(chip);
        }
    }
    
    private void showEmailPromptPopup() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_email, null);
        dialog.setContentView(dialogView);
        
        dialogView.findViewById(R.id.btnSendOtp).setOnClickListener(v -> {
            EditText etEmail = dialogView.findViewById(R.id.etEmail);
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            showOtpPopup();
        });
        
        dialog.show();
    }

    private void showOtpPopup() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_otp_verify, null);
        dialog.setContentView(dialogView);

        EditText otp1 = dialogView.findViewById(R.id.otp1);
        EditText otp2 = dialogView.findViewById(R.id.otp2);
        EditText otp3 = dialogView.findViewById(R.id.otp3);
        EditText otp4 = dialogView.findViewById(R.id.otp4);

        setupOtpAutoFollow(otp1, otp2);
        setupOtpAutoFollow(otp2, otp3);
        setupOtpAutoFollow(otp3, otp4);

        dialogView.findViewById(R.id.btnVerifyOtp).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(EditProfileActivity.this, ResetPasswordActivity.class));
        });

        dialog.show();
    }

    private void setupOtpAutoFollow(final EditText current, final EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    next.requestFocus();
                }
            }
        });
    }
}
