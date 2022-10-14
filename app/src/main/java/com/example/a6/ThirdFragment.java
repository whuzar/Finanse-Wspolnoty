package com.example.a6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;

public class ThirdFragment extends Fragment{

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_PHOTO = "photo";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginres-5779b-default-rtdb.firebaseio.com/");

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third,
                container, false);

        RelativeLayout zarzadcaclick = (RelativeLayout) rootView.findViewById(R.id.zarzadcaclick);
        RelativeLayout logouttologin = (RelativeLayout) rootView.findViewById(R.id.logouttologin);
        RelativeLayout changepwd = (RelativeLayout) rootView.findViewById(R.id.changepasswd);
        RelativeLayout aboutyou = (RelativeLayout) rootView.findViewById(R.id.aboutyou);
        RelativeLayout contacta = (RelativeLayout) rootView.findViewById(R.id.contactwithadmin);

        Button editprofileb = (Button) rootView.findViewById(R.id.btneditprofile);

        TextView number_telephone = (TextView) rootView.findViewById(R.id.number_inne);
        TextView login_underphoto = (TextView) rootView.findViewById(R.id.login_inne);

        ImageView profilepohoto = (ImageView) rootView.findViewById(R.id.photoprofile);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String numberphone = sharedPreferences.getString(KEY_NUMBER, null);

        if(login != null || numberphone != null) {
            login_underphoto.setText(login);
            number_telephone.setText("+48" + numberphone);
        }

        DatabaseReference uidRef = databaseReference.child("admin").child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String picprof = task.getResult().child("pimage").getValue(String.class);

                        Transformation transformation = new RoundedTransformationBuilder()
                                .borderColor(Color.WHITE)
                                .borderWidthDp(1)
                                .cornerRadiusDp(100)
                                .oval(true)
                                .build();

                        Picasso.get()
                                .load(picprof)
                                .fit()
                                .transform(transformation)
                                .into(profilepohoto);
                    }
                }
            }
        });

       zarzadcaclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });

       logouttologin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.clear();
               editor.commit();
               startActivity(new Intent(getActivity(), Login.class));
               getActivity().finish();
           }
       });

       editprofileb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getActivity(), editprofile.class));
           }
       });

        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        });

        aboutyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutYou.class));
            }
        });

        contacta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ContactToAdmin.class));
            }
        });

        return rootView;
    }
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), adminInne.class);
        startActivity(intent);
    }
}