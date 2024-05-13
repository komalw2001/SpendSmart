package com.example.spendsmart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class GoalAdapter extends FirebaseRecyclerAdapter<Goal,GoalAdapter.GoalAdapterViewHolder> {

    public GoalAdapter(@NonNull FirebaseRecyclerOptions<Goal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalAdapterViewHolder holder, int position, @NonNull Goal model) {
        Log.e("Goal Adapter", "Goal Adapter onBindView Holder");
        holder.gAmount.setText(model.getTotalGoal() + "");
            holder.gname.setText(model.getGoalName());
    }

    @NonNull
    @Override
    public GoalAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Goal Adapter", "Goal Adapter ON CREATE View Holder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_goal,parent,false);
        return new GoalAdapterViewHolder(v);
    }

    public class GoalAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView gname,gAmount;

        GoalAdapterViewHolder(@NonNull View itemview)
        {
            super(itemview);
            gname = itemview.findViewById(R.id.goalName);
            gAmount = itemView.findViewById(R.id.goalAmount);
            Log.e("Goal Adapter", "Goal Adapter View Holder");

        }

    }
}
