package com.example.spendsmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReportsFragment extends Fragment {

    Context context;
    ProgressBar pbTotal;
    TextView tvNetBalance, tvIncome, tvExpense, tvReportIncome, tvReportExpense;
    RecyclerView rvIncome,rvExpense;
    double totalIncome, totalExpense;

    DatabaseReference dbRef;

    CashFlowCategoryAdapter incomeAdapter,expenseAdapter;

    public ReportsFragment(Context c) {
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
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbTotal = view.findViewById(R.id.pbTotal);

        tvNetBalance = view.findViewById(R.id.tvNetBalance);
        tvIncome = view.findViewById(R.id.tvIncome);
        tvExpense = view.findViewById(R.id.tvExpense);
        tvReportIncome = view.findViewById(R.id.tvReportIncome);
        tvReportExpense = view.findViewById(R.id.tvReportExpense);

        rvIncome = view.findViewById(R.id.rvIncomes);
        rvExpense = view.findViewById(R.id.rvExpenses);

        dbRef = FirebaseDatabase.getInstance().getReference();

        totalExpense = 0;
        totalIncome = 0;

        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user = sp.getString("session_user",null);

        Calendar currentMonthStart = Calendar.getInstance();
        currentMonthStart.set(Calendar.DAY_OF_MONTH, 1);
        Date currentMonthStartDate = currentMonthStart.getTime();

        Calendar currentMonthEnd = Calendar.getInstance();
        currentMonthEnd.set(Calendar.DAY_OF_MONTH, currentMonthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currentMonthEndDate = currentMonthEnd.getTime();

        Query expensesQuery = dbRef.child("Expenses").orderByChild("user").equalTo(user);
        expensesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalExpense = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Date expenseDate = dateFormat.parse(expenseSnapshot.child("date").getValue(String.class));
                        if (expenseDate != null && (expenseDate.after(currentMonthStartDate) || expenseDate.equals(currentMonthStartDate)) && (expenseDate.before(currentMonthEndDate) || expenseDate.equals(currentMonthEndDate))) {
                            double amount = expenseSnapshot.child("amount").getValue(Double.class);
                            totalExpense += amount;
                        }
                    } catch (ParseException e) {
                        Log.d("ReportsFragment", e.getMessage());
                    }
                }

                tvReportExpense.setText("" + totalExpense);
                tvExpense.setText("-" + totalExpense);

                int progress = (int) ((totalExpense / totalIncome) * 100);
                pbTotal.setProgress(progress);

                double net = totalIncome - totalExpense;
                if (net < 0)
                    tvNetBalance.setText("" + net);
                else
                    tvNetBalance.setText("+" + net);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ReportsFragment", databaseError.getMessage());
            }
        });

        Query incomesQuery = dbRef.child("Incomes").orderByChild("user").equalTo(user);
        incomesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalIncome = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                for (DataSnapshot incomeSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Date incomeDate = dateFormat.parse(incomeSnapshot.child("date").getValue(String.class));
                        if (incomeDate != null && (incomeDate.after(currentMonthStartDate) || incomeDate.equals(currentMonthStartDate)) && (incomeDate.before(currentMonthEndDate) || incomeDate.equals(currentMonthEndDate))) {
                            double amount = incomeSnapshot.child("amount").getValue(Double.class);
                            totalIncome += amount;
                        }
                    } catch (ParseException e) {
                        Log.d("ReportsFragment", e.getMessage());
                    }
                }

                tvReportIncome.setText("" + totalIncome);
                tvIncome.setText("+" + totalIncome);

                int progress = (int) ((totalExpense / totalIncome) * 100);
                pbTotal.setProgress(progress);

                double net = totalIncome - totalExpense;
                if (net < 0)
                    tvNetBalance.setText("" + net);
                else
                    tvNetBalance.setText("+" + net);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ReportsFragment", databaseError.getMessage());
            }
        });

        rvExpense.setLayoutManager(new LinearLayoutManager(context));
        rvIncome.setLayoutManager(new LinearLayoutManager(context));

        rvExpense.setHasFixedSize(true);
        rvIncome.setHasFixedSize(true);

        expenseAdapter = new CashFlowCategoryAdapter(context,"expense",user);
        incomeAdapter = new CashFlowCategoryAdapter(context,"income",user);

        rvExpense.setAdapter(expenseAdapter);
        rvIncome.setAdapter(incomeAdapter);
    }


}