package com.example.spendsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    TextView profile,password,logout;
    ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    protected void init()
    {
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,DashboardActivity.class);
                startActivity(intent);
            }
        });

        profile = findViewById(R.id.profileSettings);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,myProfile.class);
                startActivity(intent);
            }
        });

        password = findViewById(R.id.passwordsettings);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, changePasswor.class);
                startActivity(intent);
            }
        });



        logout = findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();


                editor.putBoolean("logged_in",false);
                editor.apply();

                Toast.makeText(Settings.this, "Logging out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);


            }
        });
    }
}