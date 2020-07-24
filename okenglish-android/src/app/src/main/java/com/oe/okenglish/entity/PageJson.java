package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PageJson {
    @SerializedName("readCount")
    String readCount;
    @SerializedName("data")
    ArrayList<Page> data;

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public ArrayList<Page> getData() {
        return data;
    }

    public void setData(ArrayList<Page> data) {
        this.data = data;
    }
}
