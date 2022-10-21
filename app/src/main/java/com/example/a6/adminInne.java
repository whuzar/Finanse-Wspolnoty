package com.example.a6;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Objects;

public class adminInne extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_NUMBER = "number";
    private String who;
    Dialog mDialog;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private RelativeLayout voteIdea, createlogin, editanuser, sendmes, deleteaccuser, voteon, adminIdeaList;
    private TextView logina, phonea;
    private ImageView img, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inne);

        mDialog = new Dialog(this);

        voteIdea = findViewById(R.id.voteidea);
        createlogin = findViewById(R.id.createlogingo);
        editanuser = findViewById(R.id.editanotheruser);
        sendmes = findViewById(R.id.sendmessage);
        deleteaccuser = findViewById(R.id.deletuser);
        voteon = findViewById(R.id.voteonideaclick);
        adminIdeaList = findViewById(R.id.adminidealist);

        logina = findViewById(R.id.setloginadmin);
        phonea = findViewById(R.id.setphoneadmin);

        img = findViewById(R.id.photopic);
        back = findViewById(R.id.backlastview);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        who = sharedPreferences.getString(KEY_LOGED, null);
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
        adminIdeaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, AdminIdeasList.class));
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
        sendmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, SendMessage.class));
            }
        });

        voteon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminInne.this, VoteOnIdeaFinish.class));
            }
        });

        deleteaccuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDeleteUser(view);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                        if(picprof != null){
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
                                    .into(profilepohoto);}
                    }
                }
            }
        });
    }

    public void ShowDeleteUser(View v){
        mDialog.setContentView(R.layout.popupdeleteaccuser);
        Button btnexit = mDialog.findViewById(R.id.backuser);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        Button btndel = mDialog.findViewById(R.id.deltuser);

        EditText loginuser = mDialog.findViewById(R.id.userlogin);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ly = loginuser.getText().toString();

                DatabaseReference uidRef = databaseReference;
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String team1 = task.getResult().child("admin").child(login).child("team").getValue(String.class);
                                String team2 = task.getResult().child("user").child(ly).child("team").getValue(String.class);
                                if(Objects.equals(team1, team2)){


                                    DatabaseReference uid = databaseReference.child("user").child(ly);
                                    uid.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot remov: dataSnapshot.getChildren()) {
                                                remov.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e(TAG, "onCancelled", databaseError.toException());
                                        }
                                    });
                                    Toast.makeText(adminInne.this, "Usunięto użytkownika", Toast.LENGTH_SHORT).show();
                                    loginuser.setText("");
                                }
                                else{
                                    Toast.makeText(adminInne.this, "Taki użytkownik w twojej wspólocie nie istnieje", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });
        mDialog.show();
    }

}