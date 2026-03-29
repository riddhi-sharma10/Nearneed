package com.example.nearneed;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SavedTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_tasks);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
