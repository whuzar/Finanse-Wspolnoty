package com.example.a6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class EditAboutYou extends AppCompatActivity {
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9}$");
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private TextInputLayout changephone, changeemail, changeshares, changenamesur;
    private Button confirmchanges;
    private String cp, ce, cs;

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
        String who = sharedPreferences.getString(KEY_LOGED, null);
        DatabaseReference uidRef = databaseReference.child(who).child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String surname = task.getResult().child("surname").getValue(String.class);
                        String name = task.getResult().child("name").getValue(String.class);

                        changenamesur.getEditText().setText(name + " "+ surname);

                    }
                }
            }
        });

        confirmchanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cp = changephone.getEditText().getText().toString();
                ce = changeemail.getEditText().getText().toString();
                cs = changeshares.getEditText().getText().toString();

                if(!validateEmail() || !validatePhone()){
                    return;
                }
                else {
                    DatabaseReference textRef = databaseReference.child(who).child(login).child("email");
                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(ce.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, ce)){
                                    changeemail.setError("Email jest taki sam jak poprzedni");
//                                    Toast.makeText(EditAboutYou.this, "Email jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child(who).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child(who).child(login).child("email").setValue(ce);
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
                    DatabaseReference textRef2 = databaseReference.child(who).child(login).child("phone");
                    textRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(cp.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, cp)){
                                    changephone.setError("Telefon jest taki sam jak poprzedni");
//                                    Toast.makeText(EditAboutYou.this, "Telefon jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child(who).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child(who).child(login).child("phone").setValue(cp);
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
                    DatabaseReference textRef3 = databaseReference.child(who).child(login).child("shares");
                    textRef3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(cs.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, cs)){
                                    changeshares.setError("Udziały są takie same jak ostatnio");
//                                    Toast.makeText(EditAboutYou.this, "Udziały są takie same jak ostatnio", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child(who).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child(who).child(login).child("shares").setValue(cs);
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
                    changeemail.getEditText().setText("");
                    changephone.getEditText().setText("");
                    changeshares.getEditText().setText("");
                    changeemail.setError(null);
                    changephone.setError(null);
                    changeshares.setError(null);

                }



            }
        });

    }
    private boolean validateEmail() {
        String emailInput = changeemail.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            return true;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            changeemail.setError("Proszę wpisać poprawnie adres e-mail");
//            Toast.makeText(Register.this, "Proszę wpisać poprawnie adres e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            changeemail.setError(null);
            return true;
        }
    }



    private boolean validatePhone() {
        String phoneInput = changephone.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
//            changephone.setError("Pole numer telefonu nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return true;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            changephone.setError("Proszę wpisać poprawnie numer telefonu");
            Toast.makeText(EditAboutYou.this, "Proszę wpisać poprawnie numer telefonu", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            changephone.setError(null);
            return true;
        }
    }



}