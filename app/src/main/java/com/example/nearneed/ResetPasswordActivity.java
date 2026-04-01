package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        com.google.android.material.textfield.TextInputEditText etNewPassword = findViewById(R.id.etNewPassword);
        com.google.android.material.textfield.TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        com.google.android.material.textfield.TextInputLayout tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        MaterialButton btnSavePassword = findViewById(R.id.btnSavePassword);
        btnSavePassword.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();

            if (newPass.isEmpty()) {
                etNewPassword.setError("Password cannot be empty");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                tilConfirmPassword.setError("Passwords do not match");
                return;
            } else {
                tilConfirmPassword.setError(null);
            }

            // Logic to update password would go here.
            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
            
            // Redirect to Profile Page
            Intent intent = new Intent(ResetPasswordActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
