package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Message {
    @SerializedName("code")
    String code;
    @SerializedName("info")
    String info;
    @SerializedName("data")
    ArrayList<String> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
