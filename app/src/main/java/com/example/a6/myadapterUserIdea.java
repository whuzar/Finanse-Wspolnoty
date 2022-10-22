package com.example.a6;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class myadapterUserIdea extends FirebaseRecyclerAdapter<modelvote, myadapterUserIdea.mywierUserIdea> {
    List<String> name=new ArrayList<String>();
    public myadapterUserIdea(@NonNull FirebaseRecyclerOptions<modelvote> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull mywierUserIdea holder, int position, @NonNull modelvote model) {

        holder.idea.setText(model.getIdea());
        name.add(model.getIdea());

    }

    @NonNull
    @Override
    public mywierUserIdea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowideauser, parent, false);
        mywierUserIdea viewHolder = new mywierUserIdea(view);

        return viewHolder;
    }

    class mywierUserIdea extends RecyclerView.ViewHolder {

        public TextView idea;
        LinearLayout line;

        public mywierUserIdea(@NonNull View intemview) {
            super(intemview);

            line = itemView.findViewById(R.id.allideasUser);
            idea = itemView.findViewById(R.id.ideaUser);

        }
    }
}