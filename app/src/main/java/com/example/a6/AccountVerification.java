package com.example.a6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String email;
    private String password;
    private String login;
    private String phone;
    private String shares;
    private String wspolnotaId;
    private String name;
    private String surname;
    private String usershares;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        codeInput = findViewById(R.id.code);
        final Button weryfikuj = findViewById(R.id.weryfikuj);
        final TextView ponownie = findViewById(R.id.ponownie);
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
        email = bundle.getString("email");
        password = bundle.getString("password");
        login = bundle.getString("login");
        phone = bundle.getString("phone");
        shares = bundle.getString("shares");
        name = bundle.getString("name");
        surname = bundle.getString("surname");
        usershares = bundle.getString("usershares");
        wspolnotaId = generateCode();

        weryfikuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userCode = codeInput.getEditText().getText().toString();
                
                if (userCode.equals(code)){
                    addWspolnota();
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.child("admin").child(login).child("email").setValue(email);
                                databaseReference.child("admin").child(login).child("password").setValue(password);
                                databaseReference.child("admin").child(login).child("phone").setValue(phone);
                                databaseReference.child("admin").child(login).child("name").setValue(name);
                                databaseReference.child("admin").child(login).child("surname").setValue(surname);
                                databaseReference.child("admin").child(login).child("team").setValue(wspolnotaId);
                                databaseReference.child("admin").child(login).child("shares").setValue(usershares);
//                                databaseReference.child("admin").child(login).child("usershares").setValue(usershares);
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
    private Void addWspolnota(){
        databaseReference.child("wspolnota").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(wspolnotaId)){
                    wspolnotaId = generateCode();
                    addWspolnota();
                }else{
                    databaseReference.child("wspolnota").child(wspolnotaId).child("shares").setValue(shares);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}