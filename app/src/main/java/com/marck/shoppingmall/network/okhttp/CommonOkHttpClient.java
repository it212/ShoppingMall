package com.marck.shoppingmall.network.okhttp;

import com.marck.shoppingmall.network.okhttp.listener.DisposeDataHandler;
import com.marck.shoppingmall.network.okhttp.response.CommonJsonCallback;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CommonOkHttpClient {

    private static final int TIME_OUT = 10;
    private static OkHttpClient sInstance;

    /**
     * We create a OkHttpClient instance positively
     */
    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        // set timeout
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.followRedirects(true);

        sInstance = okHttpBuilder.build();
    }

    public static Call get(Request request, DisposeDataHandler handle){
        Call call = sInstance.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }
}
