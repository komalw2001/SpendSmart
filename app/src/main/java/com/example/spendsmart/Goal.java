package com.example.spendsmart;

public class Goal {

    public Goal()
    {

    }
    public Goal(String goalName, int totalGoal, int goalAchieved) {
        this.goalName = goalName;
        this.totalGoal = totalGoal;
        this.goalAchieved = goalAchieved;
    }
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public int getTotalGoal() {
        return totalGoal;
    }

    public void setTotalGoal(int totalGoal) {
        this.totalGoal = totalGoal;
    }

    public int getGoalAchieved() {
        return goalAchieved;
    }

    public void setGoalAchieved(int goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public String goalName;

    public int totalGoal;



    public int goalAchieved;



}
