package com.marck.shoppingmall.network.okhttp.request;

import android.util.Log;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

public class CommonRequest {

    private static final String TAG = CommonRequest.class.getSimpleName();

    /**
     * Create a get request for network
     * @param url access url
     * @param params request params
     * @return {@link Request}
     */
    public static Request createGetRequest(String url, RequestParams params){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if(params != null){
            for(Map.Entry<String, String> entry : params.urlParams.entrySet()){
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            Log.d(TAG, urlBuilder.substring(0, urlBuilder.length()-1));
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length()-1)).get().build();
    }

    /**
     * Create a post request for network
     * @param url access url
     * @param params post params
     * @return {@link Request}
     */
    public static Request createPostRequest(String url, RequestParams params){
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null){
            for(Map.Entry<String, String> entry : params.urlParams.entrySet()){
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }


}
