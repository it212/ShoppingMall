package com.marck.shoppingmall.network.okhttp.listener;

public class DisposeDataHandler {

    public DisposeDataListener mListener;
    public Class<?> mClass;

    public DisposeDataHandler(DisposeDataListener listener) {
        this.mListener = listener;
    }

    public DisposeDataHandler(DisposeDataListener listener, Class<?> clazz) {
        this.mListener = listener;
        this.mClass = clazz;
    }


}
