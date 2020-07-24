package com.oe.okenglish.api;

import com.oe.okenglish.entity.VideoJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoURLFetcher {
    @GET("dataapi/jsp/getTitle.jsp?total=100&type=voavideo")
    Call<VideoJson> listVideo(@Query("sign") String sign);
}
