package com.marck.shoppingmall.fragment.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.impl.MainActivity;
import com.marck.shoppingmall.fragment.BaseFragment;
import com.marck.shoppingmall.widget.CustomToolbar;

/**
 * Description: This Fragment has not been developed yet !
 * Created by Marck on 19/03/14
 */
public class MineFragment extends BaseFragment {

    private CustomToolbar mToolbar; // activity 的 toolbar
    private MainActivity mMainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        mToolbar = mMainActivity.getToolbar();
        mToolbar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_layout, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // release
        mToolbar = null;
        mMainActivity = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // 切换相关状态的时候 mToolbar 需要做的相应变化
        if(!hidden){
            mToolbar.setVisibility(View.GONE);
        }else {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }
}
