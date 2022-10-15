package com.example.a6;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

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
        if(model.getPimage() == null){
        }else {
            Glide.with(holder.img.getContext()).load(model.getPimage()).into(holder.img);
        }

    }

    @NonNull
    @Override
    public myviewrec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowrec, parent, false);
        myviewrec viewHolder = new myviewrec(view);
        return viewHolder;
    }

    class myviewrec extends RecyclerView.ViewHolder{

        private CircleImageView img;
        private TextView namesur, phone, email;
        LinearLayout line;

        public myviewrec(@NonNull View intemview){
            super(intemview);

            line = itemView.findViewById(R.id.admindate);
            img = itemView.findViewById(R.id.picadminr);
            namesur = itemView.findViewById(R.id.namesurrec);
            phone = itemView.findViewById(R.id.phonerec);
            email = itemView.findViewById(R.id.emailrec);

        }
    }
}
