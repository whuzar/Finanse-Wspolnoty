package com.example.a6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private SwipeRefreshLayout refreshLayout;
    private TextView themes, mes;
    private LinearLayout linearLayoutonoff, linearLayoutshow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second,
                container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        refreshLayout = rootView.findViewById(R.id.refreshLayout2);
        themes = rootView.findViewById(R.id.settheme);
        mes = rootView.findViewById(R.id.setmessage);
        linearLayoutonoff = rootView.findViewById(R.id.noticeonoff);
        linearLayoutshow = rootView.findViewById(R.id.noticeshownothing);

        shownotice();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shownotice();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void shownotice() {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String team = task.getResult().child(who).child(login).child("team").getValue(String.class);

                        String theme = task.getResult().child("wspolnota").child(team).child("notice").child("thememessage").getValue(String.class);
                        String message = task.getResult().child("wspolnota").child(team).child("notice").child("message").getValue(String.class);
                        if(theme.equals("") || message.equals("")){
                            linearLayoutonoff.setVisibility(View.GONE);
                            linearLayoutshow.setVisibility(View.VISIBLE);
                        }
//                        String name = task.getResult().child("wspolnota").child(team).child("notice").child("sendby").getValue(String.class);

                        themes.setText(theme);
                        mes.setText(message);

                    }
                }
            }
        });

    }
}