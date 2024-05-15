package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RemindersFragment extends Fragment {

    Context context;

    RecyclerView rvReminders;
    ReminderAdapter adapter;

    public RemindersFragment(Context c) {
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
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp =context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user = sp.getString("session_user","");

        rvReminders = view.findViewById(R.id.rvReminders);
        rvReminders.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference().child("Reminders");
        FirebaseRecyclerOptions<Reminder> options =
                new FirebaseRecyclerOptions.Builder<Reminder>()
                        .setQuery(remindersRef.child(user).orderByChild("date"), Reminder.class)
                        .build();

        adapter = new ReminderAdapter(options, getContext());
        rvReminders.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}