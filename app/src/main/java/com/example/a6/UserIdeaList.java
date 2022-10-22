package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserIdeaList extends AppCompatActivity {
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_LOGIN = "login";
    myadapterUserIdea adapter;
    private String who;
    private String team;
    private String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_idea_list);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.userIdeaRecycler);
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


                    FirebaseRecyclerOptions<modelvote> options = new FirebaseRecyclerOptions
                            .Builder<modelvote>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("wspolnota").child(team).child("userIdeas"), modelvote.class)
                            .build();

                    adapter = new myadapterUserIdea(options);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.startListening();
        }
    }
}