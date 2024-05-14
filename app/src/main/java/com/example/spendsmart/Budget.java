package com.example.spendsmart;

public class Budget {
    public String getBudgetName() {
        return budgetName;
    }

    public Budget()
    {

    }
    public Budget(String budgetName, int totalBudget, int totalSpent, String user,int categoryIndex) {
        this.budgetName = budgetName;
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
        this.user = user;
        this.status = true;
        this.categoryIndex = categoryIndex;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public int getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(int totalBudget) {
        this.totalBudget = totalBudget;
    }

    public int getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(int totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String budgetName;

    public int totalBudget;

    public int totalSpent;

    public String user;

    public boolean status; //true means within budget, false exceeded

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public int categoryIndex;

}
