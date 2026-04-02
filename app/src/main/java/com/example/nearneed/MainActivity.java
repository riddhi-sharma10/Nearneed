package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FrameLayout cardMyPost = findViewById(R.id.cardMyPost);
        FrameLayout cardGig1 = findViewById(R.id.cardGig1);
        FrameLayout cardCommunity1 = findViewById(R.id.cardCommunity1);
        FrameLayout cardCommunity2 = findViewById(R.id.cardCommunity2);
        FrameLayout cardGig2 = findViewById(R.id.cardGig2);
        ChipGroup cgFilters = findViewById(R.id.cgFilters);
        
        // --- Static Community Post Countdowns ---
        setupCountdown(findViewById(R.id.tvCountdown1), 18000000); // 5 hours
        setupCountdown(findViewById(R.id.tvCountdown2), 3600000);  // 1 hour

        // --- User's Post Logic ---
        updateMyPostDisplayAndCountdown();

        // --- Rest of Dashboard Logic ---
        View notificationPopup = findViewById(R.id.notificationPopup);
        android.widget.ImageView ivNotifications = findViewById(R.id.ivNotifications);
        ivNotifications.setOnClickListener(v -> {
            ivNotifications.setImageResource(R.drawable.ic_bell);
            if (notificationPopup.getVisibility() == View.VISIBLE) {
                notificationPopup.setVisibility(View.GONE);
            } else {
                notificationPopup.setVisibility(View.VISIBLE);
            }
        });

        setupNotificationInteractions(notificationPopup);

        setupPostClickListeners();

        // ── Bottom Navigation ────────────────────────────────────────────────
        NavbarHelper.setup(this, NavbarHelper.TAB_HOME);

        cgFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int idx = group.indexOfChild(findViewById(checkedIds.get(0)));
            updateFilterDisplay(idx, cardMyPost, cardGig1, cardGig2, cardCommunity1, cardCommunity2);
        });
        
        updateFilterDisplay(0, cardMyPost, cardGig1, cardGig2, cardCommunity1, cardCommunity2);
    }

    private void setupCountdown(TextView tv, long millis) {
        if (tv == null || millis <= 0) {
            if (tv != null) tv.setVisibility(View.GONE);
            return;
        }
        tv.setVisibility(View.VISIBLE);
        new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tv.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d left", hours, minutes, seconds));
            }
            @Override
            public void onFinish() {
                tv.setText("Post Expired");
            }
        }.start();
    }

    private void setupPostClickListeners() {
        View btnVolC1 = findViewById(R.id.btnVolC1);
        if (btnVolC1 != null) {
            btnVolC1.setOnClickListener(v -> startActivityFade(CommunityVolunteerActivity.class));
        }

        View btnVolC2 = findViewById(R.id.btnVolC2);
        if (btnVolC2 != null) {
            btnVolC2.setOnClickListener(v -> startActivityFade(CommunityVolunteerActivity.class));
        }

        View btnViewC0 = findViewById(R.id.btnViewC0);
        if (btnViewC0 != null) {
            btnViewC0.setOnClickListener(v -> startActivityFade(MyCommunityPostDetailActivity.class));
        }

        View btnApplyGig1 = findViewById(R.id.btnApplyGig1);
        if (btnApplyGig1 != null) {
            btnApplyGig1.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, GigDetailActivity.class);
                intent.putExtra(GigDetailActivity.EXTRA_TITLE, getString(R.string.txt_plumbing_repair));
                intent.putExtra(GigDetailActivity.EXTRA_PRICE, getString(R.string.txt_price_400));
                intent.putExtra(GigDetailActivity.EXTRA_DESC, "Bathroom pipe burst, need experienced plumber ASAP.");
                intent.putExtra(GigDetailActivity.EXTRA_DISTANCE, "0.3 km  ·  2 min ago");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }

    private void startActivityFade(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void updateFilterDisplay(int idx, View my, View g1, View g2, View c1, View c2) {
        if (idx == 0) { // All
            my.setVisibility(View.VISIBLE); g1.setVisibility(View.VISIBLE); g2.setVisibility(View.VISIBLE);
            c1.setVisibility(View.VISIBLE); c2.setVisibility(View.VISIBLE);
        } else if (idx == 1) { // Gigs
            my.setVisibility(View.GONE); g1.setVisibility(View.VISIBLE); g2.setVisibility(View.VISIBLE);
            c1.setVisibility(View.GONE); c2.setVisibility(View.GONE);
        } else if (idx == 2) { // Community
            my.setVisibility(View.VISIBLE); g1.setVisibility(View.GONE); g2.setVisibility(View.GONE);
            c1.setVisibility(View.VISIBLE); c2.setVisibility(View.VISIBLE);
        } else if (idx == 3) { // Emergency
            my.setVisibility(View.GONE); g1.setVisibility(View.GONE); g2.setVisibility(View.GONE);
            c1.setVisibility(View.VISIBLE); c2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMyPostDisplayAndCountdown();
    }

    private void updateMyPostDisplayAndCountdown() {
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPosts", MODE_PRIVATE);
        String title = prefs.getString("LATEST_POST_TITLE", null);
        boolean isEmergency = prefs.getBoolean("LATEST_POST_IS_EMERGENCY", false);
        
        TextView tvTitle = findViewById(R.id.c0_title);
        TextView tvDesc = findViewById(R.id.c0_desc);
        TextView tvCountdown = findViewById(R.id.tvCountdown0);

        if (title != null) {
            findViewById(R.id.cardMyPost).setVisibility(View.VISIBLE);
            if (tvTitle != null) tvTitle.setText(title);
            if (tvDesc != null) tvDesc.setText(prefs.getString("LATEST_POST_DESC", ""));
            
            // Re-trigger countdown ONLY if it's an emergency post
            if (isEmergency) {
                setupCountdown(tvCountdown, 10800000); // 3 hours
                ((TextView)findViewById(R.id.c0_tag)).setText("● URGENT EMERGENCY");
                ((TextView)findViewById(R.id.c0_sub)).setText("Emergency Food Support");
            } else {
                if (tvCountdown != null) tvCountdown.setVisibility(View.GONE);
                ((TextView)findViewById(R.id.c0_tag)).setText("● YOUR POST");
                ((TextView)findViewById(R.id.c0_sub)).setText(prefs.getString("LATEST_POST_CATEGORY", "Community Project"));
            }
        }
    }

    private void setupNotificationInteractions(View popup) {
        View btnClose = findViewById(R.id.btnClosePopup);
        if (btnClose != null) btnClose.setOnClickListener(v -> popup.setVisibility(View.GONE));
        View btnClearAll = findViewById(R.id.btnClearAllNotifications);
        View scroll = findViewById(R.id.scrollNotifications);
        View emptyView = findViewById(R.id.empty_notif_view);
        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> {
                if (scroll != null) scroll.setVisibility(View.GONE);
                if (emptyView != null) emptyView.setVisibility(View.VISIBLE);
            });
        }
    }
}