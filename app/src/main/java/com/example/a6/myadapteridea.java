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

public class myadapteridea extends FirebaseRecyclerAdapter<modelidea, myadapteridea.myviewidea> {

    public myadapteridea(@NonNull FirebaseRecyclerOptions<modelidea> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewidea holder, int position, @NonNull modelidea model) {


        holder.idea.setText(model.getIdea());

    }

    @NonNull
    @Override
    public myviewidea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowrec, parent, false);
        myviewidea viewHolder = new myviewidea(view);
        return viewHolder;
    }

    class myviewidea extends RecyclerView.ViewHolder{

        private TextView idea;
        LinearLayout line;

        public myviewidea(@NonNull View intemview){
            super(intemview);

            line = itemView.findViewById(R.id.lineidea);
            idea = itemView.findViewById(R.id.idea);

        }
    }
}
