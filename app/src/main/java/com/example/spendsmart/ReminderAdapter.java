package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReminderAdapter extends FirebaseRecyclerAdapter<Reminder, ReminderAdapter.ReminderViewHolder> {

    Context context;
    String user;



    public ReminderAdapter(@NonNull FirebaseRecyclerOptions<Reminder> options, Context context) {
        super(options);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user = sp.getString("session_user", null);
    }



    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_reminder, parent, false);
        return new ReminderViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderViewHolder holder, int i, @NonNull Reminder model) {
        holder.tvReminderText.setText(model.getText());
        holder.tvReminderTitle.setText(model.getTitle());
        holder.tvReminderDate.setText(model.getDate());

        if (model.getType() == 1) {
            holder.ivReminderIcon.setImageResource(R.drawable.reminder1);
            holder.cvReminderIcon.setCardBackgroundColor(context.getColor(R.color.negative_balance));
        } else if (model.getType() == 2) {
            holder.ivReminderIcon.setImageResource(R.drawable.reminder2);
            holder.cvReminderIcon.setCardBackgroundColor(context.getColor(R.color.positive_balance));
        }
        else if (model.getType() == 3) {
            holder.ivReminderIcon.setImageResource(R.drawable.reminder3);
            holder.cvReminderIcon.setCardBackgroundColor(context.getColor(R.color.blue));
        }

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("Reminders").child(user).child(getRef(holder.getAdapterPosition()).getKey())
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Reminder Dismissed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ReminderAdapter", "Error: " + e.getMessage());
                            }
                        });
            }
        });
    }


    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        TextView tvReminderText, tvReminderTitle, tvReminderDate, tvDelete;
        ImageView ivReminderIcon;
        CardView cvReminderIcon;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReminderText = itemView.findViewById(R.id.tvReminderText);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderDate = itemView.findViewById(R.id.tvReminderDate);
            tvDelete = itemView.findViewById(R.id.tvDismiss);
            ivReminderIcon = itemView.findViewById(R.id.ivReminderIcon);
            cvReminderIcon = itemView.findViewById(R.id.cvReminderIcon);
        }
    }
}