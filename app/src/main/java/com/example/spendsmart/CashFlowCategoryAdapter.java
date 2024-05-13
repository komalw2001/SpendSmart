package com.example.spendsmart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CashFlowCategoryAdapter extends RecyclerView.Adapter<CashFlowCategoryAdapter.CashFlowViewHolder> {

    Context context;
    String type;

    ArrayList<CashFlowCategory> list;

    public CashFlowCategoryAdapter(Context c, String t, String user) {
        context = c;
        type = t;

        list = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        if (type == "expense")
        {
            Query expenseQuery = dbRef.child("Expenses").orderByChild("user").equalTo(user);

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
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("CashFlowCategoryAdapter", "Expense query cancelled: " + error.getMessage());
                }
            });
        }
        else
        {
            Query incomeQuery = dbRef.child("Incomes").orderByChild("user").equalTo(user);

            for (int i = 0; i < Categories.incomeCategories.size(); i++){
                list.add(new CashFlowCategory(Categories.incomeCategories.get(i),0));
            }

            incomeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot incomeSnapshot : snapshot.getChildren()) {
                        Transaction income = incomeSnapshot.getValue(Transaction.class);
                        int catIndex = income.getCategory();
                        double curr = list.get(catIndex).getTotal();
                        list.get(catIndex).setTotal(curr+income.getAmount());
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("CashFlowCategoryAdapter", "Income query cancelled: " + error.getMessage());
                }
            });
        }
    }

    @NonNull
    @Override
    public CashFlowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_categorywise_transaction, parent, false);
        return new CashFlowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CashFlowViewHolder holder, int position)
    {
        holder.tvCatName.setText(list.get(position).getCategory().getName());

        String iconName = list.get(position).getCategory().getIcon();
        int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        holder.ivCatIcon.setImageResource(iconResourceId);

        holder.cvCatIcon.setCardBackgroundColor(Color.parseColor(list.get(position).getCategory().getColor()));

        if(type == "expense")
        {
            holder.tvCatAmount.setText("-"+String.format("%.2f",list.get(position).getTotal()));
        }
        else {
            holder.tvCatAmount.setText("+"+String.format("%.2f",list.get(position).getTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CashFlowViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCatIcon;
        CardView cvCatIcon;
        TextView tvCatName;
        TextView tvCatAmount;

        public CashFlowViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCatIcon = itemView.findViewById(R.id.ivCatIcon);
            tvCatAmount = itemView.findViewById(R.id.tvCatAmount);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            cvCatIcon = itemView.findViewById(R.id.cvCatIcon);
        }
    }
}
