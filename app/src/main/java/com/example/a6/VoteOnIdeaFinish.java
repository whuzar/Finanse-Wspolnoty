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

                                    DatabaseReference textRef = databaseReference.child("wspolnota").child(teamwspo).child("createdpoll");
                                    textRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // get total available quest
                                            int size = (int) dataSnapshot.getChildrenCount();
//                                            int size = (int) snapshot.getChildrenCount();
//                                            Log.i("size before", String.valueOf(size));
                                                            size = size + 1;
//                                            Log.i("size after", String.valueOf(size));
//                                                       databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("count").setValue(String.valueOf(x));
//                                                        databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child("idea").setValue(idea + "&" + ii);
                                            textRef.child(String.valueOf(size)).child("idea").setValue(ii);
                                            Toast.makeText(VoteOnIdeaFinish.this, "Dodano", Toast.LENGTH_SHORT).show();
                                            type.setText("");
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
//
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