package com.oe.okenglish.entity;

import java.util.ArrayList;

public class VideoJson {
    int total;
    ArrayList<Video>data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Video> getData() {
        return data;
    }

    public void setData(ArrayList<Video> data) {
        this.data = data;
    }
}
