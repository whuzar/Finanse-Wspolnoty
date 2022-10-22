package com.example.a6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class myadaptervote extends FirebaseRecyclerAdapter<modelvote, myadaptervote.myviewvote> {

    List<String> name=new ArrayList<String>();
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    ViewGroup parent;
    public myadaptervote(@NonNull FirebaseRecyclerOptions<modelvote> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewvote holder, int position, @NonNull modelvote model) {

        holder.idea.setText(model.getIdea());
        name.add(model.getIdea());
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view, parent, holder.getAdapterPosition());

            }
        });

    }

    @NonNull
    @Override
    public myviewvote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowidea, parent, false);
        this.parent = parent;
        myviewvote viewHolder = new myviewvote(view);


        return viewHolder;
    }

    class myviewvote extends RecyclerView.ViewHolder{

        public TextView idea;
        LinearLayout line;

        public myviewvote(@NonNull View intemview){
            super(intemview);

            line = itemView.findViewById(R.id.allideas);
            idea = itemView.findViewById(R.id.idea);

        }
    }
    public void ShowPopup(View v, ViewGroup parent, int i1){
        Dialog mDialog;
        mDialog = new Dialog(parent.getContext());
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.popupshowvote);
        Button vote = mDialog.findViewById(R.id.vote);
        Button back = mDialog.findViewById(R.id.backvote);
        TextView insertidea = mDialog.findViewById(R.id.inserttext);
        String word = name.get(i1);
        insertidea.setText(word);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences;

        sharedPreferences = parent.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference uidRefcheck = databaseReference.child(who).child(login);
                uidRefcheck.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String check = task.getResult().child("voted").child("send").getValue(String.class);
                                if (check == null) {
                                    DatabaseReference uidRef = databaseReference;
                                    uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                                    String teamwspo = task.getResult().child(who).child(login).child("team").getValue(String.class);
                                                    String shares = task.getResult().child(who).child(login).child("shares").getValue(String.class);
                                                    String number = task.getResult().child("wspolnota").child(teamwspo).child("createdpoll").child(word).child("count").getValue(String.class);
                                                    String power = task.getResult().child("wspolnota").child(teamwspo).child("showidea").child("power").getValue(String.class);

                                                    DatabaseReference textRef = databaseReference;
                                                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DataSnapshot snapshot = task.getResult();

                                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                        if (Objects.equals(power, "shares")) {
                                                                            if (number != null) {
                                                                                int x = Integer.parseInt(number);
                                                                                x = Integer.parseInt(String.valueOf(x + Integer.parseInt(shares)));
                                                                                databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(word).child("count").setValue(String.valueOf(x));
//
                                                                            } else {
                                                                                databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(word).child("count").setValue(shares);
//
                                                                            }

                                                                        } else if (Objects.equals(power, "one")) {
                                                                            if (number != null) {
                                                                                int x = Integer.parseInt(number);
                                                                                x = x + 1;
                                                                                databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(word).child("count").setValue(String.valueOf(x));
//
                                                                            } else {
                                                                                String y = "1";
                                                                                databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(word).child("count").setValue(y);
//
                                                                            }
                                                                        }
                                                                        databaseReference.child(who).child(login).child("voted").child("send").setValue("true");
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                    }
                                                                });
                                                                Toast.makeText(parent.getContext(), "Pomyślnie zagłosowano", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Log.d("TAG", task.getException().getMessage());
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(parent.getContext(), "Oddałeś już swój głos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                mDialog.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
