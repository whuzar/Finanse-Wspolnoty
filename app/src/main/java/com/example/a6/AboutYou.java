package com.example.a6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AboutYou extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    TextView anamesur, aemail, aphone, ashares;
    Button btnchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        anamesur = findViewById(R.id.aboutyounameandsurname);
        aemail = findViewById(R.id.aboutyouemail);
        aphone = findViewById(R.id.aboutyouphone);
        ashares = findViewById(R.id.aboutyoushares);

        btnchange = findViewById(R.id.bntchangeinformations);

        refreshLayout = findViewById(R.id.refreshLayoutay);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        showay();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showay();
                refreshLayout.setRefreshing(false);
            }
        });

        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutYou.this, EditAboutYou.class));
            }
        });

    }
    private void showay() {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        DatabaseReference uidRef = databaseReference.child("admin").child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String surname = task.getResult().child("surname").getValue(String.class);
                        String name = task.getResult().child("name").getValue(String.class);
                        String email = task.getResult().child("email").getValue(String.class);
                        String phone = task.getResult().child("phone").getValue(String.class);
                        String shares = task.getResult().child("shares").getValue(String.class);

                        anamesur.setText(name + " "+ surname);
                        aemail.setText(email);
                        aphone.setText(phone);
                        ashares.setText(shares);

                    }
                }
            }
        });
    }
}