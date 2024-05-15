package com.example.spendsmart;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GoalAdapter extends FirebaseRecyclerAdapter<Goal,GoalAdapter.GoalAdapterViewHolder> {

    Context context;
    public GoalAdapter(@NonNull FirebaseRecyclerOptions<Goal> options, Context c) {
        super(options);
        this.context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull GoalAdapterViewHolder holder, int position, @NonNull Goal model) {
        Log.d("Goal Adapter", "Goal Adapter onBindView Holder");
        holder.gAmount.setText(model.getTotalGoal() + " PKR");
        holder.gname.setText(model.getGoalName());
        double percent = (double) model.getGoalAchieved() /model.getTotalGoal();

        percent = percent * 100;
        int p = (int) Math.ceil(percent);
        holder.progresbar.setProgress(p);

        holder.gCompletion.setText(p + "%" + " completed");


       holder.editGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder updateProgress = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.activity_updateprogress, null, false);

                updateProgress.setTitle("Update Goal Progress");
                updateProgress.setView(view);

                updateProgress.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.updatedAmount);

                        String gAmt = editText.getText().toString().trim();
                        if (gAmt.isEmpty()) {
                            Toast.makeText(context,"Enter updated value!",Toast.LENGTH_LONG).show();
                            return;
                        }
                        int amt = Integer.parseInt(gAmt);
                        if (amt < 0)
                        {
                            Toast.makeText(context,"Enter a valid amount!",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (amt > model.getTotalGoal())
                        {
                            Toast.makeText(context,"Cannot exceed goal!",Toast.LENGTH_LONG).show();
                            return;
                        }
                        SharedPreferences sPref = context.getSharedPreferences("user_info", MODE_PRIVATE);
                        String uname = sPref.getString("session_user","");

                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("user", uname);

                        data.put("goalAchieved", amt);


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Goals");
                        Query userGoalQuery = reference.orderByChild("user").equalTo(uname);

                        userGoalQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Goal goal = snapshot.getValue(Goal.class);
                                    if (goal != null && goal.getGoalID().equals(model.getGoalID()))
                                    {
                                        // Update the goalAchieved amount
                                        int newAchievedAmount = amt;
                                        snapshot.getRef().child("goalAchieved").setValue(newAchievedAmount);

                                        double percent = (double) amt /model.getTotalGoal();

                                        percent = percent * 100;
                                        int p = (int) Math.ceil(percent);
                                        holder.progresbar.setProgress(p);

                                        holder.gCompletion.setText(p + "%" + " completed");

                                        Log.d("GOALADAPTER","-BEFOREREMINDER");

                                        // REMINDER THAT GOAL EXCEEDED

                                        Calendar calendar = Calendar.getInstance();
                                        Date currentDate = calendar.getTime();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                        String formattedDate = dateFormat.format(currentDate);

                                       // Toast.makeText(context, "helllooo", Toast.LENGTH_SHORT).show();

                                        if (percent >= 100){
                                            //Toast.makeText(context, "% "+percent, Toast.LENGTH_SHORT).show();
                                            HashMap<Object, Object> data = new HashMap<>();
                                            data.put("type", 2);
                                            data.put("title", "Goal Reached!");
                                            data.put("text", "Goal reached: "+goal.getGoalName()+".");
                                            data.put("date", formattedDate);

                                            FirebaseDatabase.getInstance().getReference().child("Reminders")
                                                    .child(uname)
                                                    .push()
                                                    .setValue(data);
                                        }

                                        //////////////
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("GoalAdapter",databaseError.getMessage());
                            }
                        });
                    }
                });

                updateProgress.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancellation
                    }
                });

                updateProgress.show();
            }
        });

       holder.deleteGoal.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v)
           {
               SharedPreferences sPref = context.getSharedPreferences("user_info", MODE_PRIVATE);
               String uname = sPref.getString("session_user","");


               DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Goals");
               Query userGoalQuery = reference.orderByChild("user").equalTo(uname);

               userGoalQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                           Goal goal = snapshot.getValue(Goal.class);
                           if (goal != null && goal.getGoalID().equals(model.getGoalID())) {

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

    @NonNull
    @Override
    public GoalAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Goal Adapter", "Goal Adapter ON CREATE View Holder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_goal,parent,false);
        return new GoalAdapterViewHolder(v);
    }

    public class GoalAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView gname,gAmount,gCompletion;

        ProgressBar progresbar;
        Spinner spCat;

        ImageButton editGoal,deleteGoal;

        GoalAdapterViewHolder(@NonNull View itemview)
        {
            super(itemview);
            gname = itemview.findViewById(R.id.goalName);
            gAmount = itemView.findViewById(R.id.goalAmount);
            spCat = itemview.findViewById(R.id.spCat);
            gCompletion = itemview.findViewById(R.id.completionStatus);
            progresbar = itemview.findViewById(R.id.progressBar);

            editGoal= itemview.findViewById(R.id.editGoal);
            deleteGoal = itemview.findViewById(R.id.deleteGoal);

            Log.d("Goal Adapter", "Goal Adapter View Holder");

        }

    }
}
