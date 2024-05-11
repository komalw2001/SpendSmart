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

public class ExpenseAdapter extends FirebaseRecyclerAdapter<Expense, ExpenseAdapter.ViewHolder> {

    Context context;

    public ExpenseAdapter(Context context, @NonNull FirebaseRecyclerOptions<Expense> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Expense expense) {

        viewHolder.tvAmount.setText(expense.getAmount());
        viewHolder.tvDate.setText(expense.getDate());


        viewHolder.tvCatName.setText(Categories.expenseCategories.get(expense.getCategory()).getName());

        String iconName = Categories.expenseCategories.get(expense.getCategory()).getIcon();
        int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        viewHolder.ivCatIcon.setImageResource(iconResourceId);
//        }
//
//        else
//        {
//            viewHolder.tvCatName.setText(Categories.incomeCategories.get(expense.getCategory()).getName());
//
//            String iconName = Categories.incomeCategories.get(expense.getCategory()).getIcon();
//            int iconResourceId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
//            viewHolder.ivCatIcon.setImageResource(iconResourceId);
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_expense, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvAmount, tvDate, tvCatName;
        ImageView ivCatIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            ivCatIcon = itemView.findViewById(R.id.ivCatIcon);
        }
    }
}
