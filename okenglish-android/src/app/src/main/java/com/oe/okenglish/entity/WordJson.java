package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WordJson {
    @SerializedName("type")
    String type;
    @SerializedName("translateResult")
    ArrayList<ArrayList<Result>> results;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ArrayList<Result>> getResults() {
        return results;
    }

    public void setResults(ArrayList<ArrayList<Result>> results) {
        this.results = results;
    }

    public static class Result {
        @SerializedName("src")
        String src;
        @SerializedName("tgt")
        String result;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}

