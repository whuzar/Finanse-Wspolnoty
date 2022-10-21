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

public class myadapterrankidea extends FirebaseRecyclerAdapter<modelrankidea, myadapterrankidea.myviewrankidea> {
    public myadapterrankidea(@NonNull FirebaseRecyclerOptions<modelrankidea> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewrankidea holder, int position, @NonNull modelrankidea model) {

        holder.idea.setText(model.getIdea());

        if (model.getCount() != null) {
            holder.count.setText(model.getCount());
        }
    }

    @NonNull
    @Override
    public myviewrankidea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowrank, parent, false);
        myviewrankidea viewHolder = new myviewrankidea(view);

        return viewHolder;
    }

    class myviewrankidea extends RecyclerView.ViewHolder {

        public TextView idea;
        public TextView count;
        LinearLayout line;

        public myviewrankidea(@NonNull View intemview) {
            super(intemview);

            line = itemView.findViewById(R.id.ranklist);
            idea = itemView.findViewById(R.id.idearank);
            count = itemView.findViewById(R.id.counter);

        }
    }
}