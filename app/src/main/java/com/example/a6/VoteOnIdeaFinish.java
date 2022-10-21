package com.example.a6;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class VoteOnIdeaFinish extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private EditText type;
    private Button btn, btncreate, btndate;
    private TextView dateTextd, dateTextm, dateTexty;
    private RadioButton valuer1, valuer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_idea_finish);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        dateTextd = findViewById(R.id.showdatedayf);
        dateTextm = findViewById(R.id.showdatemonthf);
        dateTexty = findViewById(R.id.showdateyearf);
        type = findViewById(R.id.ideainsert);
        btn = findViewById(R.id.addidea);
        btncreate = findViewById(R.id.create);
        btndate = findViewById(R.id.choosedatef);
        valuer2 = findViewById(R.id.radiochoice2valuef);
        valuer1 = findViewById(R.id.radiochoice1valuef);

        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ii = type.getText().toString();

                if(!ii.equals("")) {
                    DatabaseReference uidRef = databaseReference.child("admin").child(login);
                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String teamwspo = task.getResult().child("team").getValue(String.class);

                                    DatabaseReference textRef = databaseReference.child("wspolnota").child(teamwspo);
                                    textRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // get total available quest

//                                            Log.i("size after", String.valueOf(size));
//                                                       databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("count").setValue(String.valueOf(x));
//                                                        databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("idea").setValue(idea + "&" + ii);
                                            textRef.child("createdpoll").child(ii).child("idea").setValue(ii);
                                            Toast.makeText(VoteOnIdeaFinish.this, "Dodano", Toast.LENGTH_SHORT).show();
                                            type.setText("");
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
//
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(VoteOnIdeaFinish.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dy = dateTexty.getText().toString();
                String dm = dateTextm.getText().toString();
                String dd = dateTextd.getText().toString();

                if(!dy.equals("") && (!valuer1.isChecked() || !valuer2.isChecked())) {
                    DatabaseReference uidRef = databaseReference.child("admin").child(login);
                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String teamwspo = task.getResult().child("team").getValue(String.class);

                                    DatabaseReference textRef = databaseReference.child("wspolnota").child(teamwspo);
                                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();

                                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        databaseReference.child("wspolnota").child(teamwspo).child("showidea").child("day").setValue(dd);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("showidea").child("year").setValue(dy);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("showidea").child("month").setValue(dm);

                                                        if(valuer2.isChecked()){
                                                            databaseReference.child("wspolnota").child(teamwspo).child("showidea").child("power").setValue("shares");
                                                        }else if(valuer1.isChecked()){
                                                            databaseReference.child("wspolnota").child(teamwspo).child("showidea").child("power").setValue("one");
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Toast.makeText(VoteOnIdeaFinish.this, "Utworzono głosowanie", Toast.LENGTH_SHORT).show();
                                                dateTexty.setText("");
                                                dateTextm.setText("");
                                                dateTextd.setText("");
                                            } else {
                                                Log.d("TAG", task.getException().getMessage());
                                            }
                                        }
                                    });
                                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                if(String.valueOf(ds.child("team").getValue()).equals(teamwspo)){
                                                    String ideas = String.valueOf(ds.child("voted").getValue());
                                                    if(ideas != null){
                                                        ds.child("voted").getRef().removeValue();
                                                    }
                                                }


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                if(String.valueOf(ds.child("team").getValue()).equals(teamwspo)){
                                                    String ideas = String.valueOf(ds.child("voted").getValue());
                                                    if(ideas != null){
                                                        ds.child("voted").getRef().removeValue();
                                                    }
                                                }


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(VoteOnIdeaFinish.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                }

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        String d = String.valueOf(dayOfMonth);
        dateTexty.setText(y);
        dateTextm.setText(m);
        dateTextd.setText(d);
    }
}