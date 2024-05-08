package com.example.spendsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSplashScreen();
    }
    protected void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                if (sPref.getBoolean("logged_in",false) == true)
                {
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }

            }
        }, 1000); // Delay in milliseconds, adjust as needed
    }

}