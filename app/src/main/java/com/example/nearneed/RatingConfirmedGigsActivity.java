package com.example.nearneed;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class RatingConfirmedGigsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_confirmed_gigs);

        ImageView ivClose = findViewById(R.id.iv_close);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> finish());
        }

        AppCompatButton btnBackFeed = findViewById(R.id.btn_back_feed);
        if (btnBackFeed != null) {
            btnBackFeed.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, MainActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}
