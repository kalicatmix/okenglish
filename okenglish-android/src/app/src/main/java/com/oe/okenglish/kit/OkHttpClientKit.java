package com.oe.okenglish.kit;

import com.oe.okenglish.callback.CommonCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientKit {
    private OkHttpClient client;
    private static OkHttpClientKit okHttpClientKit;
    private HashMap<String, Object> param;

    private OkHttpClientKit() {
        client = new OkHttpClient();
        param = new HashMap<>();
    }

    public static OkHttpClientKit getInstance() {
        if (okHttpClientKit == null) okHttpClientKit = new OkHttpClientKit();
        return okHttpClientKit;
    }

    public OkHttpClientKit get(String url) {
        param.put("method", "GET");
        param.put("url", url);
        return okHttpClientKit;
    }

    public OkHttpClientKit post(String url, RequestBody body, Headers headers) {
        param.put("method", "POST");
        param.put("url", url);
        param.put("body", body);
        param.put("headers", headers);
        return okHttpClientKit;
    }

    //formData key:value&key:value
    public OkHttpClientKit post(String url, String formData, Headers headers) {
        param.put("method", "POST");
        param.put("url", url);
        param.put("body", RequestBody.create(formData, MediaType.parse("application/x-www-form-urlencoded;charset=utf-8")));
        param.put("headers", headers);
        return okHttpClientKit;
    }

    public void enqueue(CommonCallback commonCallback) {
        if (param.get("method").toString().equals("GET")) {
            client.newCall(new Request.Builder().url(param.get("url").toString()).build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    param.clear();
                    commonCallback.call(response);
                }
            });
        } else {
            Request request = new Request.Builder().url((String) param.get("url")).headers((Headers) param.get("headers")).post((RequestBody) param.get("body")).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    AppKit.log("request:" + param.get("url"), "faild");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    param.clear();
                    commonCallback.call(response);
                }
            });
        }
    }
}
