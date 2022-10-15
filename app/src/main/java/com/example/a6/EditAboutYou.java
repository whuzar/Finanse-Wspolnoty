package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditAboutYou extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    private EditText changephone, changeemail, changeshares, changenamesur;
    private Button confirmchanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about_you);

        changephone = findViewById(R.id.editphone);
        changeemail = findViewById(R.id.editemail);
        changenamesur = findViewById(R.id.editnamesur);
        changeshares = findViewById(R.id.editshares);

        confirmchanges = findViewById(R.id.bntchangeacceptinfo);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        DatabaseReference uidRef = databaseReference.child("admin").child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String surname = task.getResult().child("surname").getValue(String.class);
                        String name = task.getResult().child("name").getValue(String.class);

                        changenamesur.setText(name + " "+ surname);

                    }
                }
            }
        });

        confirmchanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cp = changephone.getText().toString();
                String ce = changeemail.getText().toString();
                String cs = changeshares.getText().toString();

                DatabaseReference textRef = databaseReference.child("admin").child(login).child("email");
                textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            String text = snapshot.getValue(String.class);
                            if(ce.isEmpty()){
                                return;
                            }else if(Objects.equals(text, ce)){
                                Toast.makeText(EditAboutYou.this, "Email jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child("admin").child(login).child("email").setValue(ce);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", task.getException().getMessage());
                        }
                    }
                });
                DatabaseReference textRef2 = databaseReference.child("admin").child(login).child("phone");
                textRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            String text = snapshot.getValue(String.class);
                            if(cp.isEmpty()){
                                return;
                            }else if(Objects.equals(text, cp)){
                                Toast.makeText(EditAboutYou.this, "Telefon jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child("admin").child(login).child("phone").setValue(cp);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", task.getException().getMessage());
                        }
                    }
                });
                DatabaseReference textRef3 = databaseReference.child("admin").child(login).child("shares");
                textRef3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            String text = snapshot.getValue(String.class);
                            if(cs.isEmpty()){
                                return;
                            }else if(Objects.equals(text, cs)){
                                Toast.makeText(EditAboutYou.this, "Udziały są takie same jak ostatnio", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child("admin").child(login).child("shares").setValue(cs);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", task.getException().getMessage());
                        }
                    }
                });
                Toast.makeText(EditAboutYou.this, "Dane zmienione pomyślnie", Toast.LENGTH_SHORT).show();
                changeemail.setText("");
                changephone.setText("");
                changeshares.setText("");
            }
        });

    }
}