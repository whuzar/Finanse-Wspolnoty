package com.example.a6;

import android.content.Intent;
import android.net.Uri;
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

import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
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

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zĘÓĄŚŁŻŹĆŃęóąśłżźć]+");


    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout conpassword;
    private TextInputLayout phon;
    private TextInputLayout nameInput;
    private TextInputLayout surnameInput;
    private TextInputLayout sharesInput;
    private String emailtxt;
    private String logtxt;
    private String passwordtxt;
    private String phontxt;
    private String code;
    private String name;
    private String surname;
    private String shares;
    private boolean noExistUser = false, noExistAdmin = false;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        conpassword = findViewById(R.id.conpassword);
        phon = findViewById(R.id.phone);
        nameInput = findViewById(R.id.name);
        surnameInput = findViewById(R.id.surname);
        sharesInput = findViewById(R.id.shares);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView login = findViewById(R.id.login);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                emailtxt = email.getEditText().getText().toString().trim();
                passwordtxt = password.getEditText().getText().toString().trim();
                phontxt = phon.getEditText().getText().toString().trim();
                name = nameInput.getEditText().getText().toString().trim();
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                surname = surnameInput.getEditText().getText().toString().trim();
                surname = surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase();
                shares = sharesInput.getEditText().getText().toString().trim();
                code = generateCode();
               generateUsername();

                if(!validateEmail() || !validatePhone() || !validatePassword() || !validateName(name, nameInput) || !validateName(surname, surnameInput) || !validateShares()){
                    return;
                }
                else {

                    addUser();


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

    public void passData(){
        Intent passdata_intent = new Intent(this, AccountVerification.class);
        passdata_intent.putExtra("email", emailtxt);
        passdata_intent.putExtra("login", logtxt);
        passdata_intent.putExtra("password", passwordtxt);
        passdata_intent.putExtra("phone", phontxt);
        passdata_intent.putExtra("code", code);
        passdata_intent.putExtra("name", name);
        passdata_intent.putExtra("surname", surname);
        passdata_intent.putExtra("shares", shares);
        startActivity(passdata_intent);
    }

    private String generateCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
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

    private boolean validateName(String string, TextInputLayout input) {
        if (string.isEmpty()) {
            input.setError("Pole imie nie może być puste");
//            Toast.makeText(Register.this, "Pole login nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (string.length() > 30) {
            input.setError("imie jest za długi");
//            Toast.makeText(Register.this, "Login jest za długi", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!NAME_PATTERN.matcher(string).matches()) {
            input.setError("Proszę wpisać poprawną wartość");
            return false;
        }else{
            input.setError(null);
            return true;
        }
    }

    private boolean validateShares() {
        if (shares.isEmpty()) {
            sharesInput.setError("Pole udziały nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            sharesInput.setError(null);
            return true;
        }
    }
    private void generateUsername(){
        Random rand = new Random();
        int n = rand.nextInt(1000);
        logtxt = name.substring(0, 3) + surname.substring(0, 3) + n;
        logtxt = logtxt.toUpperCase();
    }
    private void addUser(){
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(logtxt)){
//                    Toast.makeText(Register.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                    noExistAdmin = false;
                }else{
                    noExistAdmin = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(logtxt)){
//                    Toast.makeText(Register.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                    noExistUser = false;
                }else{
                    noExistUser = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(noExistUser && noExistAdmin){
            JavaMailAPI javaMailAPI = new JavaMailAPI(Register.this, emailtxt, "Kod potwierdzający rejestracje", "<div style='background-image:linear-gradient(to right,#7400b8,#80ffdb); margin: 10px;'><h1 style='text-align:center;padding-top: 30px;'>Twój kod Aktywacyjny</h1><h2 style='text-align:center;padding-bottom:30px'>"+code+"</h2><h4 style='padding: 20px; text-align:center;'>Jeśli to nie ty prosiłeś o weryfikacje zignoruj tą wiadomość</h4></div>");
            javaMailAPI.execute();
            passData();
            finish();
        }
    }


}