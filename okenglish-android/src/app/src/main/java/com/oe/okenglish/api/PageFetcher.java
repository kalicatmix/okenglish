package com.oe.okenglish.api;

import com.oe.okenglish.entity.PageJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PageFetcher {
    @GET("getText.jsp?format=json&uid=65642725&appid=240")
    Call<PageJson> fetchPage(@Query("NewsId") String id);
}
