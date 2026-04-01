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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_enter);

        btnBack = findViewById(R.id.btnBack);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        etPhone = findViewById(R.id.etPhone);

        setupListeners();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnSendOtp.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (phone.length() < 10) {
                Toast.makeText(this, getString(R.string.txt_please_enter_a_valid_10_digit), Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isSignup = getIntent().getBooleanExtra("IS_SIGNUP", false);
            
            Intent loadingIntent = new Intent(this, LoadingActivity.class);
            loadingIntent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, OtpVerifyActivity.class.getName());
            loadingIntent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGE, "Sending OTP...");
            loadingIntent.putExtra(LoadingActivity.EXTRA_DURATION_MS, 1200L);
            loadingIntent.putExtra("IS_SIGNUP", isSignup);
            loadingIntent.putExtra("PHONE_NUMBER", phone);
            
            startActivity(loadingIntent);
            finish();
        });
    }
}
