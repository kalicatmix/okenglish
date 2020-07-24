package com.oe.okenglish.entity;

public class Word {
    String word;
    String translate;
    byte[] voice;

    public Word() {
    }

    public Word(String word, String translate, byte[] voice) {
        this.word = word;
        this.translate = translate;
        this.voice = voice;
    }

    public Word(String word, String translate) {
        this.word = word;
        this.translate = translate;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public byte[] getVoice() {
        return voice;
    }

    public void setVoice(byte[] voice) {
        this.voice = voice;
    }
}
