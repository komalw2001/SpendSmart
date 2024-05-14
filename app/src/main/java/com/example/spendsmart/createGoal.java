package com.example.spendsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class createGoal extends AppCompatActivity {

    Spinner spCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        init();

    }

    protected void init()
    {
        spCat = findViewById(R.id.spCat);


        Toast.makeText(this, "Setting cat adapter", Toast.LENGTH_SHORT).show();

    }
}