package com.example.spendsmart;


public class Transaction {
    private double amount;
    private int category;
    private String date;
    private String user;
    private String type;

    public Transaction() {}

    public Transaction(double amount, int category, String date, String user) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
