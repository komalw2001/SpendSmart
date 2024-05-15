package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        addDailyReminderToDatabase();
        addMonthlyIncomeReminderToDatabase();
        return Result.success();
    }

    private void addDailyReminderToDatabase() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String user = sp.getString("session_user", null);

        if (user != null) {

            DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference().child("Reminders").child(user);
            HashMap<String, Object> dailyReminderData = new HashMap<>();
            dailyReminderData.put("type", 3);
            dailyReminderData.put("title", "Daily Reminder");
            dailyReminderData.put("text", "Don't forget to add your expenses for today!");
            dailyReminderData.put("date", formattedDate);

            remindersRef.push().setValue(dailyReminderData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private void addMonthlyIncomeReminderToDatabase() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        if (currentDay == 1) {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String user = sp.getString("session_user", null);

            if (user != null){

                DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference().child("Reminders").child(user);
                HashMap<String, Object> monthlyReminderData = new HashMap<>();
                monthlyReminderData.put("type", 3);
                monthlyReminderData.put("title", "Monthly Reminder");
                monthlyReminderData.put("text", "Don't forget to add your income for this month!");
                monthlyReminderData.put("date", formattedDate);

                remindersRef.push().setValue(monthlyReminderData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        }
    }
}

