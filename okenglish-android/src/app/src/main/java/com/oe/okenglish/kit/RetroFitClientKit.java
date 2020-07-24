package com.oe.okenglish.kit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClientKit {
    private Retrofit retrofit;
    private static RetroFitClientKit kit;
    private GsonConverterFactory factory;

    private RetroFitClientKit() {
        factory = GsonConverterFactory.create();
        retrofit = new Retrofit.Builder().addConverterFactory(factory).baseUrl(ApiKit.URL_TRANSLATE_BASE).build();
    }

    public static RetroFitClientKit getInstance() {
        synchronized (RetroFitClientKit.class) {
            if (kit == null) kit = new RetroFitClientKit();
        }
        return kit;
    }

    public <T> T build(String baseUrl, Class<T> tclass) {
        return retrofit.newBuilder().addConverterFactory(factory).baseUrl(baseUrl).build().create(tclass);
    }
}
