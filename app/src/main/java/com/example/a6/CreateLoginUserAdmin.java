package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Random;

public class CreateLoginUserAdmin extends AppCompatActivity {

    private EditText name, username, email, phonenumber, sharesuser;

    Dialog mDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login_user_admin);

        name = findViewById(R.id.nameuseroradmin);
        username = findViewById(R.id.usernameuseroradmin);
        email = findViewById(R.id.emailuseroradmin);
        phonenumber = findViewById(R.id.phoneuseroradmin);
        sharesuser = findViewById(R.id.sharesuseroradmin);

        mDialog = new Dialog(this);

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

        final String nameau = name.getText().toString();
        final String surnameau = username.getText().toString();
        final String mailau = email.getText().toString();
        final String phoneau = phonenumber.getText().toString();
        final String sharesau = sharesuser.getText().toString();

        Random rand = new Random();
        int n = rand.nextInt(1000);

        String password = "";
        String valuecharacter = "";

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(int i = 0; i< 13; i++){
            int ranletter = rand.nextInt(characters.length());
            valuecharacter = String.valueOf(characters.charAt(ranletter));
            password = password + valuecharacter;
        }

        String logtxt = nameau.substring(0, 3) + surnameau.substring(0, 3) + n;
        logtxt.toUpperCase(Locale.ROOT);


        String finalPassword = password;
        databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(logtxt)){
                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("admin").child(logtxt).child("name").setValue(nameau);
                    databaseReference.child("admin").child(logtxt).child("surname").setValue(surnameau);
                    databaseReference.child("admin").child(logtxt).child("email").setValue(mailau);
                    databaseReference.child("admin").child(logtxt).child("password").setValue(finalPassword);
                    databaseReference.child("admin").child(logtxt).child("phone").setValue(phoneau);
                    databaseReference.child("admin").child(logtxt).child("shares").setValue(sharesau);
                    databaseReference.child("admin").child(logtxt).child("team").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.setText("");
        username.setText("");
        email.setText("");
        phonenumber.setText("");
        sharesuser.setText("");

        Toast.makeText(CreateLoginUserAdmin.this, "Pomyślnie utworzono zarządcę", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    public void CreateUserAccept(View v){
        mDialog.setContentView(R.layout.popupcreateua);

        final String nameau = name.getText().toString();
        final String surnameau = username.getText().toString();
        final String mailau = email.getText().toString();
        final String phoneau = phonenumber.getText().toString();
        final String sharesau = sharesuser.getText().toString();

        Random rand = new Random();
        int n = rand.nextInt(1000);

        String password = "";
        String valuecharacter = "";

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(int i = 0; i< 13; i++){
            int ranletter = rand.nextInt(characters.length());
            valuecharacter = String.valueOf(characters.charAt(ranletter));
            password = password + valuecharacter;
        }

        String logtxt = nameau.substring(0, 3) + surnameau.substring(0, 3) + n;
        logtxt.toUpperCase(Locale.ROOT);


        String finalPassword = password;
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(logtxt)){
                    Toast.makeText(CreateLoginUserAdmin.this, "Ten login już istnieje", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("user").child(logtxt).child("name").setValue(nameau);
                    databaseReference.child("user").child(logtxt).child("surname").setValue(surnameau);
                    databaseReference.child("user").child(logtxt).child("email").setValue(mailau);
                    databaseReference.child("user").child(logtxt).child("password").setValue(finalPassword);
                    databaseReference.child("user").child(logtxt).child("phone").setValue(phoneau);
                    databaseReference.child("user").child(logtxt).child("shares").setValue(sharesau);
                    databaseReference.child("user").child(logtxt).child("team").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.setText("");
        username.setText("");
        email.setText("");
        phonenumber.setText("");
        sharesuser.setText("");

        Toast.makeText(CreateLoginUserAdmin.this, "Pomyślnie utworzono użytkonika", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

}