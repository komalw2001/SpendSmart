package com.example.spendsmart;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Transaction {
    private String amount;
    private int category;
    private String date;
    private String type; // "Income" or "Expense"
    private String user;

    public Transaction() {
        // Default constructor required for Firebase
    }

    public Transaction(String amount, int category, String date, String type, String user) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
        this.user = user;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
