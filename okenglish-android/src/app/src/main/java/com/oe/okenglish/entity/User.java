package com.oe.okenglish.entity;

public class User {
    int id;
    String name;
    int plan;
    int dayWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getDayWord() {
        return dayWord;
    }

    public void setDayWord(int dayWord) {
        this.dayWord = dayWord;
    }
}
