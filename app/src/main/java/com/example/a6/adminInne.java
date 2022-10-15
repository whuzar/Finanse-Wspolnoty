package com.example.a6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class adminInne extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NUMBER = "number";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private RelativeLayout voteIdea, createlogin, editanuser;
    private TextView logina, phonea;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inne);

        voteIdea = findViewById(R.id.voteidea);
        createlogin = findViewById(R.id.createlogingo);
        editanuser = findViewById(R.id.editanotheruser);

        logina = findViewById(R.id.setloginadmin);
        phonea = findViewById(R.id.setphoneadmin);

        img = findViewById(R.id.photopic);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        refreshLayout = findViewById(R.id.refreshLayoutai);

        refresh(logina, phonea, img);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(logina, phonea, img);
                refreshLayout.setRefreshing(false);
            }
        });

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
    private void refresh(TextView login_underphoto, TextView number_telephone, ImageView profilepohoto) {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String numberphone = sharedPreferences.getString(KEY_NUMBER, null);


        if(login != null || numberphone != null) {
            login_underphoto.setText(login);
            number_telephone.setText("+48" + numberphone);
        }

        DatabaseReference uidRef = databaseReference.child("admin").child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String picprof = task.getResult().child("pimage").getValue(String.class);

                        Transformation transformation = new RoundedTransformationBuilder()
                                .borderColor(Color.WHITE)
                                .borderWidthDp(1)
                                .cornerRadiusDp(100)
                                .oval(true)
                                .build();

                        Picasso.get()
                                .load(picprof)
                                .fit()
                                .transform(transformation)
                                .into(profilepohoto);
                    }
                }
            }
        });
    }
}