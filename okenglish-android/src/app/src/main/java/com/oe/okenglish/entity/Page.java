package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

public class Page {
    @SerializedName("Sentence_cn")
    String sentenceCh;
    @SerializedName("Sentence")
    String sentence;

    public String getSentenceCh() {
        return sentenceCh;
    }

    public void setSentenceCh(String sentenceCh) {
        this.sentenceCh = sentenceCh;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
