package com.marck.shoppingmall.activity.impl;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.BaseActivity;
import com.marck.shoppingmall.adapter.warelist.WareListAdapter;
import com.marck.shoppingmall.model.hotpager.HotPagerData;
import com.marck.shoppingmall.network.http.HttpRequestCenter;
import com.marck.shoppingmall.network.okhttp.listener.DisposeDataListener;
import com.marck.shoppingmall.util.Constants;
import com.marck.shoppingmall.widget.CustomToolbar;
import com.marck.shoppingmall.widget.divider.DividerItemDecoration;
import com.marck.shoppingmall.widget.divider.WareListGridItemDecoration;

/**
 * Description: {@link WareListActivity} responsibility is to show the ware list,
 * when we click the item of the list, we can jump to the {@link WareDetailActivity}.
 *
 * Created by Marck on 19/03/10
 */
public class WareListActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener, WareListAdapter.OnItemClickListener {

    private static final String TAG = WareListActivity.class.getSimpleName();

    /**
     * UI 相关
     */
    private RecyclerView mRecyclerView;
    private CustomToolbar mToolbar;
    private MaterialRefreshLayout mRefreshLayout;
    private TabLayout mTabLayout;
    private TextView mWareCountView;
    private WareListAdapter mWareListAdapter;

    /**
     * Data
     */
    private HotPagerData mData;
    private int campaignId;
    private int currentPage = 1;
    private int orderBy = 0;
    private int totalCount = 0;

    /**
     * Order by
     */
    public static final int ORDER_DEFAULT = 0;
    public static final int ORDER_PRICE = 2;
    public static final int ORDER_SALE = 1;

    enum State{
        STATE_NORMAL,     // 普通加载状态
        STATE_REFRESH,    // 刷新状态
        STATE_LOAD_MORE   // 加载更多
    }
    private State mState = State.STATE_NORMAL; // 默认为普通加载状态

    enum Style{
        STYLE_GRID,  // 网格布局
        STYLE_LIST   // 线性布局
    }
    private Style mStyle = Style.STYLE_LIST;  // 默认为线性垂直布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        initView();
        initToolbar();
        initTab();
        initRefreshLayout();
        campaignId = getIntent().getIntExtra(Constants.CAMPAIGN_ID, 0);
        requestWareListData();
    }

    @Override
    public void onItemClickListener(View view, int position) {
        // 将 ware 信息存进 Intent 中传给 WareDetailActivity
        Intent intent = new Intent(this, WareDetailActivity.class);
        intent.putExtra(Constants.WARES_ID, mData.getList().get(position));
        startActivity(intent);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mTabLayout = (TabLayout) findViewById(R.id.ware_list_tab_layout);
        mToolbar = (CustomToolbar) findViewById(R.id.ware_list_toolbar);
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.ware_list_refresh_layout);
        mWareCountView = (TextView) findViewById(R.id.ware_list_text_summary);
    }

    /**
     * 初始化 Toolbar
     */
    private void initToolbar() {
        mToolbar.setTitle(R.string.title);
        mToolbar.setToolbarStyle(true);
        mToolbar.setRightButtonBackground(R.drawable.icon_list_32);
        mToolbar.setOnButtonClickListener(new CustomToolbar.OnButtonClickListener() {
            @Override
            public void onLeftButtonClick(View view) {
                finish();
            }

            @Override
            public void onRightButtonClick(View view) {
                // 线性处置布局和网格布局模式切换
                if(mStyle == Style.STYLE_LIST){
                    mStyle = Style.STYLE_GRID;
                    mWareListAdapter.resetLayout(R.layout.item_wares_grid_layout);
                    // 重新移除添加分割线
                    if(mRecyclerView.getItemDecorationCount() == 1) {
                        mRecyclerView.removeItemDecorationAt(0);
                        mRecyclerView.addItemDecoration(new WareListGridItemDecoration());
                    }
                    mRecyclerView.setAdapter(mWareListAdapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(WareListActivity.this, 2));
                    mToolbar.setRightButtonBackground(R.drawable.icon_grid_32);
                }else {
                    mStyle = Style.STYLE_LIST;
                    mWareListAdapter.resetLayout(R.layout.item_hot_layout);
                    // 重新移除添加分割线
                    if(mRecyclerView.getItemDecorationCount() == 1) {
                        mRecyclerView.removeItemDecorationAt(0);
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                                WareListActivity.this, DividerItemDecoration.VERTICAL_LIST));
                    }
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(WareListActivity.this));
                    mRecyclerView.setAdapter(mWareListAdapter);
                    mToolbar.setRightButtonBackground(R.drawable.icon_list_32);
                }
            }

            @Override
            public void onEditButtonClick(View view) {
                // do nothing
            }
        });
    }

    /**
     * 初始化 {@link TabLayout}
     */
    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(ORDER_DEFAULT);
        mTabLayout.addTab(tab);

        tab= mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(ORDER_PRICE);
        mTabLayout.addTab(tab);

        tab= mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(ORDER_SALE);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    /**
     * 初始化 {@link MaterialRefreshLayout}
     */
    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                // 刷新数据
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                // 加载更多数据
                loadMoreData();
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        currentPage++;  // 请求下一页的数据
        mState = State.STATE_LOAD_MORE;
        requestWareListData();  // 网络请求数据
    }

    /**
     * 网络请求数据
     */
    private void requestWareListData() {
        HttpRequestCenter.requestWareListData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mData = (HotPagerData) responseObj;
                if(mData != null){
                    currentPage = mData.getCurrentPage();
                    totalCount = mData.getTotalCount();
                    String s = "共有" + totalCount + "件商品";
                    mWareCountView.setText(s);
                    // 当商品数量大于0时才加载商品视图
                    if(mData.getList().size() > 0) {
                        loadView();
                    }else {
                        // 当商品数量为0而此时状态为加载跟多模式的话，说明此时已经
                        // 无法下拉获得更多商品，使用 Toast 进行提示
                        if(mState == State.STATE_LOAD_MORE){
                            Toast.makeText(WareListActivity.this, "没有更多了",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.finishRefreshLoadMore();
                            mState = State.STATE_NORMAL;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        }, campaignId, currentPage, orderBy);

    }

    /**
     * 加载视图的方法，分为 3 种模式进行加载
     */
    private void loadView() {
        switch (mState){
            case STATE_NORMAL:
                mWareListAdapter = new WareListAdapter(this, mData.getList(), R.layout.item_hot_layout);
                mWareListAdapter.setOnItemClickListener(this);
                mRecyclerView.setAdapter(mWareListAdapter);
                if(mRecyclerView.getItemDecorationCount() == 0) {
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(
                            WareListActivity.this, DividerItemDecoration.VERTICAL_LIST));
                }
                mRecyclerView.setLayoutManager(new LinearLayoutManager(WareListActivity.this));
                mToolbar.setRightButtonBackground(R.drawable.icon_list_32);
                break;
            case STATE_REFRESH:
                mWareListAdapter.clear();
                mWareListAdapter.addData(mData.getList());
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                mState = State.STATE_NORMAL;
                break;
            case STATE_LOAD_MORE:
                mWareListAdapter.addData(mWareListAdapter.getData().size(), mData.getList());
                mRecyclerView.scrollToPosition(mWareListAdapter.getData().size());
                mRefreshLayout.finishRefreshLoadMore();
                mState = State.STATE_NORMAL;
                break;
        }
    }

    /**
     * 刷新数据的方法
     */
    private void refreshData() {
        currentPage = 1;
        totalCount = 0;
        mState = State.STATE_REFRESH;
        requestWareListData();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        totalCount = 0;
        refreshData();  // 刷新视图
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
