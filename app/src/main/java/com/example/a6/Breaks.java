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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Breaks extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    myadapterbreaks adapter;
    private String team;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private EditText typebreak;
    private Button btnsendbreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breaks);
        recyclerView = findViewById(R.id.recyclebreaks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        refreshLayout = findViewById(R.id.refreshLayoutay);

        typebreak = findViewById(R.id.reportbreaks);
        btnsendbreak = findViewById(R.id.btnreport);

        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);

        getbreaks(who, login);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getbreaks(who, login);
                refreshLayout.setRefreshing(false);
            }
        });

        btnsendbreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tb = typebreak.getText().toString();
                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String teamwspo = task.getResult().child(who).child(login).child("team").getValue(String.class);

                                if(!tb.equals("")){
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int size = (int) snapshot.child("wspolnota").child(teamwspo).child("breaks").getChildrenCount();
                                            size = size +1;
                                            databaseReference.child("wspolnota").child(teamwspo).child("breaks").child(String.valueOf(size)).child("mybreak").setValue(tb);
                                            databaseReference.child("wspolnota").child(teamwspo).child("breaks").child(String.valueOf(size)).child("user").setValue(login);

                                            Toast.makeText(Breaks.this, "Pomyślnie dodano usterkę", Toast.LENGTH_SHORT).show();
                                            typebreak.setText("");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }else{
                                    Toast.makeText(Breaks.this, "Uzupełnij dane", Toast.LENGTH_SHORT).show();
                                }
//
                            }
                        }
                    }
                });


            }
        });

    }
    public void getbreaks(String who, String login){
        databaseReference.child(who).child(login).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    team = String.valueOf(task.getResult().getValue());
                    FirebaseRecyclerOptions<modelbreaks> options = new FirebaseRecyclerOptions
                            .Builder<modelbreaks>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("wspolnota").child(team).child("breaks"), modelbreaks.class)
                            .build();

                    adapter = new myadapterbreaks(options);
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