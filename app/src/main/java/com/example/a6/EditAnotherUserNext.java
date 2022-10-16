package com.example.a6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class EditAnotherUserNext extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9}$");

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN2 = "login2";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private TextInputLayout eaphone, eaemail, eashares, eanamesur;
    private Button eaconfirm;
    private TextView insertl;
    private String ep, ee, es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_another_user_next);

        eaphone = findViewById(R.id.editphoneuser);
        eaemail = findViewById(R.id.editemailuser);
        eashares = findViewById(R.id.editsharesuser);
        eanamesur = findViewById(R.id.editnamesurn);

        eaconfirm = findViewById(R.id.eabntchangeacceptinfo);

        insertl = findViewById(R.id.insertlogin);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN2, null);

        insertl.setText(login);

        DatabaseReference uidRef = databaseReference.child("user").child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String surname = task.getResult().child("surname").getValue(String.class);
                        String name = task.getResult().child("name").getValue(String.class);

                        eanamesur.getEditText().setText(name + " "+ surname);

                    }
                }
            }
        });

        eaconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ep = eaphone.getEditText().getText().toString();
                ee = eaemail.getEditText().getText().toString();
                es = eashares.getEditText().getText().toString();


                if(!validateEmail() || !validatePhone() || !validateShares()){
                    return;
                }
                else {

                    DatabaseReference textRef = databaseReference.child("user").child(login).child("email");
                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(ee.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, ee)){
                                    Toast.makeText(EditAnotherUserNext.this, "Email jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child("user").child(login).child("email").setValue(ee);
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
                    DatabaseReference textRef2 = databaseReference.child("user").child(login).child("phone");
                    textRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(ep.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, ep)){
                                    Toast.makeText(EditAnotherUserNext.this, "Telefon jest taki sam jak poprzedni", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child("user").child(login).child("phone").setValue(ep);
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
                    DatabaseReference textRef3 = databaseReference.child("user").child(login).child("shares");
                    textRef3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                String text = snapshot.getValue(String.class);
                                if(es.isEmpty()){
                                    return;
                                }else if(Objects.equals(text, es)){
                                    Toast.makeText(EditAnotherUserNext.this, "Udziały są takie same jak ostatnio", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            databaseReference.child("user").child(login).child("shares").setValue(es);
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
                    Toast.makeText(EditAnotherUserNext.this, "Dane zmienione pomyślnie", Toast.LENGTH_SHORT).show();
                    eaphone.getEditText().setText("");
                    eaemail.getEditText().setText("");
                    eashares.getEditText().setText("");


                }

            }

        });
    }
    private boolean validateEmail() {
        String emailInput = eaemail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
//            email.setError("Pole jest puste");
            eaemail.setError("Pole nie może być puste");
//            Toast.makeText(Register.this, "Pole e-mail jest puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            eaemail.setError("Proszę wpisać poprawnie adres e-mail");
//            Toast.makeText(Register.this, "Proszę wpisać poprawnie adres e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            eaemail.setError(null);
            return true;
        }
    }



    private boolean validatePhone() {
        String phoneInput = eaphone.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            eaphone.setError("Pole numer telefonu nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            eaphone.setError("Proszę wpisać poprawnie numer telefonu");
            Toast.makeText(EditAnotherUserNext.this, "Proszę wpisać poprawnie numer telefonu", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            eaphone.setError(null);
            return true;
        }
    }


    private boolean validateShares() {
        if (es.isEmpty()) {
            eashares.setError("Pole udziały nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            eashares.setError(null);
            return true;
        }
    }
}