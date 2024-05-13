package com.example.spendsmart;

import android.content.Context;
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
import android.widget.Button;
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

import java.util.HashMap;


public class GoalsFragment extends Fragment {

    Context context;
    RecyclerView rv;
    GoalAdapter goalAdapter;

    Button addNew;

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


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast when addNew is clicked
                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                //Hardcoding some goals here!

                HashMap<Object, Object> data = new HashMap<>();
                data.put("goalName", "Accommodation");
                data.put("totalGoal", 70000);
                data.put("goalAchieved", 40000);

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
        Query query = FirebaseDatabase.getInstance().getReference().child("Goals");
        FirebaseRecyclerOptions<Goal> options = new FirebaseRecyclerOptions.Builder<Goal>().setQuery(query,Goal.class).build();
        goalAdapter = new GoalAdapter(options);
        // Set up the RecyclerView with the adapter
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