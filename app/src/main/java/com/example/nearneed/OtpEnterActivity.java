package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class OtpEnterActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnSendOtp;
    private EditText etPhone;
    private android.view.View vPhoneUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_enter);

        btnBack = findViewById(R.id.btnBack);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        etPhone = findViewById(R.id.etPhone);
        vPhoneUnderline = findViewById(R.id.vPhoneUnderline);

        setupListeners();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        etPhone.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vPhoneUnderline.setBackgroundColor(androidx.core.content.ContextCompat.getColor(OtpEnterActivity.this, R.color.border_standard));
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnSendOtp.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (phone.length() < 10) {
                vPhoneUnderline.setBackgroundColor(0xFFEF4444); // Red error underline
                return;
            }

            boolean isSignup = getIntent().getBooleanExtra("IS_SIGNUP", false);
            
            Intent intent = new Intent(this, OtpVerifyActivity.class);
            intent.putExtra("IS_SIGNUP", isSignup);
            intent.putExtra("PHONE_NUMBER", phone);
            
            startActivity(intent);
            finish();
        });
    }
}
