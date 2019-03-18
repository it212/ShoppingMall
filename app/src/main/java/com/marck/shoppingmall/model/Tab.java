package com.marck.shoppingmall.model;

import android.support.v4.app.Fragment;

/**
 * Created by Marck on 2019/03/07
 */
public class Tab {

    private int title; // 底部标题

    private int icon;  // 底部图标

    private Class<? extends Fragment> fragment;  // 对应的 Fragment

    public Tab(int title, int icon, Class<? extends Fragment> fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }
}
