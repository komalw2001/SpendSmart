package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OverviewFragment extends Fragment
{
    Context context;

    TextView tvBalance;

    DatabaseReference dbRef;

    public OverviewFragment(Context c) {
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
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbRef = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String user = sp.getString("session_user",null);

        tvBalance = view.findViewById(R.id.tvBalance);

        dbRef.child("Users")
                .child(user)
                .child("balance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            Double balance = snapshot.getValue(Double.class);

                            if (balance <= 0) {
                                if (isNightModeActive()) {
                                    tvBalance.setTextColor(ContextCompat.getColor(context, R.color.negative_balance_night));
                                } else {
                                    tvBalance.setTextColor(ContextCompat.getColor(context, R.color.negative_balance));
                                }
                            } else {
                                if (isNightModeActive()) {
                                    tvBalance.setTextColor(ContextCompat.getColor(context, R.color.main_color_night));
                                } else {
                                    tvBalance.setTextColor(ContextCompat.getColor(context, R.color.main_color));
                                }
                            }

                            tvBalance.setText(String.format("%.0f", balance));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Showing Cash","Error: "+error.getMessage());
                    }
                });
    }

    private boolean isNightModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}