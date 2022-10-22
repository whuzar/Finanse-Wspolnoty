package com.example.a6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapterrec extends FirebaseRecyclerAdapter<modelrec, myadapterrec.myviewrec> {

    List<String> name=new ArrayList<String>();
    ViewGroup parent;
    public myadapterrec(@NonNull FirebaseRecyclerOptions<modelrec> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewrec holder, int position, @NonNull modelrec model) {

        holder.namesur.setText(model.getlogin());
        holder.email.setText(model.getEmail());
        holder.phone.setText("+48" + model.getPhone());
        if(model.getPimage() == null){
            name.add(null);
        }else {
            Glide.with(holder.img.getContext()).load(model.getPimage()).into(holder.img);
            name.add(model.getPimage());
        }
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view, parent, holder.phone, holder.email, model.getName() + " " + model.getSurname(), holder.getAdapterPosition());
            }
        });

    }

    @NonNull
    @Override
    public myviewrec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowrec, parent, false);
        this.parent = parent;
        myviewrec viewHolder = new myviewrec(view);


        return viewHolder;
    }

    class myviewrec extends RecyclerView.ViewHolder{

        private CircleImageView img;
        public TextView namesur, phone, email;
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

    public void ShowPopup(View v, ViewGroup parent, TextView t1, TextView t2, String fullname, int i1){
        Dialog mDialog;
        mDialog = new Dialog(parent.getContext());
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.popupcontactadmin);
        ImageView call = mDialog.findViewById(R.id.call);
        ImageView mail = mDialog.findViewById(R.id.sendmail);
        TextView ns = mDialog.findViewById(R.id.namesurname);
        CircleImageView profpopup = mDialog.findViewById(R.id.photoprofilepopup);
        String emailmail = String.valueOf(t2.getText());
        String phonephon = String.valueOf(t1.getText());
        String showname = fullname;
        String linkprofil = name.get(i1);

        ns.setText(showname);

        if(linkprofil != null){
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(1)
                    .cornerRadiusDp(100)
                    .oval(true)
                    .build();

            Picasso.get()
                    .load(linkprofil)
                    .fit()
                    .transform(transformation)
                    .into(profpopup);
        }

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + emailmail));
                parent.getContext().startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("tel:" + phonephon));
                parent.getContext().startActivity(intent);
            }
        });
        mDialog.show();
    }
}
