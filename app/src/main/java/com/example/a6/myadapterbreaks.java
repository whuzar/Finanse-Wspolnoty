package com.example.a6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class myadapterbreaks extends FirebaseRecyclerAdapter<modelbreaks, myadapterbreaks.myviewbreaks> {

    List<String> name=new ArrayList<String>();
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    SharedPreferences sharedPreferences;
    String login, who;



    public myadapterbreaks(@NonNull FirebaseRecyclerOptions<modelbreaks> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewbreaks holder, int position, @NonNull modelbreaks model) {

        holder.idea.setText(model.getmybreak());
        name.add(model.getmybreak());
        if(who.equals("user")){
            holder.button.setVisibility(View.GONE);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(name);
                String itemName = name.get(holder.getAdapterPosition());
                deleteElement(itemName, holder.getAdapterPosition());
            }
        });

    }

    @NonNull
    @Override
    public myviewbreaks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = parent.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);
        who = sharedPreferences.getString(KEY_LOGED, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowbreak, parent, false);
        myviewbreaks viewHolder = new myviewbreaks(view);


        return viewHolder;
    }
    public void deleteElement(String itemName, int pos){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child(who).child(login).child("team").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String team = String.valueOf(task.getResult().getValue());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(itemName == null){
                                return;
                            }
                            snapshot.child("wspolnota").child(team).child("breaks").child(itemName).getRef().removeValue();
                            name.remove(pos);
//                                    for (DataSnapshot ds : snapshot.child("wspolnota").child(team).child("createdpoll").getChildren()) {
//
//                                        if(String.valueOf(ds.child("idea").getValue()).equals(itemName)){
//                                            ds.getRef().removeValue();
//                                        }
//
//
//                                    }

//                                    Log.i("cos", String.valueOf(snapshot.child("wspolnota").child(team).child("createdpol").child(String.valueOf(viewHolder.getAdapterPosition()+1)).getValue()));

//                                    Log.i("usuwam", "usuwam");



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }

    class myviewbreaks extends RecyclerView.ViewHolder{

        public TextView idea;
        LinearLayout line;
        private ImageView button;

        public myviewbreaks(@NonNull View intemview){
            super(intemview);
            button = itemView.findViewById(R.id.deleteBreakAdmin);
            line = itemView.findViewById(R.id.allbreaks);
            idea = itemView.findViewById(R.id.mybreak);

        }
    }
    
}
