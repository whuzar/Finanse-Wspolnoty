package com.example.a6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactToAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_LOGIN = "login";
    myadapterrec adapter;
    private String who;
    private String team;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_to_admin);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        who = sharedPreferences.getString(KEY_LOGED, null);
        login = sharedPreferences.getString(KEY_LOGIN, null);

        databaseReference.child(who).child(login).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    team = String.valueOf(task.getResult().getValue());
                    FirebaseRecyclerOptions<modelrec> options = new FirebaseRecyclerOptions
                            .Builder<modelrec>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("admin").orderByChild("team").equalTo(team), modelrec.class)
                            .build();

                    adapter = new myadapterrec(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
            }
        });



    }


    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}