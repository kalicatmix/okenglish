package com.oe.okenglish.entity;

import androidx.annotation.IntRange;

public class Reader {
    public static final int TYPE_NEWS = 0;
    public static final int TYPE_MOVIE = 1;
    private String imgUrl;
    private String title;
    private String sound;
    private String id;
    @IntRange(from = TYPE_NEWS, to = TYPE_MOVIE)
    private int type;

    public Reader() {

    }

    public Reader( String title,String imgUrl, String sound, String id, int type) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.sound = sound;
        this.id = id;
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
