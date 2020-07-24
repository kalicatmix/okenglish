package com.oe.okenglish.entity;

import android.graphics.drawable.Drawable;

public class Plan {
    String name;
    String dbName;
    Drawable drawable;
    public Plan() {
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Plan(String name, String dbName, Drawable drawable) {
        this.name = name;
        this.dbName = dbName;
        this.drawable=drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
