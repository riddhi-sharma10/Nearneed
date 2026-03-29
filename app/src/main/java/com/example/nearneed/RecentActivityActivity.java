package com.example.nearneed;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RecentActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activity);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
