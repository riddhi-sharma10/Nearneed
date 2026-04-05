package com.example.nearneed;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.nearneed.R;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import java.util.Locale;

public class GigDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_PRICE = "extra_price";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_DISTANCE = "extra_distance";
    public static final String EXTRA_IS_OWNER = "extra_is_owner";

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler updateHandler;
    private long gigPostTimestamp;
    private double userLatitude = 28.6139;  // Default: Delhi coordinates
    private double userLongitude = 77.2090;
    private double gigLatitude = 28.6139;   // Default: Same location
    private double gigLongitude = 77.2090;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnOptions = findViewById(R.id.btnOptions);
        boolean isOwner = getIntent().getBooleanExtra(EXTRA_IS_OWNER, false);
        
        if (btnOptions != null) {
            btnOptions.setOnClickListener(v -> {
                if (isOwner) {
                    showManagementMenu(v);
                } else {
                    Toast.makeText(this, "More options coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageButton btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> shareGigDetails());
        }

        MaterialButton btnGigAction = findViewById(R.id.btnGigAction);
        if (isOwner) {
            btnGigAction.setText("Posted");
            btnGigAction.setOnClickListener(v -> {
                Toast.makeText(this, "You have posted this gig.", Toast.LENGTH_SHORT).show();
            });
        } else {
            btnGigAction.setText("Apply for this Gig →");
            btnGigAction.setOnClickListener(v -> showApplySheet());
        }

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String price = intent.getStringExtra(EXTRA_PRICE);
            String desc = intent.getStringExtra(EXTRA_DESC);
            String distance = intent.getStringExtra(EXTRA_DISTANCE);

            if (title != null) {
                TextView tvTitle = findViewById(R.id.tvGigTitle);
                if (tvTitle != null) tvTitle.setText(title);
            }
            if (price != null) {
                TextView tvPrice = findViewById(R.id.tvGigPrice);
                if (tvPrice != null) tvPrice.setText(price);
            }
            if (desc != null) {
                TextView tvDesc = findViewById(R.id.tvGigDesc);
                if (tvDesc != null) tvDesc.setText(desc);
            }
        }

        // Setup audio player
        setupAudioPlayer();

        // Setup author profile interactions
        setupAuthorProfileInteractions();

        // Setup real-time updates for time and distance
        setupRealTimeUpdates();
    }

    private void setupAuthorProfileInteractions() {
        View cardAuthorProfile = findViewById(R.id.cardAuthorProfile);
        View btnCallAuthor = findViewById(R.id.btnCallAuthor);
        View btnMessageAuthor = findViewById(R.id.btnMessageAuthor);
        
        // Click card to view full profile
        if (cardAuthorProfile != null) {
            cardAuthorProfile.setOnClickListener(v -> {
                Toast.makeText(this, "Viewing full profile of Ramesh Kumar", Toast.LENGTH_SHORT).show();
            });
        }
        
        // Call button
        if (btnCallAuthor != null) {
            btnCallAuthor.setOnClickListener(v -> callAuthor());
        }
        
        // Message button
        if (btnMessageAuthor != null) {
            btnMessageAuthor.setOnClickListener(v -> messageAuthor());
        }
    }

    private void setupRealTimeUpdates() {
        // Set initial timestamp to current time (in production, get from server)
        gigPostTimestamp = System.currentTimeMillis();
        
        // Start updating time and distance every minute
        updateHandler = new Handler(Looper.getMainLooper());
        updateTimeAndDistance();
    }

    private void updateTimeAndDistance() {
        TextView tvTimeAgo = findViewById(R.id.tvTimeAgo);
        TextView tvDistance = findViewById(R.id.tvDistance);
        
        if (tvTimeAgo != null) {
            tvTimeAgo.setText(getRelativeTimeString());
        }
        if (tvDistance != null) {
            tvDistance.setText(getDistanceString());
        }
        
        // Update every 60 seconds
        updateHandler.postDelayed(this::updateTimeAndDistance, 60000);
    }

    private String getRelativeTimeString() {
        long currentTime = System.currentTimeMillis();
        long timeDiffMs = currentTime - gigPostTimestamp;
        long timeDiffSecs = timeDiffMs / 1000;
        long timeDiffMins = timeDiffSecs / 60;
        long timeDiffHours = timeDiffMins / 60;
        long timeDiffDays = timeDiffHours / 24;
        
        if (timeDiffMins < 1) {
            return "just now";
        } else if (timeDiffMins < 60) {
            return timeDiffMins + "m ago";
        } else if (timeDiffHours < 24) {
            return timeDiffHours + "h ago";
        } else {
            return timeDiffDays + "d ago";
        }
    }

    private String getDistanceString() {
        double distance = calculateDistance(userLatitude, userLongitude, gigLatitude, gigLongitude);
        
        if (distance < 1) {
            return String.format(Locale.US, "%.1f m away", distance * 1000);
        } else {
            return String.format(Locale.US, "%.1f km away", distance);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void setupAudioPlayer() {
        ImageView ivPlayPauseButton = findViewById(R.id.ivPlayPauseButton);
        if (ivPlayPauseButton != null) {
            ivPlayPauseButton.setOnClickListener(v -> toggleAudioPlayPause());
        }
    }

    private void toggleAudioPlayPause() {
        ImageView ivPlayPauseButton = findViewById(R.id.ivPlayPauseButton);
        
        if (!isPlaying) {
            // Start playing
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                // Load audio file - using a raw resource or URL
                try {
                    // Example: mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gig_audio"));
                    // For now, using a simple placeholder that you can replace with actual audio
                    mediaPlayer.setOnCompletionListener(mp -> {
                        isPlaying = false;
                        if (ivPlayPauseButton != null) {
                            ivPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Toast.makeText(this, "Error loading audio", Toast.LENGTH_SHORT).show();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    return;
                }
            }
            mediaPlayer.start();
            isPlaying = true;
            if (ivPlayPauseButton != null) {
                ivPlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
        } else {
            // Pause playing
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            isPlaying = false;
            if (ivPlayPauseButton != null) {
                ivPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Stop real-time updates
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
        }
        
        // Stop audio playback
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void showManagementMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_gig_detail_owner, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_view_volunteers) {
                Intent intent = new Intent(this, GigApplicantsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_view_responses) {
                Intent intent = new Intent(this, GigApplicantsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_update_status) {
                Intent intent = new Intent(this, PostStatusActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_rate_volunteers) {
                Toast.makeText(this, "Opening Rating Interface...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_save) {
                Toast.makeText(this, "Post saved successfully", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_help) {
                Intent intent = new Intent(this, HelpSupportActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (id == R.id.menu_report) {
                Toast.makeText(this, "Opening Report Interface...", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void shareGigDetails() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(EXTRA_TITLE);
        String price = intent.getStringExtra(EXTRA_PRICE);
        String desc = intent.getStringExtra(EXTRA_DESC);
        
        String shareText = "Check out this Gig!\n\n" +
                "Title: " + (title != null ? title : "Gig") + "\n" +
                "Price: " + (price != null ? price : "N/A") + "\n" +
                "Description: " + (desc != null ? desc : "N/A") + "\n\n" +
                "Available on NearNeed App";
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Gig on NearNeed!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        startActivity(Intent.createChooser(shareIntent, "Share Gig via"));
    }

    private void callAuthor() {
        // TODO: Replace with actual phone number from user data
        String phoneNumber = "9876543210"; // Placeholder
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(android.net.Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to make call", Toast.LENGTH_SHORT).show();
        }
    }

    private void messageAuthor() {
        // TODO: Replace with actual chat implementation or SMS
        String phoneNumber = "9876543210"; // Placeholder
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(android.net.Uri.parse("smsto:" + phoneNumber));
        smsIntent.putExtra("sms_body", "Hi! I'm interested in your gig.");
        try {
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to send message", Toast.LENGTH_SHORT).show();
        }
    }

    private void showApplySheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_apply_gig, null);
        dialog.setContentView(sheetView);

        Intent intent = getIntent();
        String postedPriceStr = intent.getStringExtra(EXTRA_PRICE);
        if (postedPriceStr == null) postedPriceStr = "₹350";
        
        TextView tvAcceptLabel = sheetView.findViewById(R.id.tvAcceptPostedPrice);
        tvAcceptLabel.setText("Accept posted price (" + postedPriceStr + ")");

        // Parse numerical value (e.g. ₹350 -> 350)
        int initialPrice = 350;
        try {
            initialPrice = Integer.parseInt(postedPriceStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) { /* fallback */ }

        final int[] proposedPrice = {initialPrice + 50};
        TextView tvProposedPrice = sheetView.findViewById(R.id.tvProposedPrice);
        TextView tvPriceDifference = sheetView.findViewById(R.id.tvPriceDifference);
        tvProposedPrice.setText("₹ " + proposedPrice[0]);
        tvPriceDifference.setText("+ ₹" + (proposedPrice[0] - initialPrice) + " from posted price");
        
        final String finalPostedPriceStr = postedPriceStr;
        RadioButton rbPropose = sheetView.findViewById(R.id.rbProposeDifferent);
        RadioButton rbAccept = sheetView.findViewById(R.id.rbAcceptPosted);
        TextView tvPropose = sheetView.findViewById(R.id.tvProposeDifferent);
        TextView tvAccept = sheetView.findViewById(R.id.tvAcceptPostedPrice);
        com.google.android.material.card.MaterialCardView cardPropose = sheetView.findViewById(R.id.cardProposeDifferent);
        com.google.android.material.card.MaterialCardView cardAccept = sheetView.findViewById(R.id.cardAcceptPosted);
        View negotiator = sheetView.findViewById(R.id.llPriceNegotiator);

        View.OnClickListener selectPropose = v -> {
            rbPropose.setChecked(true);
            rbAccept.setChecked(false);

            // Selected: Propose card
            cardPropose.setStrokeColor(0xFF1A6FD4);
            cardPropose.setStrokeWidth(3);
            cardPropose.setCardBackgroundColor(0xFFE8F1FC);
            tvPropose.setTextColor(0xFF1A6FD4);

            // Unselected: Accept card
            cardAccept.setStrokeColor(getResources().getColor(R.color.border_standard));
            cardAccept.setStrokeWidth(2);
            cardAccept.setCardBackgroundColor(0xFFFFFFFF);
            tvAccept.setTextColor(getResources().getColor(R.color.text_subheadline));

            negotiator.setVisibility(View.VISIBLE);
            negotiator.setAlpha(1.0f);
            negotiator.setEnabled(true);
        };

        View.OnClickListener selectAccept = v -> {
            rbAccept.setChecked(true);
            rbPropose.setChecked(false);

            // Selected: Accept card
            cardAccept.setStrokeColor(0xFF1A6FD4);
            cardAccept.setStrokeWidth(3);
            cardAccept.setCardBackgroundColor(0xFFE8F1FC);
            tvAccept.setTextColor(0xFF1A6FD4);

            // Unselected: Propose card
            cardPropose.setStrokeColor(getResources().getColor(R.color.border_standard));
            cardPropose.setStrokeWidth(2);
            cardPropose.setCardBackgroundColor(0xFFFFFFFF);
            tvPropose.setTextColor(getResources().getColor(R.color.text_header));

            negotiator.setVisibility(View.GONE);
            negotiator.setEnabled(false);
        };

        rbPropose.setOnClickListener(selectPropose);
        cardPropose.setOnClickListener(selectPropose);

        rbAccept.setOnClickListener(selectAccept);
        cardAccept.setOnClickListener(selectAccept);

        // Set initial state
        selectAccept.onClick(null);

        sheetView.findViewById(R.id.btnIncreasePrice).setOnClickListener(v1 -> {
            if (negotiator.isEnabled()) {
                proposedPrice[0] += 50;
                tvProposedPrice.setText("₹ " + proposedPrice[0]);
                tvPriceDifference.setText("+ ₹" + (proposedPrice[0] - initialPrice) + " from posted price");
            }
        });

        sheetView.findViewById(R.id.btnDecreasePrice).setOnClickListener(v1 -> {
            if (negotiator.isEnabled() && proposedPrice[0] > 50) {
                proposedPrice[0] -= 50;
                tvProposedPrice.setText("₹ " + proposedPrice[0]);
                tvPriceDifference.setText("+ ₹" + (proposedPrice[0] - initialPrice) + " from posted price");
            }
        });

        // Payment preference listeners
        final String[] selectedPayment = {"UPI"};
        View llUpiOption = sheetView.findViewById(R.id.llUpiOption);
        View llCashOption = sheetView.findViewById(R.id.llCashOption);
        TextView tvUpi = sheetView.findViewById(R.id.tvUpi);
        TextView tvCash = sheetView.findViewById(R.id.tvCash);

        // Set initial UPI as selected
        llUpiOption.setBackgroundColor(0xFFE8F1FC);
        tvUpi.setTextColor(0xFF1A6FD4);
        llCashOption.setBackgroundColor(getResources().getColor(android.R.color.white));
        tvCash.setTextColor(getResources().getColor(R.color.text_subheadline));

        llUpiOption.setOnClickListener(v -> {
            selectedPayment[0] = "UPI";
            llUpiOption.setBackgroundColor(0xFFE8F1FC);
            tvUpi.setTextColor(0xFF1A6FD4);
            llCashOption.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvCash.setTextColor(getResources().getColor(R.color.text_subheadline));
        });

        llCashOption.setOnClickListener(v -> {
            selectedPayment[0] = "Cash";
            llCashOption.setBackgroundColor(0xFFE8F1FC);
            tvCash.setTextColor(0xFF1A6FD4);
            llUpiOption.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvUpi.setTextColor(getResources().getColor(R.color.text_subheadline));
        });

        sheetView.findViewById(R.id.btnSendResponse).setOnClickListener(v -> {
            dialog.dismiss();
            
            String paymentMethod = selectedPayment[0];
            String finalMsg = rbAccept.isChecked() ? "Application Sent at posted price: " + finalPostedPriceStr + " via " + paymentMethod
                                                   : "Application Sent with proposed price: ₹" + proposedPrice[0] + " via " + paymentMethod;
            Toast.makeText(this, finalMsg, Toast.LENGTH_SHORT).show();
            
            // Transition to Success Popup Screen
            Intent successIntent = new Intent(this, ApplySuccessActivity.class);
            startActivity(successIntent);
            finish();
        });

        dialog.show();
    }
}
