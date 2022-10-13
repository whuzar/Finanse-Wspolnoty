package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    private EditText oldpass, newpass, repeatnewpass;
    Button changepasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = findViewById(R.id.oldpassword);
        newpass = findViewById(R.id.newpassword);
        repeatnewpass = findViewById(R.id.repeatnewpassword);
        changepasswd = findViewById(R.id.bntchangepasswd);
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        changepasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String op = oldpass.getText().toString();
                String np = newpass.getText().toString();
                String rnp = repeatnewpass.getText().toString();

                DatabaseReference textRef = databaseReference.child("admin").child(login).child("password");
                textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            String text = snapshot.getValue(String.class);
                            if(op == np){
                                Toast.makeText(ChangePassword.this, "Hasło nie może być takie samo jak poprzednie", Toast.LENGTH_SHORT).show();
                            }else if(!np.equals(rnp)){
                                Toast.makeText(ChangePassword.this, "Hasła nie są takie same", Toast.LENGTH_SHORT).show();
                            }else if(!Objects.equals(text, op)){
                                Toast.makeText(ChangePassword.this, "Nie zgodne jest stare hasło", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child("admin").child(login).child("password").setValue(np);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Toast.makeText(ChangePassword.this, "Hasło zostało zmienione pomyślnie", Toast.LENGTH_SHORT).show();
                                oldpass.setText("");
                                newpass.setText("");
                                repeatnewpass.setText("");
                            }
                        } else {
                            Log.d("TAG", task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }
}