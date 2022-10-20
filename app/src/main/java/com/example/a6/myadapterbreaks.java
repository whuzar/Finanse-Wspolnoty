package com.example.a6;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

public class myadapterbreaks extends FirebaseRecyclerAdapter<modelbreaks, myadapterbreaks.myviewbreaks> {

    List<String> name=new ArrayList<String>();

    public myadapterbreaks(@NonNull FirebaseRecyclerOptions<modelbreaks> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myviewbreaks holder, int position, @NonNull modelbreaks model) {

        holder.idea.setText(model.getmybreak());

    }

    @NonNull
    @Override
    public myviewbreaks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowbreak, parent, false);
        myviewbreaks viewHolder = new myviewbreaks(view);

        viewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view, parent, viewHolder.idea);

            }
        });
        return viewHolder;
    }

    class myviewbreaks extends RecyclerView.ViewHolder{

        public TextView idea;
        LinearLayout line;

        public myviewbreaks(@NonNull View intemview){
            super(intemview);

            line = itemView.findViewById(R.id.allbreaks);
            idea = itemView.findViewById(R.id.mybreak);

        }
    }
    public void ShowPopup(View v, ViewGroup parent, TextView t1){
        Dialog mDialog;
        mDialog = new Dialog(parent.getContext());
        mDialog.setContentView(R.layout.popupcontactadmin);
        ImageView mail = mDialog.findViewById(R.id.sendmail);
        ImageView profpopup = mDialog.findViewById(R.id.photoprofilepopup);
        String emailmail = String.valueOf(t1.getText());

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + emailmail));
                parent.getContext().startActivity(intent);
            }
        });

        mDialog.show();
    }
}
