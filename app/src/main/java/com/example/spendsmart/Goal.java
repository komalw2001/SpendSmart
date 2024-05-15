package com.example.spendsmart;
import java.util.UUID;
public class Goal {

    public Goal()
    {

    }
    public Goal(String user, String goalName, int totalGoal, int goalAchieved) {
        this.user = user;
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



    public String getGoalID()
    {
        return goalID;
    }

    public int goalAchieved;

    public String user;

    public void setGoalID(String goalID) {
        this.goalID = goalID;
    }

    public String goalID;

}
