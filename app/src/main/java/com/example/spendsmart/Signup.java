package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    DatabaseReference reference;
    EditText etEmail,etPassword,etConfirmPassword;
    Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {
        reference = FirebaseDatabase.getInstance().getReference();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmpassword = etConfirmPassword.getText().toString().trim();

                if (!password.equals(confirmpassword)) {
                    Toast.makeText(Signup.this, "Password fields don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.contains(".") || email.contains("#") || email.contains("$") || email.contains("[") || email.contains("]"))
                {
                    Toast.makeText(Signup.this, "Invalid username format!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 5)
                {
                    Toast.makeText(Signup.this, "Password length needs to be at least 4 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }



                HashMap<Object, Object> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);

                reference.child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Toast.makeText(Signup.this, "Username already exists. Choose another email.", Toast.LENGTH_SHORT).show();
                            } else {

                                HashMap<Object, Object> data = new HashMap<>();
                                data.put("email", email);
                                data.put("password", password);
                                data.put("balance", 0);

                                reference.child("Users")
                                        .child(email)
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Signup.this, "Successfully created account!", Toast.LENGTH_SHORT).show();

                                                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sPref.edit();
                                                editor.putString("session_user", email);

                                                editor.putBoolean("logged_in",true);
                                                editor.apply();
                                                Intent intent = new Intent(Signup.this, DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Signup.this, "Something went wrong.. Please try again."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Signup.this, "Database error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });
    }
}