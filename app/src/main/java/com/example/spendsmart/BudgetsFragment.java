package com.example.spendsmart;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BudgetsFragment extends Fragment {

    Context context;

    int selectedCategoryIndex = 0;
    RecyclerView rv;
    BudgetAdapter budgetAdapter;
    Button addNew;

    Spinner spCat;

    ImageButton editGoal;
    public BudgetsFragment(Context c) {
        context = c;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budgets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvBudgets);
        addNew = view.findViewById(R.id.createnewgoal);
        editGoal = view.findViewById(R.id.editGoal);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                AlertDialog.Builder addContact = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.activity_createbudget, null, false);




                ArrayList<Category> goalCategories;
                CategoryAdapter adapter;
                goalCategories = Categories.expenseCategories;

                adapter = new CategoryAdapter(context,goalCategories);

                Spinner spinner = view.findViewById(R.id.spCat);
                spinner.setAdapter(adapter);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        int selectedIndex = position;
                        selectedCategoryIndex = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Handle no selection
                    }
                });
                addContact.setTitle("Create a New Budget");
                addContact.setView(view);

                addContact.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.budgetAmount);
                        String gAmt = editText.getText().toString().trim();
                        if (gAmt.isEmpty()) {
                            return;
                        }


                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("budgetName", Categories.expenseCategories.get(selectedCategoryIndex).getName());
                        int amt = Integer.valueOf(editText.getText().toString());
                        if (amt  <= 0)
                        {
                            Toast.makeText(context, "Enter a valid amount!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        data.put("totalBudget", amt);
                       data.put("categoryIndex",selectedCategoryIndex);
                        data.put("totalSpent", 0);
                        String budgetID = UUID.randomUUID().toString();
                        data.put("budgetID",budgetID);
                        SharedPreferences sPref = requireContext().getSharedPreferences("user_info", MODE_PRIVATE);
                        String uname = sPref.getString("session_user","");
                        data.put("user",uname);


                        reference.child("Budget").push().setValue(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data insertion successful
                                        Toast.makeText(context, "New budget created successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Data inserted successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Data insertion failed
                                        Toast.makeText(context, "Error creating budget!", Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase", "Error inserting data: " + e.getMessage());
                                    }
                                });
                    }
                });

                addContact.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancellation
                    }
                });

                addContact.show();
            }
        });




        SharedPreferences sPref = requireContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String uname = sPref.getString("session_user","");

        Query query = FirebaseDatabase.getInstance().getReference().child("Budget").orderByChild("user").equalTo(uname);;
        FirebaseRecyclerOptions<Budget> options = new FirebaseRecyclerOptions.Builder<Budget>().setQuery(query,Budget.class).build();
        budgetAdapter = new BudgetAdapter(options,context);

        rv.setLayoutManager(new LinearLayoutManager(getContext())); // Set LayoutManager if not set in XML
        rv.setAdapter(budgetAdapter);



    }

    @Override
    public void onStart()
    {
        super.onStart();
        budgetAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        budgetAdapter.stopListening();
    }
}