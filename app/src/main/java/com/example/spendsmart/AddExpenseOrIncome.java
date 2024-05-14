package com.example.spendsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddExpenseOrIncome extends AppCompatActivity {

    Spinner spCat;
    ImageView ivAddCancel;
    EditText etAmount;
    TextView tvExpenses, tvIncome;
    Button btnAddConfirm;

    ArrayList<Category> expCategories;
    ArrayList<Category> incCategories;

    CategoryAdapter adapter;

    int currentSelection; // 1=expense,2=income

    DatabaseReference dbRef;

    int selectedCategoryIndex;

    double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense_or_income);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbRef = FirebaseDatabase.getInstance().getReference();

        etAmount = findViewById(R.id.edAmount);
        tvExpenses = findViewById(R.id.tvExpenses);
        tvIncome = findViewById(R.id.tvIncome);
        ivAddCancel =findViewById(R.id.btnCancelAdd);
        btnAddConfirm = findViewById(R.id.btnAddConfirm);

        currentSelection=1;
        selectedCategoryIndex=0;

        spCat = findViewById(R.id.spCat);
        expCategories = Categories.expenseCategories;
        incCategories = Categories.incomeCategories;

        adapter = new CategoryAdapter(this,expCategories);
        spCat.setAdapter(adapter);

        tvIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = 2;
                selectedCategoryIndex=0;

                etAmount.setText("");

                if (!isNightModeActive()) {
                    tvIncome.setTextColor(getResources().getColor(R.color.main_color));
                    tvIncome.setBackgroundColor(getResources().getColor(R.color.white));
                    tvExpenses.setBackgroundColor(getResources().getColor(R.color.main_color));
                    tvExpenses.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    tvIncome.setTextColor(getResources().getColor(R.color.main_color_night));
                    tvIncome.setBackgroundColor(getResources().getColor(R.color.black));
                    tvExpenses.setBackgroundColor(getResources().getColor(R.color.main_color_night));
                    tvExpenses.setTextColor(getResources().getColor(R.color.black));
                }


                adapter = new CategoryAdapter(getBaseContext(),incCategories);
                spCat.setAdapter(adapter);
            }
        });

        tvExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelection = 1;
                selectedCategoryIndex=0;

                etAmount.setText("");

                if (!isNightModeActive()) {
                    tvExpenses.setTextColor(getResources().getColor(R.color.main_color));
                    tvExpenses.setBackgroundColor(getResources().getColor(R.color.white));
                    tvIncome.setBackgroundColor(getResources().getColor(R.color.main_color));
                    tvIncome.setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    tvExpenses.setTextColor(getResources().getColor(R.color.main_color_night));
                    tvExpenses.setBackgroundColor(getResources().getColor(R.color.black));
                    tvIncome.setBackgroundColor(getResources().getColor(R.color.main_color_night));
                    tvIncome.setTextColor(getResources().getColor(R.color.black));
                }


                adapter = new CategoryAdapter(getBaseContext(),expCategories);
                spCat.setAdapter(adapter);
            }
        });

        ivAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddExpenseOrIncome.this,DashboardActivity.class));
                finish();
            }
        });

        btnAddConfirm.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String amt = etAmount.getText().toString().trim();
                if (amt.isEmpty()){
                    Toast.makeText(getBaseContext(),"Please enter an amount.",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sp = getSharedPreferences("user_info",MODE_PRIVATE);

                    String user = sp.getString("session_user",null);

                    if(user ==null){
                        Toast.makeText(getBaseContext(),"Please login again.",Toast.LENGTH_SHORT).show();
                    }


                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String formattedDate = dateFormat.format(currentDate);


                    HashMap<Object, Object> data = new HashMap<>();
                    data.put("user",user);
                    data.put("amount",Double.parseDouble(amt));
                    data.put("category",selectedCategoryIndex);
                    data.put("date",formattedDate);



                    dbRef.child("Users")
                            .child(user)
                            .child("balance")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String tableName;
                                        balance = snapshot.getValue(Double.class);

                                        if (currentSelection == 1){

                                            balance -= Double.parseDouble(amt);

                                            // REMINDER IF EXPENSE > BALANCE

                                            if (balance < 0) {

                                                Calendar calendar = Calendar.getInstance();
                                                Date currentDate = calendar.getTime();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                String formattedDate = dateFormat.format(currentDate);

                                                HashMap<Object, Object> data = new HashMap<>();
                                                data.put("type", 1);
                                                data.put("user", user);
                                                data.put("title", "Balance Exceeded!");
                                                data.put("text", "You have exceeded your current balance!");
                                                data.put("date", formattedDate);

                                                dbRef.child("Reminders")
                                                        .child(user)
                                                        .push()
                                                        .setValue(data)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(AddExpenseOrIncome.this, "Balance exceeded!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }

                                            tableName = "Expenses";
                                        }
                                        else {
                                            balance += Double.parseDouble(amt);
                                            tableName = "Incomes";
                                        }

                                        dbRef
                                                .child(tableName)
                                                .push()
                                                .setValue(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        // UPDATE USER BALANCE
                                                        dbRef.child("Users")
                                                                .child(user)
                                                                .child("balance")
                                                                .setValue(balance)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Log.d("Updating Balance","Successfully updated");
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("Updating Balance","Successfully updated");
                                                                    }
                                                                });

                                                        Toast.makeText(getBaseContext(),"Added Successfully!",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AddExpenseOrIncome.this,DashboardActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getBaseContext(),"Something went wrong, please try again.",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AddExpenseOrIncome.this,DashboardActivity.class));
                                                        finish();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(AddExpenseOrIncome.this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AddExpenseOrIncome.this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
        });

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCategoryIndex = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryIndex = 0;
            }

        });


    }


    private boolean isNightModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}