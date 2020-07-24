package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("CreateTime")
    String creatTime;
    @SerializedName("Title")
    String title;
    @SerializedName("Sound")
    String sound;
    @SerializedName("Pic")
    String pic;
    @SerializedName("Id")
    String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
