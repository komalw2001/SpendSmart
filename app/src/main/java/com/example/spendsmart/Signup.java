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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                // checks lagane hain !!!!!!!!!! - password match,empty fields,username alr exists

                HashMap<Object, Object> data = new HashMap<>();
                data.put("email", email);
                data.put("password", password);


                reference.child("Users")
                        .child(email) // Using the email as the key
                        .setValue(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Signup.this, "Successfully created account!", Toast.LENGTH_SHORT).show();

                                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sPref.edit();
                                editor.putString("session_user", email);

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
        });
    }
}