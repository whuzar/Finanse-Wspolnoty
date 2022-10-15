package com.example.a6;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class VoteOnIdeaAdmin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private TextView dateText;
    private EditText valuefor, votetheme, sugger;
    private Button btndate, sendvote;
    private RadioButton valuer1, valuer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_idea_admin);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        dateText = findViewById(R.id.cos);
        btndate = findViewById(R.id.choosedate);
        valuefor = findViewById(R.id.showedit);
        valuer2 = findViewById(R.id.radiochoice2value);
        valuer1 = findViewById(R.id.radiochoice1value);
        sendvote = findViewById(R.id.sendtoonevote);
        votetheme = findViewById(R.id.themevote);
        sugger = findViewById(R.id.suggest);

        final String themevt = votetheme.getText().toString();
        System.out.println(themevt);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

//        int kwota = 0;

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

        String login = sharedPreferences.getString(KEY_LOGIN, null);

        sendvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vt = votetheme.getText().toString();
                String sg = sugger.getText().toString();
                String bd = btndate.getText().toString();
                Toast.makeText(VoteOnIdeaAdmin.this, bd, Toast.LENGTH_SHORT).show();

                DatabaseReference uidRef = databaseReference.child("admin").child(login);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String teamwspo = task.getResult().child("team").getValue(String.class);

//                                DatabaseReference textRef = databaseReference.child("wspolnota").child(teamwspo);
//                                textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            DataSnapshot snapshot = task.getResult();
//
//                                            databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    databaseReference.child("wspolnota").child(teamwspo).child("notice").child("thememessage").setValue(tm);
//                                                    databaseReference.child("wspolnota").child(teamwspo).child("notice").child("message").setValue(ma);
//                                                    databaseReference.child("wspolnota").child(teamwspo).child("notice").child("sendby").setValue(login);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            Toast.makeText(VoteOnIdeaAdmin.this, "Utworzono głosowanie", Toast.LENGTH_SHORT).show();
//                                            theme.setText("");
//                                            message.setText("");
//                                        } else {
//                                            Log.d("TAG", task.getException().getMessage());
//                                        }
//                                    }
//                                });;
                            }
                        }
                    }
                });

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
        String date = dayOfMonth + "-" + month + "-" + year;
        dateText.setText(date);
        metoda(date);
    }
    public void metoda(String date){

        return;
    }
}