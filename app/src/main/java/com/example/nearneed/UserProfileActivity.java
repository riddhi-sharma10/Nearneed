package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });

        findViewById(R.id.btnEditProfile).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class))
        );

        findViewById(R.id.btnShare).setOnClickListener(v -> 
            Toast.makeText(this, "Share invoked", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnMyPosts).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, MyPostsActivity.class))
        );

        findViewById(R.id.btnSavedTasks).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, SavedTasksActivity.class))
        );

        findViewById(R.id.btnHelpSupport).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, HelpSupportActivity.class))
        );

        findViewById(R.id.btnSettings).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, SettingsActivity.class))
        );
        
        findViewById(R.id.btnViewReviews).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, ReviewsActivity.class))
        );
        
        findViewById(R.id.btnSeeAllActivity).setOnClickListener(v -> 
            startActivity(new Intent(UserProfileActivity.this, RecentActivityActivity.class))
        );

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            com.google.android.material.bottomsheet.BottomSheetDialog dialog = new com.google.android.material.bottomsheet.BottomSheetDialog(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
            dialog.setContentView(dialogView);

            dialogView.findViewById(R.id.btnCancel).setOnClickListener(dView -> dialog.dismiss());
            dialogView.findViewById(R.id.btnConfirmLogout).setOnClickListener(dView -> {
                dialog.dismiss();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            dialog.show();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            finish();
        });
    }
}
