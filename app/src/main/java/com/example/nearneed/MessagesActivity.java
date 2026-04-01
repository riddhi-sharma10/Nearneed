package com.example.nearneed;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // ── Bottom Navigation ────────────────────────────────────────────────
        NavbarHelper.setup(this, NavbarHelper.TAB_MESSAGES);

        // ── Conversation click listeners ─────────────────────────────────────
        if (findViewById(R.id.convRachel) != null)
            findViewById(R.id.convRachel).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Rachel...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convRahul) != null)
            findViewById(R.id.convRahul).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Rahul Singh...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convManya) != null)
            findViewById(R.id.convManya).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Manya Awasthi...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convVishu) != null)
            findViewById(R.id.convVishu).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Vishu Singh...", Toast.LENGTH_SHORT).show());

        if (findViewById(R.id.convRiddhi) != null)
            findViewById(R.id.convRiddhi).setOnClickListener(v ->
                Toast.makeText(this, "Opening chat with Riddhi Sharma...", Toast.LENGTH_SHORT).show());
    }
}
