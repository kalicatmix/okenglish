package com.oe.okenglish.entity;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    String id;
    @SerializedName("thumbnail_w300_sha1")
    String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

