package com.example.nearneed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.io.File;

public class CreateCommunityPostStep2Activity extends AppCompatActivity {

    private static final int REQUEST_MIC = 101;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private String voiceNotePath;

    private View playerControls;
    private TextView btnStartRecording, tvVoiceTitle, tvVoiceSub;
    private ImageView ivVoiceStatus, btnPlayAudio, btnDeleteAudio;
    private ImageView[] photoSlots;
    private int photoCount = 0;

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
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community_post_step2);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnStartRecording = findViewById(R.id.btnStartRecording);
        tvVoiceTitle = findViewById(R.id.tvVoiceTitle);
        tvVoiceSub = findViewById(R.id.tvVoiceSub);
        ivVoiceStatus = findViewById(R.id.ivVoiceStatus);
        playerControls = findViewById(R.id.playerControls);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        btnDeleteAudio = findViewById(R.id.btnDeleteAudio);

        photoSlots = new ImageView[]{
                findViewById(R.id.ivPhoto1),
                findViewById(R.id.ivPhoto2),
                findViewById(R.id.ivPhoto3)
        };
    }

    private void setupListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
            if (photoCount < 3) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            } else {
                Toast.makeText(this, "Maximum 3 photos allowed", Toast.LENGTH_SHORT).show();
            }
        });

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

        findViewById(R.id.cardLocation).setOnClickListener(v -> openMapPicker());

        findViewById(R.id.btnPostNow).setOnClickListener(v -> navigateToSuccess());

        // Chips logic
        TextView chipToday = findViewById(R.id.chipWhenToday);
        TextView chipWeek = findViewById(R.id.chipWhenThisWeek);
        TextView chipFlexible = findViewById(R.id.chipWhenFlexible);
        TextView[] chips = {chipToday, chipWeek, chipFlexible};

        for (int i = 0; i < chips.length; i++) {
            final int index = i;
            if (chips[i] != null) {
                chips[i].setOnClickListener(v -> {
                    for (int j = 0; j < chips.length; j++) {
                        chips[j].setBackgroundResource(R.drawable.bg_chip_unselected);
                        chips[j].setTextColor(getResources().getColor(R.color.text_subheadline));
                    }
                    chips[index].setBackgroundResource(R.drawable.bg_chip_selected_red);
                    chips[index].setTextColor(android.graphics.Color.WHITE);
                });
            }
        }
    }

    private void startRecording() {
        try {
            voiceNotePath = getExternalCacheDir().getAbsolutePath() + "/community_voice_note.3gp";
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
            tvVoiceSub.setText("Recording your message...");
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

        btnStartRecording.setVisibility(View.GONE);
        playerControls.setVisibility(View.VISIBLE);
        tvVoiceTitle.setText("Voice note saved");
        tvVoiceSub.setText("Listen back or delete to re-record.");
    }

    private void playVoiceNote() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            return;
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(voiceNotePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing your voice note...", Toast.LENGTH_SHORT).show();
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

        File file = new File(voiceNotePath);
        if (file.exists()) file.delete();

        btnStartRecording.setVisibility(View.VISIBLE);
        btnStartRecording.setText("Start");
        btnStartRecording.setTextColor(getResources().getColor(R.color.brand_error_solid));
        playerControls.setVisibility(View.GONE);
        tvVoiceTitle.setText("Record details");
        tvVoiceSub.setText("Record a voice note for better clarity");
    }

    private void openMapPicker() {
        Uri mapUri = Uri.parse("geo:0,0?q=");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com")));
        }
    }

    private void navigateToSuccess() {
        // Save post data for dashboard display
        android.content.SharedPreferences prefs = getSharedPreferences("NearNeedPosts", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LATEST_POST_TITLE", getIntent().getStringExtra("POST_TITLE"));
        editor.putString("LATEST_POST_DESC", getIntent().getStringExtra("POST_DESC"));
        editor.putString("LATEST_POST_CATEGORY", getIntent().getStringExtra("POST_CATEGORY"));
        editor.apply();

        Intent intent = new Intent(this, CommunityPostSuccessActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MIC && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
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
}
