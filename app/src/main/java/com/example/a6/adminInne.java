package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class adminInne extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NUMBER = "number";

    private RelativeLayout voteIdea, createlogin, editanuser;
    private TextView logina, phonea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inne);

        voteIdea = findViewById(R.id.voteidea);
        createlogin = findViewById(R.id.createlogingo);
        editanuser = findViewById(R.id.editanotheruser);

        logina = findViewById(R.id.setloginadmin);
        phonea = findViewById(R.id.setphoneadmin);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String numberphone = sharedPreferences.getString(KEY_NUMBER, null);

        if(login != null || numberphone != null) {
            logina.setText(login);
            phonea.setText("+48" + numberphone);
        }

        voteIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, VoteOnIdeaAdmin.class));
            }
        });

        createlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, CreateLoginUserAdmin.class));
            }
        });

        editanuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, EditAnotherUser.class));
            }
        });
    }
}