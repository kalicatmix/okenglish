package com.oe.okenglish.entity;


import com.google.gson.annotations.SerializedName;

public class NewsData {
    @SerializedName("CreatTime")
    String creatTime;
    @SerializedName("Title")
    String title;
    @SerializedName("Title_cn")
    String chTitle;
    @SerializedName("Sound")
    String sound;
    @SerializedName("Pic")
    String pic;
    @SerializedName("WordCount")
    String wordCount;
    @SerializedName("ReadCount")
    String readCount;
    @SerializedName("NewsId")
    String newsId;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getChTitle() {
        return chTitle;
    }

    public void setChTitle(String chTitle) {
        this.chTitle = chTitle;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}
