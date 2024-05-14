package com.example.spendsmart;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GoalsFragment extends Fragment {

    Context context;

    int selectedCategoryIndex = 0;
    RecyclerView rv;
    GoalAdapter goalAdapter;
    Button addNew;

    Spinner spCat;

    ImageButton editGoal;

    public GoalsFragment(Context c) {
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
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvGoals);
        addNew = view.findViewById(R.id.createnewgoal);
        editGoal = view.findViewById(R.id.editGoal);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                AlertDialog.Builder addContact = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.activity_create_goal, null, false);


                EditText etName = view.findViewById(R.id.goalAmount);

                ArrayList<Category> goalCategories;
                CategoryAdapter adapter;
                goalCategories = Categories.goalCategories;
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
                addContact.setTitle("Create a New Goal");
                addContact.setView(view);

                addContact.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.goalAmount);
                        String gAmt = editText.getText().toString().trim();
                        if (gAmt.isEmpty()) {
                            return;
                        }


                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("goalName", Categories.goalCategories.get(selectedCategoryIndex).getName());
                        int amt = Integer.valueOf(editText.getText().toString());

                        data.put("totalGoal", amt);

                        data.put("goalAchieved", 0);

                        SharedPreferences sPref = requireContext().getSharedPreferences("user_info", MODE_PRIVATE);
                        String uname = sPref.getString("session_user","");
                        data.put("user",uname);

                        // Insert data into the "Goals" table
                        reference.child("Goals").push().setValue(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data insertion successful
                                        Toast.makeText(context, "New goal created successfully!", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Data inserted successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Data insertion failed
                                        Toast.makeText(context, "Error creating goal!", Toast.LENGTH_SHORT).show();
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



        Query query = FirebaseDatabase.getInstance().getReference().child("Goals");
        FirebaseRecyclerOptions<Goal> options = new FirebaseRecyclerOptions.Builder<Goal>().setQuery(query,Goal.class).build();
        goalAdapter = new GoalAdapter(options,context);

        rv.setLayoutManager(new LinearLayoutManager(getContext())); // Set LayoutManager if not set in XML
        rv.setAdapter(goalAdapter);


        Query query1 = FirebaseDatabase.getInstance().getReference().child("Goals");
        FirebaseRecyclerOptions<Goal> options1 = new FirebaseRecyclerOptions.Builder<Goal>().setQuery(query1,Goal.class).build();
        goalAdapter = new GoalAdapter(options1,context);

        rv.setLayoutManager(new LinearLayoutManager(getContext())); // Set LayoutManager if not set in XML
        rv.setAdapter(goalAdapter);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        goalAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        goalAdapter.stopListening();
    }
}