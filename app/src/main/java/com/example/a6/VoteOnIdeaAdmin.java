package com.example.a6;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class VoteOnIdeaAdmin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView dateText;
    private EditText valuefor, votetheme;
    private Button btndate, sendvote;
    private RadioButton vt1, vt2, valuer1, valuer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_idea_admin);

        dateText = findViewById(R.id.cos);
        btndate = findViewById(R.id.choosedate);
        valuefor = findViewById(R.id.showedit);
        valuer2 = findViewById(R.id.radiochoice2value);
        valuer1 = findViewById(R.id.radiochoice1value);
        sendvote = findViewById(R.id.senddatavote);
        votetheme = findViewById(R.id.themevote);

        final String themevt = votetheme.getText().toString();
        System.out.println(themevt);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        valuer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    valuefor.setVisibility(View.VISIBLE);
                }
            }
        });

        valuer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    valuefor.setVisibility(View.GONE);
                }
            }
        });

        sendvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VoteOnIdeaAdmin.this, themevt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "Wybrana data " + dayOfMonth + "-" + month + "-" + year;
        dateText.setText(date);
    }
}