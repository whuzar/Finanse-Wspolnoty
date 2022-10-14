package com.example.a6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditAnotherUser extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_TEAM = "team";
    private static final String KEY_TEAM2 = "team2";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    private EditText getuser;
    private Button btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_another_user);

        getuser = findViewById(R.id.getloginuser);
        btnnext = findViewById(R.id.btnnextgo);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String downloaduserinfo = getuser.getText().toString();

                DatabaseReference uidRef = databaseReference.child("admin").child(downloaduserinfo);
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String team = task.getResult().child("team").getValue(String.class);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_TEAM, team);
                                editor.apply();
                            }
                        }
                    }
                });
                String teamwspo = sharedPreferences.getString(KEY_TEAM, null);
                DatabaseReference uidRef2 = databaseReference.child("admin").child(login);
                uidRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String team2 = task.getResult().child("team").getValue(String.class);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_TEAM2, team2);
                                editor.apply();
                            }
                        }
                    }
                });
                String teamwspo2 = sharedPreferences.getString(KEY_TEAM2, null);

                if(teamwspo == teamwspo2){
                    startActivity(new Intent(EditAnotherUser.this, EditAnotherUserNext.class));
                }else{
                    Toast.makeText(EditAnotherUser.this, "Taki użytkownik w twojej wspólocie nie istnieje", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}