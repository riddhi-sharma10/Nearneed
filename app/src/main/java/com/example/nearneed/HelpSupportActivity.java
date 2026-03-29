package com.example.nearneed;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HelpSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Setup FAQ dropdowns
        setupFaq(1);
        setupFaq(2);
        setupFaq(3);
        setupFaq(4);
        setupFaq(5);
        setupFaq(6);

        findViewById(R.id.btnContactUs).setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_contact_us, null);
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialogView.findViewById(R.id.btnCancelContact).setOnClickListener(dView -> dialog.dismiss());
            dialogView.findViewById(R.id.btnChatSupport).setOnClickListener(dView -> {
                dialog.dismiss();
                Toast.makeText(this, "Starting chat...", Toast.LENGTH_SHORT).show();
            });
            dialogView.findViewById(R.id.btnCallAgent).setOnClickListener(dView -> {
                dialog.dismiss();
                Toast.makeText(this, "Calling an agent...", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });
    }

    private void setupFaq(int id) {
        int headerId = getResources().getIdentifier("faq_header_" + id, "id", getPackageName());
        int answerId = getResources().getIdentifier("faq_answer_" + id, "id", getPackageName());
        int arrowId = getResources().getIdentifier("faq_arrow_" + id, "id", getPackageName());

        View header = findViewById(headerId);
        TextView answer = findViewById(answerId);
        ImageView arrow = findViewById(arrowId);

        header.setOnClickListener(v -> {
            if (answer.getVisibility() == View.VISIBLE) {
                answer.setVisibility(View.GONE);
                arrow.animate().rotation(0).setDuration(200).start();
            } else {
                answer.setVisibility(View.VISIBLE);
                arrow.animate().rotation(180).setDuration(200).start();
            }
        });
    }
}
