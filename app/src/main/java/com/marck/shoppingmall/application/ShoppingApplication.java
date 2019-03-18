package com.marck.shoppingmall.application;

import android.app.Application;

/**
 * Created by Marck on 19/03/08
 */
public class ShoppingApplication extends Application {
    /**
     * Application 本身即为单例模式，无需再使用单例判空
     */
    private static ShoppingApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static ShoppingApplication getInstance(){
        return mApplication;
    }
}
