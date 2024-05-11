package com.example.spendsmart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class TransactionAdapter extends FirebaseRecyclerAdapter<Transaction,TransactionAdapter.ViewHolder> {

    Context context;

    public TransactionAdapter(Context context, @NonNull FirebaseRecyclerOptions<Transaction> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Transaction transaction) {

        String type = transaction.getType();

        viewHolder.tvType.setText(type);
        viewHolder.tvAmount.setText(transaction.getAmount());
        viewHolder.tvDate.setText(transaction.getDate());

        if (type.equals("expense")){
            viewHolder.tvCatName.setText(Categories.expenseCategories.get(transaction.getCategory()).getName());

            String iconName = Categories.expenseCategories.get(transaction.getCategory()).getIcon();
            int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            viewHolder.ivCatIcon.setImageResource(iconResourceId);
        }

        else
        {
            viewHolder.tvCatName.setText(Categories.incomeCategories.get(transaction.getCategory()).getName());

            String iconName = Categories.incomeCategories.get(transaction.getCategory()).getIcon();
            int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            viewHolder.ivCatIcon.setImageResource(iconResourceId);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_transaction, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvAmount, tvType, tvDate, tvCatName;
        ImageView ivCatIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            ivCatIcon = itemView.findViewById(R.id.ivCatIcon);
        }
    }
}
