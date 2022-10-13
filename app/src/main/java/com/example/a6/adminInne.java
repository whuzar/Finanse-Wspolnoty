package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class adminInne extends AppCompatActivity {
    private RelativeLayout voteIdea;
    private RelativeLayout createlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inne);

        voteIdea = findViewById(R.id.voteidea);
        createlogin = findViewById(R.id.createlogingo);

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
    }
}