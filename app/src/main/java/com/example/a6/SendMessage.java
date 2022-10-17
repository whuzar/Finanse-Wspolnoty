package com.example.a6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendMessage extends AppCompatActivity {

    private EditText theme, message;
    private Button btnsend;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        theme = findViewById(R.id.thememessage);
        message = findViewById(R.id.messageadmin);

        btnsend = findViewById(R.id.acceptsendmessage);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tm = theme.getText().toString();
                String ma = message.getText().toString();

                if(!tm.equals("") && !ma.equals("")) {

                    DatabaseReference uidRef = databaseReference.child("admin").child(login);
                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String teamwspo = task.getResult().child("team").getValue(String.class);

                                    DatabaseReference textRef = databaseReference.child("wspolnota").child(teamwspo);
                                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();

                                                databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        databaseReference.child("wspolnota").child(teamwspo).child("notice").child("thememessage").setValue(tm);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("notice").child("message").setValue(ma);
                                                        databaseReference.child("wspolnota").child(teamwspo).child("notice").child("sendby").setValue(login);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Toast.makeText(SendMessage.this, "Wiadomość wysłana pomyślnie", Toast.LENGTH_SHORT).show();
                                                theme.setText("");
                                                message.setText("");
                                            } else {
                                                Log.d("TAG", task.getException().getMessage());
                                            }
                                        }
                                    });
                                    ;
                                }
                            }
                        }
                    });
                }else {
                    Toast.makeText(SendMessage.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}