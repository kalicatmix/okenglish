package com.oe.okenglish.api;

import com.oe.okenglish.entity.WordImageJson;
import com.oe.okenglish.kit.ApiKit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordImageFetcher {
    @GET("api/v1/picture/search/")
    Call<WordImageJson>listImage(@Query("q") String name);
}
