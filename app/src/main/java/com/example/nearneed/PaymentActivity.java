package com.example.nearneed;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout optionUpi, optionCash, optionQr, upiContent;
    private ImageView radioUpi, radioCash, radioQr;
    private MaterialButton btnConfirmPayment;

    private static final int SELECTED_UPI  = 0;
    private static final int SELECTED_CASH = 1;
    private static final int SELECTED_QR   = 2;

    private int selectedOption = SELECTED_UPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        optionUpi  = findViewById(R.id.optionUpi);
        optionCash = findViewById(R.id.optionCash);
        optionQr   = findViewById(R.id.optionQr);
        upiContent = findViewById(R.id.upiContent);

        radioUpi  = findViewById(R.id.radioUpi);
        radioCash = findViewById(R.id.radioCash);
        radioQr   = findViewById(R.id.radioQr);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        updateSelection(SELECTED_UPI);

        optionUpi.setOnClickListener(v  -> updateSelection(SELECTED_UPI));
        optionCash.setOnClickListener(v -> updateSelection(SELECTED_CASH));
        optionQr.setOnClickListener(v   -> updateSelection(SELECTED_QR));

        btnConfirmPayment.setOnClickListener(v -> {
            String method = selectedOption == SELECTED_UPI  ? "UPI" :
                            selectedOption == SELECTED_CASH ? "Pay in Person" : "QR Code";
            Toast.makeText(this, "Order confirmed with " + method, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateSelection(int option) {
        selectedOption = option;

        optionUpi.setBackgroundResource(R.drawable.bg_payment_unselected);
        optionCash.setBackgroundResource(R.drawable.bg_payment_unselected);
        optionQr.setBackgroundResource(R.drawable.bg_payment_unselected);

        radioUpi.setImageResource(R.drawable.ic_unselected_grey);
        radioCash.setImageResource(R.drawable.ic_unselected_grey);
        radioQr.setImageResource(R.drawable.ic_unselected_grey);
        
        upiContent.setVisibility(View.GONE);

        switch (option) {
            case SELECTED_UPI:
                optionUpi.setBackgroundResource(R.drawable.bg_payment_selected);
                radioUpi.setImageResource(R.drawable.ic_checked_blue);
                upiContent.setVisibility(View.VISIBLE);
                break;
            case SELECTED_CASH:
                optionCash.setBackgroundResource(R.drawable.bg_payment_selected);
                radioCash.setImageResource(R.drawable.ic_checked_blue);
                break;
            case SELECTED_QR:
                optionQr.setBackgroundResource(R.drawable.bg_payment_selected);
                radioQr.setImageResource(R.drawable.ic_checked_blue);
                break;
        }
    }
}
