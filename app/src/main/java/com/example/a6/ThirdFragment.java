package com.example.a6;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThirdFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third,
                container, false);
        RelativeLayout button = (RelativeLayout) rootView.findViewById(R.id.zarzadcaclick);
        TextView number_telephone = (TextView) rootView.findViewById(R.id.number_inne);
        TextView login_underphoto = (TextView) rootView.findViewById(R.id.login_inne);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });

        number_telephone.setText("jo");
        login_underphoto.setText("whuzar");



        return rootView;
    }
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), adminInne.class);
        startActivity(intent);
    }
}