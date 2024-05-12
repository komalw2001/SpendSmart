package com.example.spendsmart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

        rvReminders = view.findViewById(R.id.rvReminders);

        rvReminders.setLayoutManager(new LinearLayoutManager(context));
        rvReminders.setHasFixedSize(true);

        adapter = new ReminderAdapter(context);
        rvReminders.setAdapter(adapter);
    }
}