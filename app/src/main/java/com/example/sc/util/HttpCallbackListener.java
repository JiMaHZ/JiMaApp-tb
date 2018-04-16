package com.example.sc.util;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
