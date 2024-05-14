package com.example.spendsmart;

import android.content.Context;
import android.content.res.Configuration;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    ArrayList<Transaction> list;
    Context context;

    public TransactionAdapter(Context context, String user) {
        this.context = context;

        list = new ArrayList<>();

        DatabaseReference incomeReference = FirebaseDatabase.getInstance().getReference().child("Incomes");
        DatabaseReference expenseReference = FirebaseDatabase.getInstance().getReference().child("Expenses");

        Query incomeQuery = incomeReference.orderByChild("user").equalTo(user);
        Query expenseQuery = expenseReference.orderByChild("user").equalTo(user);

        ValueEventListener incomeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction income = snapshot.getValue(Transaction.class);
                    income.setType("income");
                    list.add(income);
                }
                sortListByDate();
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TransactionAdapter", "Income query cancelled: " + databaseError.getMessage());
            }
        };

        ValueEventListener expenseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction expense = snapshot.getValue(Transaction.class);
                    expense.setType("expense");
                    list.add(expense);
                }
                sortListByDate();
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TransactionAdapter", "Expense query cancelled: " + databaseError.getMessage());
            }
        };

        incomeQuery.addValueEventListener(incomeListener);
        expenseQuery.addValueEventListener(expenseListener);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_income, parent, false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder viewHolder, int position) {

        if (list.get(position).getType().equals("income") ){
            viewHolder.tvAmount.setText("+"+String.format("%.2f", list.get(position).getAmount()));
            viewHolder.tvType.setText("Income");
            viewHolder.tvCatName.setText(Categories.incomeCategories.get(list.get(position).getCategory()).getName());

            int colorResId = context.getResources().getIdentifier(
                    isNightMode() ? "positive_balance_night" : "positive_balance",
                    "color",
                    context.getPackageName()
            );
            viewHolder.tvAmount.setTextColor(ContextCompat.getColor(context, colorResId));

            String iconName = Categories.incomeCategories.get(list.get(position).getCategory()).getIcon();
            int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            viewHolder.ivCatIcon.setImageResource(iconResourceId);

            viewHolder.cvCatIcon.setCardBackgroundColor(Color.parseColor(Categories.incomeCategories.get(list.get(position).getCategory()).getColor()));
        }
        else {
            viewHolder.tvAmount.setText("-"+String.format("%.2f", list.get(position).getAmount()));
            viewHolder.tvType.setText("Expense");
            viewHolder.tvCatName.setText(Categories.expenseCategories.get(list.get(position).getCategory()).getName());

            int colorResId = context.getResources().getIdentifier(
                    isNightMode() ? "negative_balance_night" : "negative_balance",
                    "color",
                    context.getPackageName()
            );
            viewHolder.tvAmount.setTextColor(ContextCompat.getColor(context, colorResId));

            String iconName = Categories.expenseCategories.get(list.get(position).getCategory()).getIcon();
            int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            viewHolder.ivCatIcon.setImageResource(iconResourceId);

            viewHolder.cvCatIcon.setCardBackgroundColor(Color.parseColor(Categories.expenseCategories.get(list.get(position).getCategory()).getColor()));
        }

        viewHolder.tvDate.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate, tvAmount, tvCatName, tvType;
        ImageView ivCatIcon;
        CardView cvCatIcon;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvType = itemView.findViewById(R.id.tvType);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            ivCatIcon = itemView.findViewById(R.id.ivCatIcon);
            cvCatIcon = itemView.findViewById(R.id.cvCatIcon);
        }
    }

    private boolean isNightMode() {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    private void sortListByDate() {
        Collections.sort(list, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                try {
                    Date date1 = sdf.parse(t1.getDate());
                    Date date2 = sdf.parse(t2.getDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    Log.d("TransactionAdapter",e.getMessage());
                }
                return 0;
            }
        });
    }
}
