package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class adminInne extends AppCompatActivity {
    private RelativeLayout voteIdea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inne);

        voteIdea = findViewById(R.id.voteidea);

        voteIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, VoteOnIdeaAdmin.class));
            }
        });
    }
}