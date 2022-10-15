package com.example.a6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Calendar;

public class FirstFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private TextView timehello;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first,
                container, false);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        timehello = rootView.findViewById(R.id.texthello);

        showtime();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showtime();
                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
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
}