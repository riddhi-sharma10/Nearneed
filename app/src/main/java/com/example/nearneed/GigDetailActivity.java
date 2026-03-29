package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GigDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnApply).setOnClickListener(v -> showApplySheet());
    }

    private void showApplySheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_apply_gig, null);
        dialog.setContentView(sheetView);

        sheetView.findViewById(R.id.btnSendResponse).setOnClickListener(v -> {
            dialog.dismiss();
            
            // Show confirmation
            Toast.makeText(this, "Applied for the gig", Toast.LENGTH_SHORT).show();
            
            // Redirect to loading -> dashboard
            Intent loadingIntent = new Intent(this, LoadingActivity.class);
            loadingIntent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, MainActivity.class.getName());
            loadingIntent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGES, new String[]{
                "Sending application...",
                "Notifying neighbor...",
                "Securing spot...",
                "Done!"
            });
            startActivity(loadingIntent);
            finish();
        });

        dialog.show();
    }
}
