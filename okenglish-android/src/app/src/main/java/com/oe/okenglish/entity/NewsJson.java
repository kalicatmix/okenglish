package com.oe.okenglish.entity;

import java.util.ArrayList;

public class NewsJson {
    int result;
    int total;
    ArrayList<NewsData> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<NewsData> getData() {
        return data;
    }

    public void setData(ArrayList<NewsData> data) {
        this.data = data;
    }
}
