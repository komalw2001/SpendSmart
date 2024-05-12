package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    ArrayList<Reminder> list;
    Context context;

    public ReminderAdapter(Context context) {
        this.context = context;

        list = new ArrayList<>();

        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user = sp.getString("session_user",null);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Reminders");
        Query remindersQuery = dbRef.orderByChild("user").equalTo(user);
        remindersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                    Reminder reminder = reminderSnapshot.getValue(Reminder.class);
                    list.add(reminder);
                }
                Collections.reverse(list);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ReminderAdapter", "Database error: " + databaseError.getMessage());
            }
        });
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
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.tvReminderText.setText(list.get(position).getText());
        holder.tvReminderTitle.setText(list.get(position).getTitle());
        holder.tvReminderDate.setText(list.get(position).getDate());

        if (list.get(position).getType() == 1)
        {
            int iconResourceId = context.getResources().getIdentifier("reminder1", "drawable", context.getPackageName());
            holder.ivReminderIcon.setImageResource(iconResourceId);
            holder.cvReminderIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.negative_balance));
        }
        else if (list.get(position).getType() == 2)
        {
            int iconResourceId = context.getResources().getIdentifier("reminder2", "drawable", context.getPackageName());
            holder.ivReminderIcon.setImageResource(iconResourceId);
            holder.cvReminderIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.positive_balance));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView tvReminderText, tvReminderTitle, tvReminderDate;
        ImageView ivReminderIcon;
        CardView cvReminderIcon;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReminderText = itemView.findViewById(R.id.tvReminderText);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderDate = itemView.findViewById(R.id.tvReminderDate);
            ivReminderIcon = itemView.findViewById(R.id.ivReminderIcon);
            cvReminderIcon = itemView.findViewById(R.id.cvReminderIcon);
        }
    }
}
