package com.example.nearneed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;

import androidx.appcompat.app.AppCompatActivity;

public class CreateGigActivity extends AppCompatActivity {

    private int counterValue = 500;
    private int peopleCount  = 1;

    private View     cardMaxCounter;
    private TextView btnCounterMinus;
    private TextView btnCounterPlus;
    private TextView tvCounterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gig);

        // Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // --- Negotiation toggle ---
        com.google.android.material.materialswitch.MaterialSwitch switchNegotiation = findViewById(R.id.switchNegotiation);
        cardMaxCounter   = findViewById(R.id.cardMaxCounter);
        btnCounterMinus  = findViewById(R.id.btnCounterMinus);
        btnCounterPlus   = findViewById(R.id.btnCounterPlus);
        tvCounterValue   = findViewById(R.id.tvCounterValue);

        // Start enabled (switch is ON in XML)
        setCounterEnabled(true);

        switchNegotiation.setOnCheckedChangeListener((buttonView, isChecked) ->
            setCounterEnabled(isChecked)
        );

        // Counter stepper — only works when enabled (guarded inside setCounterEnabled)
        btnCounterMinus.setOnClickListener(v -> {
            if (counterValue >= 50) { counterValue -= 50; tvCounterValue.setText(String.valueOf(counterValue)); }
        });
        btnCounterPlus.setOnClickListener(v -> {
            counterValue += 50; tvCounterValue.setText(String.valueOf(counterValue));
        });

        // People stepper
        TextView tvPeople = findViewById(R.id.tvPeopleCount);
        findViewById(R.id.btnPeopleMinus).setOnClickListener(v -> {
            if (peopleCount > 1) { peopleCount--; tvPeople.setText(String.valueOf(peopleCount)); }
        });
        findViewById(R.id.btnPeoplePlus).setOnClickListener(v -> {
            peopleCount++; tvPeople.setText(String.valueOf(peopleCount));
        });

        // Payment method selection
        View payUPI  = findViewById(R.id.payUPI);
        View payCash = findViewById(R.id.payCash);
        View payBoth = findViewById(R.id.payBoth);
        payUPI.setOnClickListener(v  -> selectPayment(payUPI,  payCash, payBoth));
        payCash.setOnClickListener(v -> selectPayment(payCash, payUPI,  payBoth));
        payBoth.setOnClickListener(v -> selectPayment(payBoth, payUPI,  payCash));

        // Continue
        findViewById(R.id.btnContinueGig).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateGigStep2Activity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        });
    }

    /**
     * Visually enables/disables the Max Counter Offer card.
     *  - OFF → dimmed (alpha 0.4), buttons not clickable
     *  - ON  → fully visible (alpha 1.0), buttons interactive
     */
    private void setCounterEnabled(boolean enabled) {
        cardMaxCounter.animate()
                .alpha(enabled ? 1.0f : 0.4f)
                .setDuration(200)
                .start();

        btnCounterMinus.setClickable(enabled);
        btnCounterMinus.setFocusable(enabled);
        btnCounterPlus.setClickable(enabled);
        btnCounterPlus.setFocusable(enabled);

        // Update button visual state
        if (enabled) {
            btnCounterPlus.setBackgroundResource(R.drawable.bg_counter_btn);
            btnCounterPlus.setTextColor(0xFFFFFFFF);
            btnCounterMinus.setBackgroundResource(R.drawable.bg_card_outline);
            btnCounterMinus.setTextColor(0xFF64748B);
        } else {
            btnCounterPlus.setBackgroundResource(R.drawable.bg_card_outline);
            btnCounterPlus.setTextColor(0xFF94A3B8);
            btnCounterMinus.setBackgroundResource(R.drawable.bg_card_outline);
            btnCounterMinus.setTextColor(0xFF94A3B8);
        }
    }

    private void selectPayment(View selected, View... others) {
        selected.setBackgroundResource(R.drawable.bg_payment_selected);
        for (View v : others) {
            v.setBackgroundResource(R.drawable.bg_payment_unselected);
        }
    }
}
