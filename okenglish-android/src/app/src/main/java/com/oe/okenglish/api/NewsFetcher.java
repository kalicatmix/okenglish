package com.oe.okenglish.api;

import com.oe.okenglish.entity.NewsJson;
import com.oe.okenglish.kit.ApiKit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsFetcher {
    @GET("cmsApi/getMyNewsList.jsp?maxId=0&level=0&source=0&myid=0")
    Call<NewsJson> listNews(@Query("pageCounts") String pageNum);
}
