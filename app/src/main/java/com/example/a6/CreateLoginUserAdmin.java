package com.example.a6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateLoginUserAdmin extends AppCompatActivity {

    private EditText name, username, email, phonenumber, sharesuser;
    private Button btnuser, btnadmin;

    Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login_user_admin);

        name = findViewById(R.id.nameuseroradmin);
        username = findViewById(R.id.usernameuseroradmin);
        email = findViewById(R.id.emailuseroradmin);
        phonenumber = findViewById(R.id.phoneuseroradmin);
        sharesuser = findViewById(R.id.sharesuseroradmin);

        mDialog = new Dialog(this);

//        final String nameau = name.getText().toString();
//        final String usernameau = username.getText().toString();
//        final String mailau = email.getText().toString();
//        final String phoneau = phonenumber.getText().toString();
//        final String sharesau = sharesuser.getText().toString();

    }
    public void ShowPopup(View v){
        mDialog.setContentView(R.layout.popupcreateua);
        Button btnexit = mDialog.findViewById(R.id.backtocreateuser);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void ShowPopup2(View v){
        mDialog.setContentView(R.layout.popupcreateua2);
        Button btnexit = mDialog.findViewById(R.id.backtocreateadmin);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void CreateAdminAccept(View v){
        mDialog.setContentView(R.layout.popupcreateua2);
        Button btnadmin = mDialog.findViewById(R.id.createadminyes);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateLoginUserAdmin.this, "Zapisano admina", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void CreateUserAccept(View v){
        mDialog.setContentView(R.layout.popupcreateua);
        Button btnuser = mDialog.findViewById(R.id.createuseryes);
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateLoginUserAdmin.this, "Zapisano usera", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

}