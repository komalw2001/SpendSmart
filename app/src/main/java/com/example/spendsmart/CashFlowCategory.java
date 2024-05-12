package com.example.spendsmart;

public class CashFlowCategory {
    Category category;
    double Total;
    String type;

    public CashFlowCategory(Category category, double total, String type) {
        this.category = category;
        Total = total;
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
