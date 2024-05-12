package com.example.spendsmart;

public class Reminder {
    String title,text;
    int type;
    String user,date;


    public Reminder(String title, String text, int type, String user, String date) {
        this.title = title;
        this.text = text;
        this.type = type;
        this.user = user;
        this.date = date;
    }

    public Reminder() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
