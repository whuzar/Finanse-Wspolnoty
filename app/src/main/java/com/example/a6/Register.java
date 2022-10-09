package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText email = findViewById(R.id.email);
        final EditText log = findViewById(R.id.logi);
        final EditText password = findViewById(R.id.password);
        final EditText conpassword = findViewById(R.id.conpassword);
        final EditText phon = findViewById(R.id.phone);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView login = findViewById(R.id.login);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String emailtxt = email.getText().toString();
                final String logtxt = log.getText().toString();
                final String passwordtxt = password.getText().toString();
                final String conpasswordtxt = conpassword.getText().toString();
                final String phontxt = phon.getText().toString();

                if(emailtxt.isEmpty() || logtxt.isEmpty() || passwordtxt.isEmpty() || conpasswordtxt.isEmpty() || phontxt.isEmpty()){
                    Toast.makeText(Register.this, "Proszę wprowadzić dane", Toast.LENGTH_SHORT).show();
                }

                else if(!passwordtxt.equals(conpasswordtxt)){
                    Toast.makeText(Register.this, "Hasła nie pasują do siebie!", Toast.LENGTH_SHORT).show();
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


}