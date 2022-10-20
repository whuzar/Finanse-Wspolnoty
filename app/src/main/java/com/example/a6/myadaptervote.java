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

public class myadaptervote extends FirebaseRecyclerAdapter<modelvote, myadaptervote.myviewvote> {

    List<String> name=new ArrayList<String>();
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    public myadaptervote(@NonNull FirebaseRecyclerOptions<modelvote> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewvote holder, int position, @NonNull modelvote model) {

        holder.idea.setText(model.getIdea());
        name.add(model.getIdea());

    }

    @NonNull
    @Override
    public myviewvote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowidea, parent, false);
        myviewvote viewHolder = new myviewvote(view);

        viewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view, parent, viewHolder.getAdapterPosition());

            }
        });
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
                DatabaseReference uidRef = databaseReference;
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String teamwspo = task.getResult().child(who).child(login).child("team").getValue(String.class);
                                String number = task.getResult().child("wspolnota").child(teamwspo).child("createdpoll").child(String.valueOf(i1+1)).child("count").getValue(String.class);

                                DatabaseReference textRef = databaseReference;
                                textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DataSnapshot snapshot = task.getResult();

                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if(number != null){
                                                            int x = Integer.parseInt(number);
                                                            x = x + 1;
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(String.valueOf(i1+1)).child("count").setValue(String.valueOf(x));
//                                                            Log.i("1", String.valueOf(x));
                                                    }else {
                                                            String y = "1";
                                                            databaseReference.child("wspolnota").child(teamwspo).child("createdpoll").child(String.valueOf(i1+1)).child("count").setValue(y);
//                                                        Log.i("2", y);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {}
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
