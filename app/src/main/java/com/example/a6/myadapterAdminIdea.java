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


    }

    @NonNull
    @Override
    public myviewAdminIdea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowideaadmin, parent, false);
        viewHolder = new myviewAdminIdea(view);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShowPopup(view, parent, viewHolder.getAdapterPosition());
//                Toast.makeText(parent.getContext(), "elo", Toast.LENGTH_SHORT).show();
                name.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                SharedPreferences sharedPreferences;

                sharedPreferences = parent.getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String login = sharedPreferences.getString(KEY_LOGIN, null);
                String who = sharedPreferences.getString(KEY_LOGED, null);
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

                                    Log.i("cos", String.valueOf(snapshot.child("wspolnota").child(team).child("createdpol").child(String.valueOf(viewHolder.getAdapterPosition()+1)).getValue()));
                                    snapshot.child("wspolnota").child(team).child("createdpoll").child(String.valueOf(viewHolder.getAdapterPosition()+1)).getRef().removeValue();
                                    Log.i("usuwam", "usuwam");



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                    }
                });
//                Log.i("name", String.valueOf(name.size()));
//                Log.i("name", String.valueOf(viewHolder.getAdapterPosition()));

            }
        });
        return viewHolder;
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
