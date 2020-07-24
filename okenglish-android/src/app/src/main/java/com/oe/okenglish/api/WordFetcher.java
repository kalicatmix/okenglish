package com.oe.okenglish.api;

import com.oe.okenglish.entity.WordJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordFetcher {
    @GET("translate?doctype=json&type=AUTO")
    Call<WordJson> getWord(@Query("i") String word);
}
