package com.example.a6;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    myadapteridea adapter2;

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
    private CountDownTimer timer;
    long diff, diffold, oldLong, NewLong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first,
                container, false);

        recyclerView = rootView.findViewById(R.id.recycleshowideas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);

//        DatabaseReference uidRef = databaseReference.child(who).child(login);
//        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DataSnapshot ds : task.getResult().getChildren()) {
//                        String team = task.getResult().child("team").getValue(String.class);

//                        FirebaseRecyclerOptions<modelidea> options = new FirebaseRecyclerOptions
//                                .Builder<modelidea>()
//                                .setQuery(FirebaseDatabase.getInstance().getReference().child("wspolnota").child("138274").child("createdpoll"), modelidea.class)
//                                .build();
////
////                            FirebaseRecyclerOptions<modelidea> options = new FirebaseRecyclerOptions
////                                    .Builder<modelidea>()
////                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("admin"), modelidea.class)
////                                    .build();
//
//                        adapter2 = new myadapteridea(options);
//                        recyclerView.setAdapter(adapter2);
//                        adapter2.startListening();
//                    }
//                }
//            }
//        });

        refreshLayout = rootView.findViewById(R.id.refreshLayout);

        timehello = rootView.findViewById(R.id.texthello);
        moneytextset = rootView.findViewById(R.id.setideamoney);
        ideamoneyset = rootView.findViewById(R.id.textmoney);
        themeidea = rootView.findViewById(R.id.setideatheme);
        countertime = rootView.findViewById(R.id.counterdowntime);

        linearLayoutshow = rootView.findViewById(R.id.ideaonoff);
        linearLayoutonoff = rootView.findViewById(R.id.ideashownothing);
        linearshowbutton = rootView.findViewById(R.id.buttonlinearidea);

        btnsendidea = rootView.findViewById(R.id.btnsendidea);

        typeidea = rootView.findViewById(R.id.youridea);

        showtime();
        shownoteidea();
        getdata();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showtime();
                shownoteidea();
                getdata();
                refreshLayout.setRefreshing(false);
            }
        });

        linearLayoutonoff.setVisibility(View.VISIBLE);
        linearLayoutshow.setVisibility(View.GONE);
        linearshowbutton.setVisibility(View.GONE);

        btnsendidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gv = typeidea.getText().toString();

                if(!gv.equals("")) {

                    DatabaseReference textRef = databaseReference.child(who).child(login);
                    textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                databaseReference.child(who).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child(who).child(login).child("ideas").child("idea").setValue(gv);
                                        databaseReference.child(who).child(login).child("ideas").child("send").setValue("true");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                typeidea.setEnabled(false);
                                btnsendidea.setText("Już wpisałeś swój pomysł");
                                btnsendidea.setEnabled(false);
                                Toast.makeText(getActivity(), "Pomysł został wysłany pomyślnie", Toast.LENGTH_SHORT).show();
                                typeidea.setText("");
                            } else {
                                Log.d("TAG", task.getException().getMessage());
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Pole nie może być puste", Toast.LENGTH_SHORT).show();
                }
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
                        String day = task.getResult().child("wspolnota").child(team).child("typeidea").child("day").getValue(String.class);
                        String month = task.getResult().child("wspolnota").child(team).child("typeidea").child("month").getValue(String.class);
                        String year = task.getResult().child("wspolnota").child(team).child("typeidea").child("year").getValue(String.class);

                        if(money != null && theme != null){
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
                            }else {
                                linearLayoutonoff.setVisibility(View.GONE);
                                linearLayoutshow.setVisibility(View.VISIBLE);
                                linearshowbutton.setVisibility(View.VISIBLE);
                            }
                            themeidea.setText(theme);

                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
                            int yr = calendar.get(Calendar.YEAR);
                            int mh = calendar.get(Calendar.MONTH);
                            int dom = calendar.get(Calendar.DAY_OF_MONTH);

                            int hr = calendar.get(Calendar.HOUR_OF_DAY);
                            int mm = calendar.get(Calendar.MINUTE);
                            int sc = calendar.get(Calendar.SECOND);

                            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
                            String oldTime = dom + "." + mh + "." + yr + ", " + hr + ":" + mm + ":" + sc;//Timer date 1
                            String NewTime = day + "." + month + "." + year + ", 23:59:59";//Timer date 2
                            Date oldDate, newDate;
                            try {
                                oldDate = formatter.parse(oldTime);
                                newDate = formatter.parse(NewTime);
                                oldLong = oldDate.getTime();
                                NewLong = newDate.getTime();
                                diff = NewLong - oldLong;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(timer == null){
                                counter(diff);
                                diffold = diff;
                            }
                            if (diffold != diff){
                                timer.cancel();
                                counter(diff);
                                diffold = diff;
                            }
                        }
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
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);
        timer = new CountDownTimer(min, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                @SuppressLint("DefaultLocale") String hms = (String.format("%02d", TimeUnit.MILLISECONDS.toDays(millis))) + ":"
                        + (String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))) + ":")
                        + (String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) + ":"
                        + (String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))));
                countertime.setText(/*context.getString(R.string.ends_in) + " " +*/ hms);
            }
            public void onFinish() {
                countertime.setText("Czas upłynął");
                DatabaseReference uidRef = databaseReference;
                uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                String team = task.getResult().child(who).child(login).child("team").getValue(String.class);
                                DatabaseReference uid = databaseReference.child("wspolnota").child(team).child("typeidea");
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
                            }
                        }
                    }
                });
                startActivity(new Intent(getActivity(), FirstFragment.class));
            }
        };
        timer.start();
    }

    public void getdata(){
        String login = sharedPreferences.getString(KEY_LOGIN, null);
        String who = sharedPreferences.getString(KEY_LOGED, null);
        DatabaseReference uidRef = databaseReference;
        uidRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        String check = task.getResult().child(who).child(login).child("ideas").child("send").getValue(String.class);
                        if(Objects.equals(check, "true")){
                            typeidea.setEnabled(false);
                            btnsendidea.setText("Już wpisałeś swój pomysł");
                            btnsendidea.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onResume();
    }
//    @Override
//    public void onStart(){
//        super.onStart();
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        adapter2.stopListening();
//    }


}