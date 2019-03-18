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
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.impl.WareDetailActivity;
import com.marck.shoppingmall.adapter.hot.HotPagerAdapter;
import com.marck.shoppingmall.fragment.BaseFragment;
import com.marck.shoppingmall.model.hotpager.HotPagerData;
import com.marck.shoppingmall.model.hotpager.HotSale;
import com.marck.shoppingmall.network.http.HttpRequestCenter;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;
import com.marck.shoppingmall.util.Constants;
import com.marck.shoppingmall.widget.divider.HotItemDecoration;

/**
 * Created by Marck on 19/03/08
 */
public class HotFragment extends BaseFragment implements HotPagerAdapter.OnItemClickListener {

    private static final String TAG = HotFragment.class.getSimpleName();

    /**
     * UI
     */
    private View mView;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLayout;
    private HotPagerAdapter mHotPagerAdapter;

    /**
     * Data
     */
    private HotPagerData mHotPagerData;
    private int currentPage = 1;

    /**
     * State
     */
    enum State{
        STATE_NORMAL,    // 普通加载模式
        STATE_REFRESH,   // 刷新
        STATE_LOAD_MORE  // 加载更多
    }
    private State mState = State.STATE_NORMAL;  // 默认为普通加载模式

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHotPagerData();  // 请求网络数据
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hot_layout, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        initRefreshLayout();
        return mView;
    }

    @Override
    public void onItemClick(View view, int position) {
        // item 点击事件，跳转到 WareDetailActivity
        HotSale ware = mHotPagerData.getList().get(position);
        Intent intent = new Intent(this.getContext(), WareDetailActivity.class);
        intent.putExtra(Constants.WARES_ID, ware);
        startActivity(intent);
    }

    /**
     * 初始化 {@link MaterialRefreshLayout}
     */
    private void initRefreshLayout() {
        mRefreshLayout = (MaterialRefreshLayout) mView.findViewById(R.id.material_refresh_layout);
        mRefreshLayout.setLoadMore(true);  // 加载更多
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();  // 刷新数据
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                loadMoreData();  // 加载更多数据
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        currentPage = 1;
        mState = State.STATE_REFRESH;
        requestHotPagerData();
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        currentPage++;  // 加载下一页的数据
        mState = State.STATE_LOAD_MORE;
        requestHotPagerData();
    }


    private void requestHotPagerData() {
        HttpRequestCenter.requestHotPagerData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mHotPagerData = (HotPagerData) responseObj;
                if(mHotPagerData != null){
                    currentPage = mHotPagerData.getCurrentPage();
                    if(mHotPagerData.getList().size() > 0) {
                        loadView();
                    }else {
                        if(mState == State.STATE_LOAD_MORE){
                            Toast.makeText(getActivity(), "没有更多了",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.finishRefreshLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, currentPage);
    }

    private void loadView() {
        switch (mState){
            case STATE_NORMAL:
                mHotPagerAdapter = new HotPagerAdapter(getActivity(), mHotPagerData.getList());
                mHotPagerAdapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(mHotPagerAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
                mRecyclerView.addItemDecoration(new HotItemDecoration());
                break;
            case STATE_REFRESH:
                Log.d(TAG, mHotPagerData.getList().toString());
                mHotPagerAdapter.clear();
                mHotPagerAdapter.addData(mHotPagerData.getList());
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_LOAD_MORE:
                mHotPagerAdapter.addData(mHotPagerAdapter.getData().size(), mHotPagerData.getList());
                mRecyclerView.scrollToPosition(mHotPagerAdapter.getData().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

}
