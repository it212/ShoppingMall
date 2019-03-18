package com.marck.shoppingmall.fragment.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.marck.shoppingmall.adapter.category.CategoryAdapter;
import com.marck.shoppingmall.adapter.category.WaresAdapter;
import com.marck.shoppingmall.fragment.BaseFragment;
import com.marck.shoppingmall.model.categorypager.Category;
import com.marck.shoppingmall.model.categorypager.CategoryData;
import com.marck.shoppingmall.model.categorypager.WaresData;
import com.marck.shoppingmall.network.http.HttpRequestCenter;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;
import com.marck.shoppingmall.widget.divider.DividerGridItemDecoration;
import com.marck.shoppingmall.widget.divider.DividerItemDecoration;

/**
 * Created by Mrack on 19/03/09
 */
public class CategoryFragment extends BaseFragment {


    private static final String TAG = HotFragment.class.getSimpleName();

    /**
     * UI 相关
     */
    private View mView;
    private RecyclerView mRecyclerViewOne;  // 分类标签的 RecyclerView
    private RecyclerView mRecyclerViewTwo;  // 商品的 RecyclerView
    private MaterialRefreshLayout mRefreshLayout;
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    /**
     * Data
     */
    private CategoryData mCategoryData;
    private WaresData mWaresData;
    private int currentPage = 1;
    private int categoryId = 0;

    /**
     * State
     */
    enum State{
        STATE_NORMAL,     // 普通加载模式
        STATE_REFRESH,    // 刷新
        STATE_LOAD_MORE   // 加载更多
    }
    private State mState = State.STATE_NORMAL;  // 默认为普通加载模式

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCategoryData();   // 请求标签的数据
    }

    /**
     * 请求商品数据的方法
     */
    private void requestWaresData() {
        HttpRequestCenter.requestWares(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mWaresData = (WaresData) responseObj;
                if(mWaresData != null){
                    if(mWaresData.getList().size() > 0){
                        // 当商品数量大于 0 时加载商品相关视图
                        loadLevelTwoView();
                    }else {
                        // 当商品数量为0而此时状态为加载跟多模式的话，说明此时已经
                        // 无法下拉获得更多商品，使用 Toast 进行提示
                        if(mState == State.STATE_LOAD_MORE){
                            Toast.makeText(getActivity(), "没有更多数据了",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.finishRefreshLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, categoryId, currentPage);
    }

    /**
     * 请求标签的数据
     */
    private void requestCategoryData() {
        HttpRequestCenter.requestCategory(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mCategoryData = (CategoryData) responseObj;
                if(mCategoryData != null && mCategoryData.getCategories().size() > 0){
                    // 默认的商品为第一个标签下的视图
                    categoryId = mCategoryData.getCategories().get(0).getId();
                    requestWaresData();  // 只有请求标签数据成功之后才开始加载商品数据
                    loadLevelOneView();   // 加载标签相关的视图
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        });
    }

    /**
     * 加载标签数据相关视图
     */
    private void loadLevelOneView() {
        mCategoryAdapter = new CategoryAdapter(getContext(), mCategoryData.getCategories());
        // 设置点击事件监听
        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryData.getCategories().get(position);
                if(category != null){
                    categoryId = category.getId();
                    currentPage = 1;
                    mState = State.STATE_NORMAL;
                    requestWaresData();
                }
            }
        });
        mRecyclerViewOne.setAdapter(mCategoryAdapter);
        mRecyclerViewOne.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerViewOne.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 加载商品数据相关视图，共有 3 种模式
     */
    private void loadLevelTwoView() {
        switch (mState){
            case STATE_NORMAL:
                mWaresAdapter = new WaresAdapter(getContext(), mWaresData.getList());
                mRecyclerViewTwo.setAdapter(mWaresAdapter);
                mRecyclerViewTwo.setLayoutManager(new GridLayoutManager(getContext(), 2));
                if(mRecyclerViewTwo.getItemDecorationCount() == 0) {
                    mRecyclerViewTwo.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                break;
            case STATE_REFRESH:
                mWaresAdapter.clear();
                mWaresAdapter.addData(mWaresData.getList());
                mRecyclerViewTwo.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_LOAD_MORE:
                mWaresAdapter.addData(mWaresAdapter.getData().size(), mWaresData.getList());
                mRecyclerViewTwo.scrollToPosition(mWaresAdapter.getData().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_category_layout, container, false);

        initRecyclerView();
        initRefreshLayout();

        return mView;
    }

    /**
     * 初始化 {@link MaterialRefreshLayout}
     */
    private void initRefreshLayout() {
        mRefreshLayout = mView.findViewById(R.id.material_refresh_layout);
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                // 刷新商品数据
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                // 加载更多商品数据
                loadMoreData();
            }
        });
    }

    /**
     * 加载更多商品数据的方法
     */
    private void loadMoreData() {
        currentPage++;
        mState = State.STATE_LOAD_MORE;
        requestWaresData();
    }

    /**
     * 刷新商品数据的方法
     */
    private void refreshData() {
        currentPage = 1;
        mState = State.STATE_REFRESH;
        requestWaresData();
    }

    /**
     * 初始化 {@link RecyclerView}
     */
    private void initRecyclerView() {
        mRecyclerViewOne = mView.findViewById(R.id.recycler_view_level_one);
        mRecyclerViewTwo = mView.findViewById(R.id.recycler_view_level_two);
    }
}
