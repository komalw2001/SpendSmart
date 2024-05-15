package com.example.spendsmart;

import android.app.slice.SliceItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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


public class OverviewFragment extends Fragment
{
    Context context;
    ArrayList<CashFlowCategory> list;
    TextView tvBalance;
    RecyclerView rvIncomes,rvExpenses;
    DatabaseReference dbRef;
    TransactionAdapter transactionAdapter;
    PieChart pcExpenses;
    TextView tooltipView;

    public OverviewFragment()
    {

    }
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

                            tvBalance.setText(String.format("%.2f", balance));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Showing Cash","Error: "+error.getMessage());
                    }
                });

        rvIncomes = view.findViewById(R.id.rvIncomes);

        rvIncomes.setLayoutManager(new LinearLayoutManager(context));
        rvIncomes.setHasFixedSize(true);

        transactionAdapter = new TransactionAdapter(context,user);
        rvIncomes.setAdapter(transactionAdapter);

        PieChart pieChart = view.findViewById(R.id.pcExpenses);

        if (isNightModeActive())
            pieChart.setBackgroundColor(Color.BLACK);

        Query expenseQuery = dbRef.child("Expenses").orderByChild("user").equalTo(user);
        list = new ArrayList<>();

        for (int i = 0; i < Categories.expenseCategories.size(); i++){
            list.add(new CashFlowCategory(Categories.expenseCategories.get(i),0));
        }

        expenseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    Transaction expense = expenseSnapshot.getValue(Transaction.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date expenseDate;
                    try {
                        expenseDate = dateFormat.parse(expense.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }

                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH);
                    int currentYear = cal.get(Calendar.YEAR);
                    cal.setTime(expenseDate);
                    int expenseMonth = cal.get(Calendar.MONTH);
                    int expenseYear = cal.get(Calendar.YEAR);
                    if (currentMonth == expenseMonth && currentYear == expenseYear) {
                        int catIndex = expense.getCategory();
                        double curr = list.get(catIndex).getTotal();
                        list.get(catIndex).setTotal(curr + expense.getAmount());
                    }
                }

                updatePieChart(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("CashFlowCategoryAdapter", "Expense query cancelled: " + error.getMessage());
            }
        });

        tooltipView = view.findViewById(R.id.tooltipView);
        tooltipView.setVisibility(View.INVISIBLE);


    }

    private boolean isNightModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    private void updatePieChart(View view) {
        List<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        float total = 0;

        for (CashFlowCategory category : list) {
            total += category.getTotal();
        }

        float threshold = total * 0.01f; // 1% threshold
        float otherCategoriesValue = 0;

        for (CashFlowCategory category : list) {
            if (category.getTotal() >= threshold && category.getTotal() > 0) {
                pieEntries.add(new PieEntry((float) category.getTotal(), category.getCategory().getName()));
                colors.add(Color.parseColor(category.getCategory().getColor()));
            } else {
                otherCategoriesValue += category.getTotal();
            }
        }

        if (otherCategoriesValue > 0) {
            pieEntries.add(new PieEntry(otherCategoriesValue, "Other Categories"));
            colors.add(Color.GRAY);
        }

        PieChart pieChart = view.findViewById(R.id.pcExpenses);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.clear();

        PieDataSet dataSet = new PieDataSet(pieEntries, "Expenses");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);

        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);

//        pieChart.setCenterText("%");
        pieChart.setData(data);
        pieChart.getDescription().setText("");
        pieChart.getLegend().setEnabled(false);

        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(45);

        pieChart.invalidate();
        pieChart.animateY(1000);

        pieChart.getDescription().setTextSize(11);

        if (isNightModeActive()) {
            pieChart.setHoleColor(Color.BLACK);
            pieChart.setCenterTextColor(Color.WHITE);
            pieChart.getDescription().setTextColor(Color.WHITE);
        }

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pieEntry = (PieEntry) e;
                pieChart.getDescription().setText(pieEntry.getLabel() + ": " + pieEntry.getValue());
            }

            @Override
            public void onNothingSelected() {
                pieChart.getDescription().setText("");
            }
        });

        if (pieEntries.isEmpty()){
            pieChart.clear();
        }
    }


}
