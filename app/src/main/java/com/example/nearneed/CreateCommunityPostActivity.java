package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateCommunityPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community_post);
        
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        MaterialButton btnContinue = findViewById(R.id.btnContinueCommunity);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                startActivity(new Intent(this, CreateCommunityPostStep2Activity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            });
        }

        // ── VOLUNTEER SECTION LOGIC ──
        android.widget.TextView tvVolCount = findViewById(R.id.tvVolCount);
        android.widget.TextView btnVolMinus = findViewById(R.id.btnVolMinus);
        android.widget.TextView btnVolPlus = findViewById(R.id.btnVolPlus);
        androidx.appcompat.widget.SwitchCompat switchMax = findViewById(R.id.switchMaxVolunteers);

        final int[] volCount = {1};
        if (btnVolMinus != null && tvVolCount != null) {
            btnVolMinus.setOnClickListener(v -> {
                if (volCount[0] > 1) {
                    volCount[0]--;
                    tvVolCount.setText(String.valueOf(volCount[0]));
                }
            });
        }
        if (btnVolPlus != null && tvVolCount != null) {
            btnVolPlus.setOnClickListener(v -> {
                volCount[0]++;
                tvVolCount.setText(String.valueOf(volCount[0]));
            });
        }

        if (switchMax != null) {
            switchMax.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    switchMax.setThumbTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.brand_error_solid)));
                    switchMax.setTrackTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.brand_error_track)));
                } else {
                    switchMax.setThumbTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.text_muted)));
                    switchMax.setTrackTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.border_standard)));
                }
            });
            // Initial state
            switchMax.setChecked(true);
        }

        // ── CATEGORY SECTION LOGIC ──
        com.google.android.material.chip.ChipGroup cgCategory = findViewById(R.id.chipGroupCategory);
        com.google.android.material.chip.Chip chipOther = findViewById(R.id.chipOther);
        android.widget.EditText etOther = findViewById(R.id.etOtherCategory);

        if (cgCategory != null) {
            cgCategory.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.contains(R.id.chipOther)) {
                    etOther.setVisibility(android.view.View.VISIBLE);
                    etOther.requestFocus();
                } else {
                    etOther.setVisibility(android.view.View.GONE);
                }
            });
        }

        if (etOther != null) {
            etOther.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    String customCat = etOther.getText().toString().trim();
                    if (!customCat.isEmpty()) {
                        chipOther.setText(customCat);
                        etOther.setVisibility(android.view.View.GONE);
                        // Hide keyboard
                        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etOther.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            });
        }

        // ── POST VALIDITY LOGIC ──
        android.widget.TextView tvDate = findViewById(R.id.tvValidityDate);
        android.widget.TextView tvTime = findViewById(R.id.tvValidityTime);

        if (tvDate != null) {
            tvDate.setOnClickListener(v -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                new android.app.DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvDate.setText(date);
                }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
            });
        }

        if (tvTime != null) {
            tvTime.setOnClickListener(v -> {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = cal.get(java.util.Calendar.MINUTE);
                
                android.app.TimePickerDialog timePicker = new android.app.TimePickerDialog(this, 
                    android.app.AlertDialog.THEME_HOLO_LIGHT, // Spinner style
                    (view, selectedHour, selectedMin) -> {
                        String ampm = selectedHour < 12 ? "AM" : "PM";
                        int h = selectedHour > 12 ? selectedHour - 12 : (selectedHour == 0 ? 12 : selectedHour);
                        String formattedTime = String.format("%02d:%02d:00 %s", h, selectedMin, ampm);
                        tvTime.setText(formattedTime);
                    }, hour, minute, false);
                timePicker.show();
            });
        }
    }
}
