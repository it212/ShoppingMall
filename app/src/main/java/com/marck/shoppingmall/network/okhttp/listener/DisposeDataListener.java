package com.marck.shoppingmall.network.okhttp.listener;

public interface DisposeDataListener {

    void onSuccess(Object responseObj);

    void onFailure(Object responseObj);
}
