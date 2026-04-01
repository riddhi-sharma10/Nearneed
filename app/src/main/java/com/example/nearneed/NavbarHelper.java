package com.example.nearneed;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/**
 * NavbarHelper – wires up the custom universal navbar for every screen.
 *
 * Usage (in any Activity.onCreate):
 *   NavbarHelper.setup(this, NavbarHelper.TAB_HOME);
 */
public class NavbarHelper {

    public static final int TAB_HOME     = 0;
    public static final int TAB_MAP      = 1;
    public static final int TAB_MESSAGES = 3;
    public static final int TAB_PROFILE  = 4;

    public static void setup(Activity activity, int activeTab) {

        LinearLayout navHome     = activity.findViewById(R.id.navHome);
        LinearLayout navMap      = activity.findViewById(R.id.navMap);
        LinearLayout navMessages = activity.findViewById(R.id.navMessages);
        LinearLayout navProfile  = activity.findViewById(R.id.navProfile);
        View         fabAdd      = activity.findViewById(R.id.fabAdd);

        if (navHome == null) return; // layout doesn't include navbar

        // Set active tab visual
        setActive(activity, navHome,     activeTab == TAB_HOME);
        setActive(activity, navMap,      activeTab == TAB_MAP);
        setActive(activity, navMessages, activeTab == TAB_MESSAGES);
        setActive(activity, navProfile,  activeTab == TAB_PROFILE);

        // Click listeners
        navHome.setOnClickListener(v -> {
            if (activeTab == TAB_HOME) return;
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        });

        navMap.setOnClickListener(v -> {
            if (activeTab == TAB_MAP) return;
            activity.startActivity(new Intent(activity, DiscoveryMapActivity.class));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        });

        navMessages.setOnClickListener(v -> {
            if (activeTab == TAB_MESSAGES) return;
            activity.startActivity(new Intent(activity, MessagesActivity.class));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        });

        navProfile.setOnClickListener(v -> {
            if (activeTab == TAB_PROFILE) return;
            activity.startActivity(new Intent(activity, UserProfileActivity.class));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        });

        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, CreatePostActivity.class));
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }

    private static void setActive(Activity activity, LinearLayout tab, boolean isActive) {
        if (tab == null) return;
        int activeColor = ContextCompat.getColor(activity, R.color.brand_primary);
        int mutedColor  = ContextCompat.getColor(activity, R.color.text_muted);
        int color = isActive ? activeColor : mutedColor;

        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(color);
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
                ((TextView) child).setTypeface(null,
                        isActive ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
            }
        }
    }
}
