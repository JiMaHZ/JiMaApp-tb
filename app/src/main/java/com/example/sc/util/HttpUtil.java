package com.example.sc.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,String token,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .header("Content-Type", "application/json")
                .addHeader("X-Authorization","Bearer "+token)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }
}
