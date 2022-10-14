package com.example.a6;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapterrec extends FirebaseRecyclerAdapter<modelrec, myadapterrec.myviewrec> {

    public myadapterrec(@NonNull FirebaseRecyclerOptions<modelrec> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewrec holder, int position, @NonNull modelrec model) {

        holder.namesur.setText(model.getName() + " " + model.getSurname());
        holder.email.setText(model.getEmail());
        holder.phone.setText("+48" + model.getPhone());

    }

    @NonNull
    @Override
    public myviewrec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowrec, parent, false);
        return new myviewrec(view);
    }

    class myviewrec extends RecyclerView.ViewHolder{

        private ImageView img;
        private TextView namesur, phone, email;

        public myviewrec(@NonNull View intemview){
            super(intemview);

            img = (ImageView)itemView.findViewById(R.id.profilepicadmins);
            namesur = (TextView)itemView.findViewById(R.id.namesurrec);
            phone = (TextView)itemView.findViewById(R.id.phonerec);
            email = (TextView)itemView.findViewById(R.id.emailrec);
        }
    }
}
