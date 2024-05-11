package com.example.spendsmart;


public class Expense {
    private String amount;
    private int category;
    private String date;
    private String user;

    public Expense() {
        // Default constructor required for Firebase
    }

    public Expense(String amount, int category, String date, String user) {
        this.amount = amount;
        this.category = category;
        this.date = date;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
