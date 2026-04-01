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
            btnForgotPassword.setOnClickListener(v -> showOtpPopup());
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
        });

        dialogView.findViewById(R.id.btnAddSelected).setOnClickListener(v -> {
            View addMoreBtn = parentGroup.findViewById(R.id.btnAddService);
            parentGroup.removeAllViews();
            
            for (int i = 0; i < cgAllServices.getChildCount(); i++) {
                Chip chip = (Chip) cgAllServices.getChildAt(i);
                if (chip.getId() == R.id.chipOthers) {
                    if (chip.isChecked()) {
                        String otherText = etOtherService.getText().toString().trim();
                        if (!otherText.isEmpty()) {
                            addNewServiceChip(parentGroup, otherText);
                        }
                    }
                } else if (chip.isChecked()) {
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
        chip.setChipStrokeWidth(1);
        chip.setTextColor(getResources().getColor(R.color.brand_primary));
        chip.setCloseIconTintResource(R.color.brand_primary);
        // Overwrite height to 48dp for consistency with Preferences
        android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            (int) (48 * getResources().getDisplayMetrics().density)
        );
        chip.setLayoutParams(params);
        chip.setGravity(android.view.Gravity.CENTER);
        
        chip.setOnCloseIconClickListener(v -> parent.removeView(chip));
        
        View addButton = parent.findViewById(R.id.btnAddService);
        if (addButton != null) {
            parent.addView(chip, parent.indexOfChild(addButton));
        } else {
            parent.addView(chip);
        }
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
