package com.example.a6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FirstFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LOGED = "wloged";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private SwipeRefreshLayout refreshLayout;
    private TextView timehello, moneytextset, ideamoneyset, themeidea, countertime;
    private EditText typeidea;
    private LinearLayout linearLayoutonoff, linearLayoutshow, linearshowbutton;
    private Button btnsendidea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first,
                container, false);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);

        timehello = rootView.findViewById(R.id.texthello);
        moneytextset = rootView.findViewById(R.id.setideamoney);
        ideamoneyset = rootView.findViewById(R.id.textmoney);
        themeidea = rootView.findViewById(R.id.setideatheme);
        countertime = rootView.findViewById(R.id.counterdowntime);

        linearLayoutshow = rootView.findViewById(R.id.ideaonoff);
        linearLayoutonoff = rootView.findViewById(R.id.ideashownothing);
        linearshowbutton = rootView.findViewById(R.id.buttonlinearidea);

        showtime();

        int minute=Integer.parseInt("2880");
        long min= minute*60*1000;
        counter(min);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showtime();
                shownoteidea();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void shownoteidea() {
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String team = task.getResult().child(who).child(login).child("team").getValue(String.class);

                        String money = task.getResult().child("wspolnota").child(team).child("typeidea").child("money").getValue(String.class);
                        String theme = task.getResult().child("wspolnota").child(team).child("typeidea").child("themevote").getValue(String.class);

                        if(money.equals("0")){
                            moneytextset.setVisibility(View.GONE);
                            ideamoneyset.setVisibility(View.GONE);
                        }else{
                            moneytextset.setText(money);
                        }

                        if(theme.equals("")){
                            linearLayoutonoff.setVisibility(View.GONE);
                            linearLayoutshow.setVisibility(View.VISIBLE);
                            linearshowbutton.setVisibility(View.GONE);
                        }
//                        String name = task.getResult().child("wspolnota").child(team).child("notice").child("sendby").getValue(String.class);

                        themeidea.setText(theme);
                    }
                }
            }
        });
    }

    public void showtime(){
        Calendar c = Calendar.getInstance();
        int hr1 = c.get(Calendar.HOUR_OF_DAY);

        if (hr1 >= 6 && hr1 < 18){
            timehello.setText("Dzień dobry");
        }else{
            timehello.setText("Dobry wieczór");
        }
    }

    private void counter(long min) {
        CountDownTimer timer = new CountDownTimer(min, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                int days = (int) ((millisUntilFinished / (1000 * 60 * 60)) / 24);
                countertime.setText(String.format("%d:%d:%d:%d", days, hours, minutes, seconds));
            }
            public void onFinish() {
                Toast.makeText(getActivity(), "koniec", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }
}