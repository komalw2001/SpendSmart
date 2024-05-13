package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myProfile extends AppCompatActivity {

    TextView fullName;
    TextView joinDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        init();
    }

    protected void init()
    {
        fullName = findViewById(R.id.fullName);
        joinDate = findViewById(R.id.userSince);

        SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
        String sessionUser = sPref.getString("session_user", ""); // Provide a default value in case "session_user" is not found

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.child(sessionUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String fullNameFromDB = dataSnapshot.child("email").getValue(String.class);

                    fullName.setText("Full Name: " +fullNameFromDB);
                    //joinDate.setText(joinDateFromDB);
                } else {
                    fullName.setText("Loading...");
                    joinDate.setText("Loading...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur
                Log.e("Firebase", "Error fetching user data", databaseError.toException());
                Toast.makeText(myProfile.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}