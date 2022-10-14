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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class EditAnotherUserNext extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN2 = "login2";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    private EditText eaphone, eaemail, eashares, eanamesur;
    private Button eaconfirm;
    private TextView insertl;

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

                        eanamesur.setText(name + " "+ surname);

                    }
                }
            }
        });

        eaconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ep = eaphone.getText().toString();
                String ee = eaemail.getText().toString();
                String es = eashares.getText().toString();

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
                eaphone.setText("");
                eaemail.setText("");
                eashares.setText("");
            }

        });
    }
}