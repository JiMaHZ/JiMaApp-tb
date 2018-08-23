package com.example.sc.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.sc.myapplication.LoginActivity.JSON;

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
    public static void sendOkHttpPost(String address,String token,String method,String params,okhttp3.Callback callback){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("method","H"+method);
            jsonObject.put("params",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();
        Request request=new Request.Builder()
                .url(address)
                .header("Content-Type", "application/json")
                .addHeader("X-Authorization","Bearer "+token)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
