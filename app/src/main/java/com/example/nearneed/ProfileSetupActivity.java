package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ProfileSetupActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnContinue;
    private MaterialButton btnUseLocation;
    private android.widget.EditText etSearchLocation;

    // Radius options grouping
    private LinearLayout opt5km, opt7km, opt10km;
    private TextView tv5kmTitle, tv5kmDesc;
    private TextView tv7kmTitle, tv7kmDesc;
    private TextView tv10kmTitle, tv10kmDesc;
    
    private int selectedRadius = 7; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        btnUseLocation = findViewById(R.id.btnUseLocation);
        etSearchLocation = findViewById(R.id.etSearchLocation);

        opt5km = findViewById(R.id.opt5km);
        opt7km = findViewById(R.id.opt7km);
        opt10km = findViewById(R.id.opt10km);

        tv5kmTitle = findViewById(R.id.tv5kmTitle);
        tv5kmDesc = findViewById(R.id.tv5kmDesc);
        tv7kmTitle = findViewById(R.id.tv7kmTitle);
        tv7kmDesc = findViewById(R.id.tv7kmDesc);
        tv10kmTitle = findViewById(R.id.tv10kmTitle);
        tv10kmDesc = findViewById(R.id.tv10kmDesc);

        // Pre-select 7km explicitly
        selectRadiusOption(7);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        // Continue to Preferences Step
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
        });

        btnUseLocation.setOnClickListener(v -> {
            btnUseLocation.setText("Location Confirmed");
            btnUseLocation.setEnabled(false);
            btnUseLocation.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF16A34A)); // Success Green
            btnUseLocation.setIcon(null); // Remove icon if any
            Toast.makeText(this, "Location set to BML Munjal University", Toast.LENGTH_SHORT).show();
            // Clear search if location confirmed
            etSearchLocation.setText("");
        });

        // Search action logic
        etSearchLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                String query = etSearchLocation.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        // Mutually exclusive single selection logic (radio button behavior)
        opt5km.setOnClickListener(v -> selectRadiusOption(5));
        opt7km.setOnClickListener(v -> selectRadiusOption(7));
        opt10km.setOnClickListener(v -> selectRadiusOption(10));
    }

    private void selectRadiusOption(int radius) {
        selectedRadius = radius;

        // Reset all states
        setOptionState(opt5km, tv5kmTitle, tv5kmDesc, false);
        setOptionState(opt7km, tv7kmTitle, tv7kmDesc, false);
        setOptionState(opt10km, tv10kmTitle, tv10kmDesc, false);

        // Apply active state to selected
        if (radius == 5) {
            setOptionState(opt5km, tv5kmTitle, tv5kmDesc, true);
        } else if (radius == 7) {
            setOptionState(opt7km, tv7kmTitle, tv7kmDesc, true);
        } else if (radius == 10) {
            setOptionState(opt10km, tv10kmTitle, tv10kmDesc, true);
        }
    }

    private void setOptionState(LinearLayout container, TextView title, TextView desc, boolean isSelected) {
        if (isSelected) {
            container.setBackgroundResource(R.drawable.bg_radius_option_selected);
            title.setTextColor(0xFF2563EB); // Blue
            desc.setTextColor(0xFF3B82F6); // Lighter blue
        } else {
            container.setBackgroundResource(R.drawable.bg_radius_option_unselected);
            title.setTextColor(0xFF0F172A); // Dark slate
            desc.setTextColor(0xFF94A3B8); // Gray
        }
    }
}
