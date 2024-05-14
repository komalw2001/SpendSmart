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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
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
                    int catIndex = expense.getCategory();
                    double curr = list.get(catIndex).getTotal();
                    list.get(catIndex).setTotal(curr+expense.getAmount());
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
        List<PieModel> pieModelList = new ArrayList<>();

        for (CashFlowCategory category : list) {
            PieModel pieModel = new PieModel(category.getCategory().getName(), (float) category.getTotal(), Color.parseColor(category.getCategory().getColor()));
            pieModelList.add(pieModel);
        }

        PieChart pieChart = view.findViewById(R.id.pcExpenses);

        pieChart.clearChart();

        for (PieModel pieModel : pieModelList) {
            pieChart.addPieSlice(pieModel);
        }

        pieChart.startAnimation();
    }


}
