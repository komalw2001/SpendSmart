package com.example.spendsmart;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BudgetAdapter extends FirebaseRecyclerAdapter<Budget,BudgetAdapter.BudgetAdapterViewHolder> {

    Context context;
    public BudgetAdapter(@NonNull FirebaseRecyclerOptions<Budget> options, Context c) {
        super(options);
        this.context = c;
    }



    @NonNull
    @Override
    public BudgetAdapter.BudgetAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Budget Adapter", "Budget Adapter ON CREATE View Holder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_singlebudget,parent,false);
        return new BudgetAdapter.BudgetAdapterViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull BudgetAdapterViewHolder holder, int position, @NonNull Budget model) {
        Log.d("Budget Adapter", "Budget Adapter onBindView Holder");

        holder.gAmount.setText(model.getTotalBudget() + " PKR");
        holder.gname.setText(model.getBudgetName());

        double percent = (double) model.getTotalSpent() /model.getTotalBudget();
        //Toast.makeText(context, percent + "", Toast.LENGTH_SHORT).show();
        percent = percent * 100;
        int p = (int) Math.ceil(percent);
        holder.progresbar.setProgress(p);

        if (percent>100) {
            holder.gCompletion.setText("Status: Budget exceeded");
            //set color red here
            holder.progresbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
        else
            holder.gCompletion.setText("Status: Within Budget");

        holder.editGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sPref = context.getSharedPreferences("user_info", MODE_PRIVATE);
                String uname = sPref.getString("session_user","");

                HashMap<Object, Object> data = new HashMap<>();
                data.put("user", uname);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Budget");
                Query userGoalQuery = reference.orderByChild("user").equalTo(uname);

                userGoalQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Budget goal = snapshot.getValue(Budget.class);
                            if (goal != null && goal.getBudgetID().equals(model.getBudgetID())) { // Check if the goal name matches

                                snapshot.getRef().removeValue();

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled event
                    }
                });

            }
        });
    }

    public class BudgetAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView gname,gAmount,gCompletion;

        ProgressBar progresbar;
        Spinner spCat;

        ImageButton editGoal;

        BudgetAdapterViewHolder(@NonNull View itemview)
        {
            super(itemview);
            gname = itemview.findViewById(R.id.goalName);
            gAmount = itemView.findViewById(R.id.goalAmount);
            spCat = itemview.findViewById(R.id.spCat);
            gCompletion = itemview.findViewById(R.id.completionStatus);
            progresbar = itemview.findViewById(R.id.progressBar);

            editGoal= itemview.findViewById(R.id.editGoal);

            Log.d("Budget Adapter", "Budget Adapter View Holder");
        }
    }
}
