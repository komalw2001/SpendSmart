package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    DatabaseReference reference;
    Button loginbtn;

    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


    }

    public void init()
    {
        TextView signUpTextView = findViewById(R.id.signUpTextView);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginbtn = findViewById(R.id.loginbtn);


        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect user to sign-up screen
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = email.getText().toString().trim();
                String pw = password.getText().toString().trim();

                if (uname.isEmpty() || pw.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uname.contains(".") || uname.contains("#") || uname.contains("$") || uname.contains("[") || uname.contains("]"))
                {
                    Toast.makeText(Login.this, "Invalid username format!", Toast.LENGTH_SHORT).show();
                    return;
                }
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(uname).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {

                            String storedPassword = dataSnapshot.child("password").getValue(String.class);

                            if (storedPassword.equals(pw)) {
                                Toast.makeText(Login.this,"Login successful!", Toast.LENGTH_LONG).show();

                                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sPref.edit();
                                editor.putString("session_user", uname);
                                editor.putBoolean("logged_in",true);
                                editor.apply();


                                Intent intent = new Intent(Login.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Login.this, "Login failed!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(Login.this,"Login failed!", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Login.this, "Database error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}