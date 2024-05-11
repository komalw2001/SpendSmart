package com.example.spendsmart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_layout, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tvCatName);
        ImageView imgView = convertView.findViewById(R.id.ivCatIcon);
        CardView cardView = convertView.findViewById(R.id.cvCatIcon);

        textView.setText(getItem(position).getName());
        cardView.setCardBackgroundColor(Color.parseColor(getItem(position).getColor()));

        String iconName = getItem(position).getIcon();
        int iconResourceId = getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
        imgView.setImageResource(iconResourceId);

        return convertView;
    }
}
