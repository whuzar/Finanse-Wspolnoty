package com.example.a6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class myadapterAdminIdea extends FirebaseRecyclerAdapter<modelvote, myadapterAdminIdea.myviewAdminIdea> {
    List<String> name=new ArrayList<String>();
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    SharedPreferences sharedPreferences;
    ViewGroup parent;
    String login, who;

    myviewAdminIdea viewHolder;
    View view;
    FirebaseRecyclerOptions options;

    public myadapterAdminIdea(@NonNull FirebaseRecyclerOptions<modelvote> options) {
        super(options);


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewAdminIdea holder, int position, @NonNull modelvote model) {
        holder.idea.setText(model.getIdea());
        name.add(model.getIdea());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = name.get(holder.getAdapterPosition());
                Log.i("position", String.valueOf(holder.getAdapterPosition()));
                Log.i("array lengts", String.valueOf(name.size()));
                deleteElement(itemName, holder.getAdapterPosition());
//                ShowPopup(view, parent, viewHolder.getAdapterPosition());
//                Toast.makeText(parent.getContext(), "elo", Toast.LENGTH_SHORT).show();

//                Log.i("name", String.valueOf(name.size()));
//                Log.i("name", String.valueOf(viewHolder.getAdapterPosition()));

            }
        });


    }

    @NonNull
    @Override
    public myviewAdminIdea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = parent.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);
        who = sharedPreferences.getString(KEY_LOGED, null);
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowideaadmin, parent, false);

        this.parent = parent;
        viewHolder = new myviewAdminIdea(view);

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
                            String started = snapshot.child("wspolnota").child(team).child("showidea").child("started").getValue(String.class);
                            if(started != null){
                                if(started.equals("true")){
                                    Toast.makeText(parent.getContext(), "Głosowanie wystartowało nie mozna usunąć", Toast.LENGTH_SHORT).show();
                                }else{
                                    snapshot.child("wspolnota").child(team).child("createdpoll").child(itemName).getRef().removeValue();
                                    name.remove(pos);
                                }
                            }else{
                                snapshot.child("wspolnota").child(team).child("createdpoll").child(itemName).getRef().removeValue();
                                name.remove(pos);
                            }

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

    class myviewAdminIdea extends RecyclerView.ViewHolder{

        public TextView idea;
        ImageView button;

        public myviewAdminIdea(@NonNull View intemview){
            super(intemview);
            button = itemView.findViewById(R.id.deleteIdeaAdmin);
            idea = itemView.findViewById(R.id.ideaAdmin);

        }

    }

}
