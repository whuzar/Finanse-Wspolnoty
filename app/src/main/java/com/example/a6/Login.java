package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NUMBER = "number";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        final TextInputLayout logi = findViewById(R.id.log);
        final TextInputLayout password = findViewById(R.id.password);
        final Button loginbtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.register);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

//Jeżeli chcemy aby po wejsciu raz loginem zapamietalo
        String loginremember = sharedPreferences.getString(KEY_LOGIN, null);

        if (loginremember != null){
            startActivity(new Intent(Login.this, MainActivity.class));
        }


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String logiText = logi.getEditText().getText().toString();
                final String passwordTxt = password.getEditText().getText().toString();

                if(logiText.isEmpty() || passwordTxt.isEmpty()){
                    if(logiText.isEmpty()){
                        logi.setError("Pole nie może być puste");
                    }
                    if(passwordTxt.isEmpty()){
                        password.setError("Pole nie może być puste");
                    }


//                    Toast.makeText(Login.this, "Proszę wprowadzić dane", Toast.LENGTH_SHORT).show();
                }else {

                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(logiText)){
                                final String getPassword = snapshot.child(logiText).child("password").getValue(String.class);
                                if(getPassword.equals(passwordTxt)){
                                    final String getNumber = snapshot.child(logiText).child("phone").getValue(String.class);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_LOGIN, logiText);
                                    editor.putString(KEY_NUMBER, getNumber);
                                    editor.apply();

                                    Toast.makeText(Login.this, "Witamy ponownie", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }else {
                                    password.setError("Niepoprawne hasło");
//                                    Toast.makeText(Login.this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                logi.setError("Niepoprawny login");
//                                Toast.makeText(Login.this, "Niepoprawny login", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}