package com.marck.shoppingmall.network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.marck.shoppingmall.network.okhttp.CommonOkHttpClient;
import com.marck.shoppingmall.network.okhttp.exception.OkHttpException;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataHandler;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommonJsonCallback implements Callback {

    private static final String TAG = CommonJsonCallback.class.getSimpleName();

    protected final int NETWORK_ERROR = -1;  // the network relative error
    protected final int JSON_ERROR = -2;     // the JSON relative error
    protected final int OTHER_ERROR = -3;    // the unknow error
    protected final String EMPTY_MSG = "";

    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;  //

    public CommonJsonCallback(DisposeDataHandler handler){
        this.mListener = handler.mListener;
        this.mClass = handler.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null){
                    mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(Object responseObj) {
        if(responseObj == null && responseObj.toString().trim().equals("")){
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        if(mClass == null){
            mListener.onSuccess(responseObj);
        }else {
//            Log.d(TAG, (String)responseObj);
            try {
                Object obj = new Gson().fromJson((String) responseObj, mClass);
                if(obj != null){
                    mListener.onSuccess(obj);
                }else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }catch (Exception e){
                mListener.onFailure(new OkHttpException(OTHER_ERROR, e));
            }

        }
    }
}
