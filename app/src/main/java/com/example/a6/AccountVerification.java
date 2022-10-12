package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AccountVerification extends AppCompatActivity {
    private EditText codeInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        codeInput = findViewById(R.id.code);
        final Button weryfikuj = findViewById(R.id.weryfikuj);

        weryfikuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userCode = codeInput.getText().toString();

            }
        });
    }
}