package com.example.a6;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9}$");



    private TextInputLayout email;
    private TextInputLayout log;
    private TextInputLayout password;
    private TextInputLayout conpassword;
    private TextInputLayout phon;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        log = findViewById(R.id.logi);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);
        phon = findViewById(R.id.phone);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView login = findViewById(R.id.login);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String emailtxt = email.getEditText().getText().toString();
                final String logtxt = log.getEditText().getText().toString();
                final String passwordtxt = password.getEditText().getText().toString();
                final String phontxt = phon.getEditText().getText().toString();

                if(!validateEmail() || !validatePhone() || !validateUsername() || !validatePassword()){
                    return;
                }
                else {
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(logtxt)){
                                Toast.makeText(Register.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                            }else{
                                databaseReference.child("admin").child(logtxt).child("email").setValue(emailtxt);
                                databaseReference.child("admin").child(logtxt).child("password").setValue(passwordtxt);
                                databaseReference.child("admin").child(logtxt).child("phone").setValue(phontxt);
                                databaseReference.child("admin").child(logtxt).child("email").setValue(emailtxt);
                                // przy generowaniu loginu ma nie byc admina chyba ze generuje dla kolejnych zarzadcow

                                Toast.makeText(Register.this, "Utworzono użytkonika", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
//            email.setError("Pole jest puste");
            email.setError("Pole nie może być puste");
//            Toast.makeText(Register.this, "Pole e-mail jest puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Proszę wpisać poprawnie adres e-mail");
//            Toast.makeText(Register.this, "Proszę wpisać poprawnie adres e-mail", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = password.getEditText().getText().toString().trim();
        String conpasswordInput = conpassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Pole hasła nie może być puste");
//            Toast.makeText(Register.this, "Pole hasła nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Hasło jest za słabe");
//            Toast.makeText(Register.this, "Hasło jest za słabe", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!passwordInput.equals(conpasswordInput)){
            password.setError("Hasła nie pasują do siebie");
//            Toast.makeText(Register.this, "Hasła nie pasują do siebie", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = log.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            log.setError("Pole login nie może być puste");
//            Toast.makeText(Register.this, "Pole login nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameInput.length() > 20) {
            log.setError("Login jest za długi");
//            Toast.makeText(Register.this, "Login jest za długi", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            log.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = phon.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phon.setError("Pole numer telefonu nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            phon.setError("Proszę wpisać poprawnie numer telefonu");
            Toast.makeText(Register.this, "Proszę wpisać poprawnie numer telefonu", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            phon.setError(null);
            return true;
        }
    }


}