package com.example.nearneed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
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
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig_step2);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // When chips — use Material ChipGroup; keep legacy manual selection as fallback
        chipToday     = findViewById(R.id.chipWhenToday);
        chipThisWeek  = findViewById(R.id.chipWhenThisWeek);
        chipFlexible  = findViewById(R.id.chipWhenFlexible);

        chipToday.setOnClickListener(v    -> selectWhen(0));
        chipThisWeek.setOnClickListener(v -> selectWhen(1));
        chipFlexible.setOnClickListener(v -> selectWhen(2));

        // ── Voice recording ──────────────────────────────────────────────────
        TextView btnRecord = findViewById(R.id.btnStartRecording);
        btnRecord.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording(btnRecord);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    startRecording(btnRecord);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC);
                }
            }
        });

        // ── Location picker ──────────────────────────────────────────────────
        // Tapping the location card launches Google Maps "place picker" intent.
        // Falls back gracefully if Maps is not installed.
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

    private void startRecording(TextView btn) {
        try {
            String outputPath = getExternalCacheDir().getAbsolutePath() + "/voice_note.3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputPath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            btn.setText("⏹  Stop");
            btn.setTextColor(0xFFEF4444);
            Toast.makeText(this, "Recording…", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Could not start recording.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording(TextView btn) {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception ignored) {}
        mediaRecorder = null;
        isRecording = false;
        btn.setText("🎙  Voice note saved");
        btn.setTextColor(0xFF1D5EF3);
        Toast.makeText(this, "Voice note saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MIC
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TextView btn = findViewById(R.id.btnStartRecording);
            startRecording(btn);
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
