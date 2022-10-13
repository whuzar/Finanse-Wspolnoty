package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.util.Random;

public class AccountVerification extends AppCompatActivity {
    private TextInputLayout codeInput;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        codeInput = findViewById(R.id.code);
        final Button weryfikuj = findViewById(R.id.weryfikuj);
        final TextView ponownie = findViewById(R.id.ponownie);
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
        String email = bundle.getString("email");
        String password = bundle.getString("password");
        String login = bundle.getString("login");
        String phone = bundle.getString("phone");
        String wspolnotaId = generateCode();

        weryfikuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userCode = codeInput.getEditText().getText().toString();
                
                if (userCode.equals(code)){
//                    databaseReference.child("wspolnota").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.hasChild(wspolnotaId)){
//
//                            }else{
//                                databaseReference.child("admin").child(login).child("email").setValue(email);
//                                databaseReference.child("admin").child(login).child("password").setValue(password);
//                                databaseReference.child("admin").child(login).child("phone").setValue(phone);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("admin").child(login).child("email").setValue(email);
                                databaseReference.child("admin").child(login).child("password").setValue(password);
                                databaseReference.child("admin").child(login).child("phone").setValue(phone);
                                // przy generowaniu loginu ma nie byc admina chyba ze generuje dla kolejnych zarzadcow

                                Toast.makeText(AccountVerification.this, "Utworzono użytkonika", Toast.LENGTH_SHORT).show();
                                finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    codeInput.setError(null);
                }else{
                    codeInput.setError("Nieprawidłowy kod");
                }
            }
        });

        ponownie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = generateCode();
                JavaMailAPI javaMailAPI = new JavaMailAPI(AccountVerification.this, email, "Kod potwierdzający rejestracje", "<div style='background-image:linear-gradient(to right,#7400b8,#80ffdb); margin: 10px;'><h1 style='text-align:center;padding-top: 30px;'>Twój kod Aktywacyjny</h1><h2 style='text-align:center;padding-bottom:30px'>"+code+"</h2><h4 style='padding: 20px; text-align:center;'>Jeśli to nie ty prosiłeś o weryfikacje zignoruj tą wiadomość</h4></div>");
                javaMailAPI.execute();
                Toast.makeText(AccountVerification.this, "Wysłano ponownie", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String generateCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}