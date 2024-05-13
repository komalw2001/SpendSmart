package com.example.spendsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changePasswor extends AppCompatActivity {

    EditText pw,newpw,confirmnewpw;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwor);

        init();
    }

    protected void init()
    {
        pw = findViewById(R.id.currentpassword);
        newpw = findViewById(R.id.newpassword);
        confirmnewpw = findViewById(R.id.confirmnewpassword);
        btn = findViewById(R.id.changepasswordbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpw = pw.getText().toString().trim();
                String newPW = newpw.getText().toString().trim();
                String confirmnewPW = confirmnewpw.getText().toString().trim();

                if (oldpw.isEmpty() || newPW.isEmpty() || confirmnewPW.isEmpty())
                {
                    Toast.makeText(changePasswor.this, "One or more fields empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //password check length
                if (!newPW.equals(confirmnewPW))
                {
                    Toast.makeText(changePasswor.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPW.equals(oldpw))
                {
                    Toast.makeText(changePasswor.this, "New password cannot be the same as the old password!", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (newPW.length()<5)
                {
                    Toast.makeText(changePasswor.this, "Length of password must be at least 4 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sPref = getSharedPreferences("user_info", MODE_PRIVATE);
                String sessionUser = sPref.getString("session_user", ""); // Provide a default value in case "session_user" is not found

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                usersRef.child(sessionUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String dbPassword = dataSnapshot.child("password").getValue(String.class);
                            if (!dbPassword.equals(oldpw))
                            {
                                Toast.makeText(changePasswor.this, "Current password incorrect!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dataSnapshot.getRef().child("password").setValue(newPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(changePasswor.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(changePasswor.this,myProfile.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(changePasswor.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else
                        {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur
                        Log.e("Firebase", "Error fetching user data", databaseError.toException());
                        Toast.makeText(changePasswor.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}