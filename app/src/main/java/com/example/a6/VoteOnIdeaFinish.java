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

public class VoteOnIdeaFinish extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private EditText type;
    private Button btn, btncreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_idea_finish);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        type = findViewById(R.id.ideainsert);
        btn = findViewById(R.id.addidea);
        btncreate = findViewById(R.id.create);

        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ii = type.getText().toString();

                if(!ii.equals("")) {
                    DatabaseReference uidRef = databaseReference.child("admin").child(login);
                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                    String teamwspo = task.getResult().child("team").getValue(String.class);

                                    DatabaseReference textRef = databaseReference;
                                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();
                                                String number = task.getResult().child("wspolnota").child(teamwspo).child("createdpoll").child("count").getValue(String.class);

                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        if(number != null){
                                                            int x = Integer.parseInt(number);
                                                            x = x + 1;
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("count").setValue(String.valueOf(x));
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("ideas").child(String.valueOf(x)).setValue(ii);
                                                        }else {
                                                            String y = "1";
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("count").setValue(y);
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("ideas").child(y).setValue(ii);
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Toast.makeText(VoteOnIdeaFinish.this, "Dodano", Toast.LENGTH_SHORT).show();
                                                type.setText("");
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
                }else{
                    Toast.makeText(VoteOnIdeaFinish.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}