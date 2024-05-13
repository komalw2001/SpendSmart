package com.example.spendsmart;

public class CashFlowCategory {
    Category category;
    double Total;

    public CashFlowCategory(Category category, double total) {
        this.category = category;
        Total = total;
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
}
