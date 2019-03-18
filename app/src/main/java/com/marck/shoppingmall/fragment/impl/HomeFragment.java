package com.marck.shoppingmall.fragment.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.impl.WareListActivity;
import com.marck.shoppingmall.adapter.home.HomePagerAdapter;
import com.marck.shoppingmall.fragment.BaseFragment;
import com.marck.shoppingmall.model.homepager.HomePagerData;
import com.marck.shoppingmall.network.http.HttpRequestCenter;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;
import com.marck.shoppingmall.util.Constants;
import com.marck.shoppingmall.widget.CustomSliderView;
import com.marck.shoppingmall.widget.divider.HomeItemDecoration;

/**
 * Created by Marck on 19/03/08
 */
public class HomeFragment extends BaseFragment implements HomePagerAdapter.OnImageClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * UI 相关
     */
    private View mView;
    private CustomSliderView mSliderView;
    private RecyclerView mRecyclerView;
    private HomePagerAdapter mHomePagerAdapter;

    /**
     * Data
     */
    private HomePagerData mPagerData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHomePagerData();  // 请求 Home 页的数据
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onStop() {
        // release work
        if(mSliderView != null) {
            mSliderView.recycle();
        }
        super.onStop();
    }

    /**
     * 请求 Home 页的数据，底层使用 okhttp 进行网络请求
     */
    private void requestHomePagerData() {
        HttpRequestCenter.requestHomePagerData(new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                mPagerData = (HomePagerData) responseObj;
                showSuccessView();
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        });
    }

    private void showSuccessView() {
        if(mPagerData != null){
            mRecyclerView.setVisibility(View.VISIBLE);
            // 轮播器
            mSliderView = new CustomSliderView(this.getActivity(), mPagerData.getData().getHead());
            mHomePagerAdapter = new HomePagerAdapter(getActivity());
            mHomePagerAdapter.addData(mPagerData.getData().getRecommend());
            mHomePagerAdapter.setHeaderView(mSliderView);  // 将轮播添加到头部
            mHomePagerAdapter.setOnImageClickListener(this);  // 设置图片点击事件的监听
            mRecyclerView.setAdapter(mHomePagerAdapter);
            // 线性垂直布局
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new HomeItemDecoration());
        }
    }

    @Override
    public void onImageClick(View view, int campaignId) {
        // 点击图片跳转到 WareListActivity
        Intent intent = new Intent(getActivity(), WareListActivity.class);
        intent.putExtra(Constants.CAMPAIGN_ID, campaignId);
        startActivity(intent);
    }
}
