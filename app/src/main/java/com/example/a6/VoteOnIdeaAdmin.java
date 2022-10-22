package com.example.a6;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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

public class VoteOnIdeaAdmin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private TextView dateTextd, dateTextm, dateTexty;
    private EditText valuefor, votetheme;
    private Button btndate, sendvote;
    private RadioButton valuer1, valuer2;
    String team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_idea_admin);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        dateTextd = findViewById(R.id.showdateday);
        dateTextm = findViewById(R.id.showdatemonth);
        dateTexty = findViewById(R.id.showdateyear);
        btndate = findViewById(R.id.choosedate);
        valuefor = findViewById(R.id.showedit);
        valuer2 = findViewById(R.id.radiochoice2value);
        valuer1 = findViewById(R.id.radiochoice1value);
        sendvote = findViewById(R.id.sendtoonevote);
        votetheme = findViewById(R.id.themevote);


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

        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);

        sendvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String vt = votetheme.getText().toString();
                String vf = valuefor.getText().toString();
                String dy = dateTexty.getText().toString();
                String dm = dateTextm.getText().toString();
                String dd = dateTextd.getText().toString();

                if(!vt.equals("") && !dy.equals("")) {
                    endtypeidea(who, login);
                    endchoose(who, login);
                    clearfinish(who, login);
                    DatabaseReference uidRef = databaseReference.child(who).child(login);
                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String teamwspo = task.getResult().child("team").getValue(String.class);
//                                    String send = task.getResult().child("ideas").child("send").getValue(String.class);
//                                    databaseReference.child(who).child(login).child("ideas").orderByChild("send").equalTo(send);

                                    DatabaseReference textRef = databaseReference;
                                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();

                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("themevote").setValue(vt);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("sendby").setValue(login);

                                                        databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("day").setValue(dd);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("year").setValue(dy);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("month").setValue(dm);
                                                        if (!vf.isEmpty()) {
                                                            databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("money").setValue(vf);
                                                        } else {
                                                            databaseReference.child("wspolnota").child(teamwspo).child("typeidea").child("money").setValue("0");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                databaseReference.child(who).child(login).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.e("firebase", "Error getting data", task.getException());
                                                        } else {
                                                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                                            team = String.valueOf(task.getResult().getValue());
                                                            databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                                                        if(String.valueOf(ds.child("team").getValue()).equals(team)){
                                                                            String ideas = String.valueOf(ds.child("ideas").getValue());
                                                                            if(ideas != null){
//                                                                                ds.child("idea").getRef().removeValue();
                                                                                ds.child("send").getRef().removeValue();
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
                                                                        if(String.valueOf(ds.child("team").getValue()).equals(team)){
                                                                            String ideas = String.valueOf(ds.child("ideas").getValue());
                                                                            if(ideas != null){
//                                                                                ds.child("idea").getRef().removeValue();
                                                                                ds.child("send").getRef().removeValue();
                                                                            }
                                                                        }


                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                            databaseReference.child("wspolnota").child(teamwspo).child("userIdeas").getRef().removeValue();
                                                        }

                                                    }
                                                });



//                                                Log.i("users", String.valueOf(databaseReference.child("user").orderByChild("team").equalTo(team).get()));


                                                Toast.makeText(VoteOnIdeaAdmin.this, "Utworzono głosowanie", Toast.LENGTH_SHORT).show();
                                                valuefor.setText("");
                                                votetheme.setText("");
                                                dateTexty.setText("");
                                                dateTextm.setText("");
                                                dateTextd.setText("");
                                            } else {
                                                Log.d("TAG", task.getException().getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(VoteOnIdeaAdmin.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
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
        String m = String.valueOf(month+1);
        String d = String.valueOf(dayOfMonth);
        dateTexty.setText(y);
        dateTextm.setText(m);
        dateTextd.setText(d);
    }
    public void endtypeidea(String who, String login){
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String team = task.getResult().child(who).child(login).child("team").getValue(String.class);
                        DatabaseReference uid = databaseReference.child("wspolnota").child(team).child("typeidea");
                        uid.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot remov: dataSnapshot.getChildren()) {
                                    remov.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }
                }
            }
        });
    }
    public void endchoose(String who, String login){
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String team = task.getResult().child(who).child(login).child("team").getValue(String.class);
                        DatabaseReference uid = databaseReference.child("wspolnota").child(team).child("showidea");
                        uid.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot remov: dataSnapshot.getChildren()) {
                                    remov.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                        DatabaseReference uid2 = databaseReference.child("wspolnota").child(team).child("createdpoll");
                        uid2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot remov2: dataSnapshot.getChildren()) {
                                    remov2.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }
                }
            }
        });
    }
    public void clearfinish(String who, String login){
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String team = task.getResult().child(who).child(login).child("team").getValue(String.class);
                        DatabaseReference textRef = databaseReference.child(who).child(login).child("password");
                        textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot snapshot = task.getResult();
                                    String text = snapshot.getValue(String.class);
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child("wspolnota").child(team).child("finish").child("check").setValue("false");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}