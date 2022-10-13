package com.example.a6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThirdFragment extends Fragment{

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NUMBER = "number";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third,
                container, false);

        RelativeLayout zarzadcaclick = (RelativeLayout) rootView.findViewById(R.id.zarzadcaclick);
        RelativeLayout logouttologin = (RelativeLayout) rootView.findViewById(R.id.logouttologin);
        RelativeLayout changepwd = (RelativeLayout) rootView.findViewById(R.id.changepasswd);
        Button editprofileb = (Button) rootView.findViewById(R.id.btneditprofile);
        TextView number_telephone = (TextView) rootView.findViewById(R.id.number_inne);
        TextView login_underphoto = (TextView) rootView.findViewById(R.id.login_inne);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);


        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String numberphone = sharedPreferences.getString(KEY_NUMBER, null);

        if(login != null || numberphone != null) {
            login_underphoto.setText(login);
            number_telephone.setText("+48" + numberphone);
        }

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

        return rootView;
    }
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), adminInne.class);
        startActivity(intent);
    }
}