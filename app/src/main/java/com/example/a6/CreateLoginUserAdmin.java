package com.example.a6;

import android.app.Dialog;
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

import java.util.Random;
import java.util.regex.Pattern;

public class CreateLoginUserAdmin extends AppCompatActivity {
    SharedPreferences sharedPreferences;
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
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_LOGIN = "login";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zĘÓĄŚŁŻŹĆŃęóąśłżźć]+");
    private TextInputLayout name, username, email, phonenumber, sharesuser;
    private String nameau;
    private String surnameau;
    private String mailau;
    private String phoneau;
    private String sharesau;
    private String login;
    private String finalPassword;
    private String team;
    private String who;
    private String whoLogin;
    private boolean noExistAdmin = false, noExistUser = false;

    Dialog mDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login_user_admin);

        name = findViewById(R.id.nameuseroradmin);
        username = findViewById(R.id.usernameuseroradmin);
        email = findViewById(R.id.emailuseroradmin);
        phonenumber = findViewById(R.id.phoneuseroradmin);
        sharesuser = findViewById(R.id.sharesuseroradmin);
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        who = sharedPreferences.getString(KEY_LOGED, null);
        whoLogin = sharedPreferences.getString(KEY_LOGIN, null);
        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void ShowPopup(View v){
        mDialog.setContentView(R.layout.popupcreateua);
        Button btnexit = mDialog.findViewById(R.id.backtocreateuser);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void ShowPopup2(View v){
        mDialog.setContentView(R.layout.popupcreateua2);
        Button btnexit = mDialog.findViewById(R.id.backtocreateadmin);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void CreateAdminAccept(View v){
        mDialog.setContentView(R.layout.popupcreateua2);

        nameau = name.getEditText().getText().toString().trim();
        surnameau = username.getEditText().getText().toString().trim();
        mailau = email.getEditText().getText().toString().trim();
        phoneau = phonenumber.getEditText().getText().toString().trim();
        sharesau = sharesuser.getEditText().getText().toString().trim();

        Random rand = new Random();


        String password = "";
        String valuecharacter = "";

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(int i = 0; i< 13; i++){
            int ranletter = rand.nextInt(characters.length());
            valuecharacter = String.valueOf(characters.charAt(ranletter));
            password = password + valuecharacter;
        }



        finalPassword = password;
        if(!validateEmail() || !validatePhone() || !validateShares() || !validateName(nameau, name) || !validateName(surnameau, username)){
            mDialog.dismiss();
            return;
        }
        else {
            nameau = nameau.substring(0, 1).toUpperCase() + nameau.substring(1).toLowerCase();
            surnameau = surnameau.substring(0, 1).toUpperCase() + surnameau.substring(1).toLowerCase();
            databaseReference.child(who).child(whoLogin).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        team = String.valueOf(task.getResult().getValue());
                        generateUsername();
                        addAdmin();
                    }
                }
            });

        }


    }

    public void CreateUserAccept(View v){
        mDialog.setContentView(R.layout.popupcreateua);

        nameau = name.getEditText().getText().toString().trim();

        surnameau = username.getEditText().getText().toString().trim();

        mailau = email.getEditText().getText().toString().trim();
        phoneau = phonenumber.getEditText().getText().toString().trim();
        sharesau = sharesuser.getEditText().getText().toString().trim();

        Random rand = new Random();

        String password = "";
        String valuecharacter = "";

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(int i = 0; i< 13; i++){
            int ranletter = rand.nextInt(characters.length());
            valuecharacter = String.valueOf(characters.charAt(ranletter));
            password = password + valuecharacter;
        }




        finalPassword = password;
        if(!validateEmail() || !validatePhone() || !validateShares() || !validateName(nameau, name) || !validateName(surnameau, username)){
            mDialog.dismiss();
            return;
        }
        else {
            nameau = nameau.substring(0, 1).toUpperCase() + nameau.substring(1).toLowerCase();
            surnameau = surnameau.substring(0, 1).toUpperCase() + surnameau.substring(1).toLowerCase();
            databaseReference.child(who).child(whoLogin).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        team = String.valueOf(task.getResult().getValue());
                        generateUsername();
                        addUser();
                    }
                }
            });


        }

    }
    private void generateUsername(){
        Random rand = new Random();
        int n = rand.nextInt(1000);
        login = nameau.substring(0, 3) + surnameau.substring(0, 3) + n;
        login = login.toUpperCase();
    }

    private void addUser(){
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(login)){
                    noExistAdmin = false;
//                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
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
                if(snapshot.hasChild(login) || !noExistAdmin){
                    generateUsername();
                    addUser();
//                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("user").child(login).child("name").setValue(nameau);
                    databaseReference.child("user").child(login).child("surname").setValue(surnameau);
                    databaseReference.child("user").child(login).child("email").setValue(mailau);
                    databaseReference.child("user").child(login).child("password").setValue(finalPassword);
                    databaseReference.child("user").child(login).child("phone").setValue(phoneau);
                    databaseReference.child("user").child(login).child("shares").setValue(sharesau);
                    databaseReference.child("user").child(login).child("team").setValue(team);
                    databaseReference.child("user").child(login).child("login").setValue(login);
                    JavaMailAPI javaMailAPI = new JavaMailAPI(CreateLoginUserAdmin.this, mailau, "Utworzono dla ciebo konto w Organizator Budżetu", "<div style='background-image:linear-gradient(to right,#7400b8,#80ffdb); margin: 10px;'><h1 style='text-align:center;padding-top: 30px;'>Zostało dla ciebie utworzone konto w aplikacji Organizator budżetu</h1><h2 style='text-align:center;padding-bottom:30px'>Login: "+login+"</h2><h2 style='text-align:center;padding-bottom:30px'>Hasło: "+finalPassword+"</h2><h4 style='padding: 20px; text-align:center;'>Jeśli to nie ty prosiłeś o to konto zignoruj tą wiadomość</h4></div>");
                    javaMailAPI.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.getEditText().setText("");
        username.getEditText().setText("");
        email.getEditText().setText("");
        phonenumber.getEditText().setText("");
        sharesuser.getEditText().setText("");

        Toast.makeText(CreateLoginUserAdmin.this, "Pomyślnie utworzono użytkonika", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    private void addAdmin(){
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(login)){
                    noExistUser = false;
//                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                }else{
                   noExistUser = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(login) || !noExistUser){
                    generateUsername();
                    addAdmin();
//                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("admin").child(login).child("name").setValue(nameau);
                    databaseReference.child("admin").child(login).child("surname").setValue(surnameau);
                    databaseReference.child("admin").child(login).child("email").setValue(mailau);
                    databaseReference.child("admin").child(login).child("password").setValue(finalPassword);
                    databaseReference.child("admin").child(login).child("phone").setValue(phoneau);
                    databaseReference.child("admin").child(login).child("shares").setValue(sharesau);
                    databaseReference.child("admin").child(login).child("team").setValue(team);
                    databaseReference.child("admin").child(login).child("login").setValue(login);
                    JavaMailAPI javaMailAPI = new JavaMailAPI(CreateLoginUserAdmin.this, mailau, "Utworzono dla ciebo konto administratora w Organizator Budżetu", "<div style='background-image:linear-gradient(to right,#7400b8,#80ffdb); margin: 10px;'><h1 style='text-align:center;padding-top: 30px;'>Zostało dla ciebie utworzone konto w aplikacji Organizator budżetu</h1><h2 style='text-align:center;padding-bottom:30px'>Login: "+login+"</h2><h2 style='text-align:center;padding-bottom:30px'>Hasło: "+finalPassword+"</h2><h4 style='padding: 20px; text-align:center;'>Jeśli to nie ty prosiłeś o to konto zignoruj tą wiadomość</h4></div>");
                    javaMailAPI.execute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.getEditText().setText("");
        username.getEditText().setText("");
        email.getEditText().setText("");
        phonenumber.getEditText().setText("");
        sharesuser.getEditText().setText("");

        Toast.makeText(CreateLoginUserAdmin.this, "Pomyślnie utworzono zarządcę", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
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



    private boolean validatePhone() {
        String phoneInput = phonenumber.getEditText().getText().toString().trim();
        if (phoneInput.isEmpty()) {
            phonenumber.setError("Pole numer telefonu nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
            phonenumber.setError("Proszę wpisać poprawnie numer telefonu");
            Toast.makeText(CreateLoginUserAdmin.this, "Proszę wpisać poprawnie numer telefonu", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            phonenumber.setError(null);
            return true;
        }
    }


    private boolean validateShares() {
        String es = sharesuser.getEditText().getText().toString().trim();
        if (es.isEmpty()) {
            sharesuser.setError("Pole udziały nie może być puste");
//            Toast.makeText(Register.this, "Pole numer telefonu nie może być puste", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            log.setError(null);
            sharesuser.setError(null);
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

}