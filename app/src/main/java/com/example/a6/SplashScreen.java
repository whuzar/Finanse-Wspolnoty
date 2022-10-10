package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//        while (!checkInternet()){
//
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Intent intent = new Intent(SplashScreen.this, Login.class);
               startActivity(intent);
               finish();
            }
        }, 3000);

    }


}