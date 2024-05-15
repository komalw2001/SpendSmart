package com.example.spendsmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class appthemesettings extends AppCompatActivity {

    ImageButton backbtn;
    RadioGroup themeRadioGroup;
    RadioButton lightModeRadioButton, darkModeRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appthemesettings);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appthemesettings.this,Settings.class);
                startActivity(intent);
            }
        });

        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        lightModeRadioButton = findViewById(R.id.lightModeRadioButton);
        darkModeRadioButton = findViewById(R.id.darkModeRadioButton);

        // Check the current theme mode and set the appropriate radio button
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            lightModeRadioButton.setChecked(true);
        } else if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeRadioButton.setChecked(true);
        }

        // Set the listener for radio button clicks
        themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lightModeRadioButton) {
                    // Set the app theme to light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (checkedId == R.id.darkModeRadioButton) {
                    // Set the app theme to dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate(); // Recreate the activity with the new theme
            }
        });

    }
}