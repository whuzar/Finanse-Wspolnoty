package com.example.a6;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ThirdFragment extends Fragment{

    private SwipeRefreshLayout refreshLayout;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";
    private static final String KEY_NUMBER = "number";
    private String who;
    Dialog mDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third,
                container, false);

        mDialog = new Dialog(getActivity());

        refreshLayout = rootView.findViewById(R.id.refreshLayout3);

        RelativeLayout zarzadcaclick = rootView.findViewById(R.id.zarzadcaclick);
        RelativeLayout logouttologin = rootView.findViewById(R.id.logouttologin);
        RelativeLayout changepwd = rootView.findViewById(R.id.changepasswd);
        RelativeLayout aboutyou = rootView.findViewById(R.id.aboutyou);
        RelativeLayout contacta = rootView.findViewById(R.id.contactwithadmin);
        RelativeLayout userList = rootView.findViewById(R.id.userlist);
        RelativeLayout deleteacc = rootView.findViewById(R.id.delet);
        RelativeLayout breaks = rootView.findViewById(R.id.report);

        Button editprofileb = rootView.findViewById(R.id.btneditprofile);

        TextView number_telephone = rootView.findViewById(R.id.number_inne);
        TextView login_underphoto = rootView.findViewById(R.id.login_inne);

        ImageView profilepohoto = rootView.findViewById(R.id.photoprofile);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        who = sharedPreferences.getString(KEY_LOGED, null);
        if(who.equals("user")){
            zarzadcaclick.setVisibility(View.GONE);
        }
        refresh(login_underphoto, number_telephone, profilepohoto);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(login_underphoto, number_telephone, profilepohoto);
                refreshLayout.setRefreshing(false);
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
               startActivity(new Intent(getActivity(), EditPhotoPicture.class));
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

        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserList.class));
            }
        });

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDelete(view);
            }
        });

        breaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Breaks.class));
            }
        });

        return rootView;
    }
    public void ShowDelete(View v){
        mDialog.setContentView(R.layout.popupdeleteacc);
        Button btnexit = mDialog.findViewById(R.id.back);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        Button btndel = mDialog.findViewById(R.id.delt);

        EditText loginy = mDialog.findViewById(R.id.yourlogin);
        String login = sharedPreferences.getString(KEY_LOGIN, null);

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ly = loginy.getText().toString();
                if(login.equals(ly)){

                    DatabaseReference uid = databaseReference.child(who).child(login);
                    uid.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot remov: dataSnapshot.getChildren()) {
                                remov.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                    Toast.makeText(getActivity(), "Usunięto użytkownika", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_LOGIN, null);
                    editor.apply();
                    startActivity(new Intent(getActivity(), Login.class));
                }else{
                    Toast.makeText(getActivity(), "Nie poprawny login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDialog.show();
    }

    private void refresh(TextView login_underphoto, TextView number_telephone, ImageView profilepohoto) {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String numberphone = sharedPreferences.getString(KEY_NUMBER, null);


        if(login != null || numberphone != null) {
            login_underphoto.setText(login);
            number_telephone.setText("+48" + numberphone);
        }

        DatabaseReference uidRef = databaseReference.child(who).child(login);
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String picprof = task.getResult().child("pimage").getValue(String.class);

                        if(picprof != null){
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
            }
        });
    }

    public void Deleteacc(View v){

    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), adminInne.class);
        startActivity(intent);
    }
}