package com.example.nearneed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreateGigStep2Activity extends AppCompatActivity {

    private static final int REQUEST_MIC = 101;

    private TextView chipToday, chipThisWeek, chipFlexible;
    private MediaRecorder mediaRecorder;
    private android.media.MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private String voiceNotePath;

    private android.view.View playerControls;
    private android.widget.TextView btnStartRecording, tvVoiceTitle, tvVoiceSub;
    private android.widget.ImageView ivVoiceStatus, btnPlayAudio, btnDeleteAudio;

    private int photoCount = 0;
    private android.widget.ImageView[] photoSlots;
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(3), uris -> {
                if (uris != null && !uris.isEmpty()) {
                    photoCount = 0;
                    for (int i = 0; i < photoSlots.length; i++) {
                        if (i < uris.size()) {
                            photoSlots[i].setVisibility(android.view.View.VISIBLE);
                            photoSlots[i].setImageURI(uris.get(i));
                            photoCount++;
                        } else {
                            photoSlots[i].setVisibility(android.view.View.GONE);
                        }
                    }
                    // Hide ADD button if max reached
                    findViewById(R.id.btnAddPhoto).setVisibility(photoCount >= 3 ? android.view.View.GONE : android.view.View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig_step2);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // UI references
        btnStartRecording = findViewById(R.id.btnStartRecording);
        tvVoiceTitle = findViewById(R.id.tvVoiceTitle);
        tvVoiceSub = findViewById(R.id.tvVoiceSub);
        ivVoiceStatus = findViewById(R.id.ivVoiceStatus);
        playerControls = findViewById(R.id.playerControls);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        btnDeleteAudio = findViewById(R.id.btnDeleteAudio);

        photoSlots = new android.widget.ImageView[]{
                findViewById(R.id.ivPhoto1),
                findViewById(R.id.ivPhoto2),
                findViewById(R.id.ivPhoto3)
        };

        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
            if (photoCount < 3) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            } else {
                Toast.makeText(this, "Maximum 3 photos allowed", Toast.LENGTH_SHORT).show();
            }
        });

        // When chips
        chipToday     = findViewById(R.id.chipWhenToday);
        chipThisWeek  = findViewById(R.id.chipWhenThisWeek);
        chipFlexible  = findViewById(R.id.chipWhenFlexible);

        chipToday.setOnClickListener(v    -> selectWhen(0));
        chipThisWeek.setOnClickListener(v -> selectWhen(1));
        chipFlexible.setOnClickListener(v -> selectWhen(2));

        // ── Voice recording ──────────────────────────────────────────────────
        btnStartRecording.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC);
                }
            }
        });

        btnPlayAudio.setOnClickListener(v -> playVoiceNote());
        btnDeleteAudio.setOnClickListener(v -> deleteVoiceNote());

        // ── Location picker ──────────────────────────────────────────────────
        findViewById(R.id.cardLocation).setOnClickListener(v -> openMapPicker());

        // ── Post Gig CTA ─────────────────────────────────────────────────────
        findViewById(R.id.btnPostGig).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoadingActivity.class);
            intent.putExtra(LoadingActivity.EXTRA_TARGET_CLASS, GigSuccessActivity.class.getName());
            intent.putExtra(LoadingActivity.EXTRA_STATUS_MESSAGES, new String[] {
                    "Broadcasting your gig...",
                    "Notifying nearby helpers...",
                    "Saving your post..."
            });
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    // ── Recording helpers ────────────────────────────────────────────────────

    private void startRecording() {
        try {
            voiceNotePath = getExternalCacheDir().getAbsolutePath() + "/voice_note.3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(voiceNotePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;

            btnStartRecording.setText("Stop");
            btnStartRecording.setTextColor(getResources().getColor(R.color.brand_error_solid));
            tvVoiceTitle.setText("Recording...");
            tvVoiceSub.setText("Keep talking or tap stop when done.");
            ivVoiceStatus.setImageResource(R.drawable.ic_dot_green); // Show recording dot
        } catch (Exception e) {
            Toast.makeText(this, "Could not start recording.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception ignored) {}
        mediaRecorder = null;
        isRecording = false;

        // Transition to review state
        btnStartRecording.setVisibility(android.view.View.GONE);
        playerControls.setVisibility(android.view.View.VISIBLE);
        tvVoiceTitle.setText("Voice note saved");
        tvVoiceSub.setText("Listen back or delete to re-record.");
        ivVoiceStatus.setImageResource(R.drawable.ic_mic_blue);
    }

    private void playVoiceNote() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            return;
        }

        try {
            mediaPlayer = new android.media.MediaPlayer();
            mediaPlayer.setDataSource(voiceNotePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing voice note...", Toast.LENGTH_SHORT).show();
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Could not play audio.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteVoiceNote() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        java.io.File file = new java.io.File(voiceNotePath);
        if (file.exists()) file.delete();

        // Reset to initial state
        btnStartRecording.setVisibility(android.view.View.VISIBLE);
        btnStartRecording.setText("Start");
        btnStartRecording.setTextColor(getResources().getColor(R.color.brand_primary));
        playerControls.setVisibility(android.view.View.GONE);
        tvVoiceTitle.setText("Record details");
        tvVoiceSub.setText("Record a 20-second voice note");
        Toast.makeText(this, "Voice note deleted.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MIC
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
        } else {
            Toast.makeText(this, "Microphone permission needed to record.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            try { mediaRecorder.release(); } catch (Exception ignored) {}
        }
        if (mediaPlayer != null) {
            try { mediaPlayer.release(); } catch (Exception ignored) {}
        }
    }

    // ── Map picker ───────────────────────────────────────────────────────────

    private void openMapPicker() {
        // Opens Google Maps in pick-a-place mode; replace with Places SDK picker when API key is added.
        Uri mapUri = Uri.parse("geo:0,0?q=");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Maps not installed — fall back to any map / browser
            Intent fallback = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://maps.google.com"));
            startActivity(fallback);
        }
    }

    // ── Chip selection ───────────────────────────────────────────────────────

    private void selectWhen(int selected) {
        TextView[] chips = {chipToday, chipThisWeek, chipFlexible};
        for (int i = 0; i < chips.length; i++) {
            if (i == selected) {
                chips[i].setBackgroundResource(R.drawable.bg_chip_selected);
                chips[i].setTextColor(0xFFFFFFFF);
            } else {
                chips[i].setBackgroundResource(R.drawable.bg_chip_unselected);
                chips[i].setTextColor(0xFF64748B);
            }
        }
    }
}
