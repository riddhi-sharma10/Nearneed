package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateAccountActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnSubmit;
    private TextInputEditText etFullName, etEmail, etPhone, etPassword;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnSubmit.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.matches("^\\d{10}$")) {
                Toast.makeText(this, "Please enter a valid 10-digit Indian phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).+$")) {
                Toast.makeText(this, "Password must be alphanumeric and include at least one special character", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(CreateAccountActivity.this, OtpEnterActivity.class);
            intent.putExtra("IS_SIGNUP", true);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccountActivity.this, OtpEnterActivity.class);
            intent.putExtra("IS_SIGNUP", false);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}
