package com.marck.shoppingmall.activity.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.BaseActivity;
import com.marck.shoppingmall.fragment.impl.CartFragment;
import com.marck.shoppingmall.fragment.impl.CategoryFragment;
import com.marck.shoppingmall.fragment.impl.HomeFragment;
import com.marck.shoppingmall.fragment.impl.HotFragment;
import com.marck.shoppingmall.fragment.impl.MineFragment;
import com.marck.shoppingmall.model.Tab;
import com.marck.shoppingmall.widget.CustomToolbar;
import com.marck.shoppingmall.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: unique Activity in the Application, its responsibility
 * is displaying the different {@link android.support.v4.app.Fragment}.
 *
 * Created by Marck on 2019/03/07
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * TabHost 相关
     */
    private static final int TAB_COUNT = 5;  // Tab 的个数
    private List<Tab> mTabs = new ArrayList<>(TAB_COUNT);

    /**
     * UI 相关
     */
    private LayoutInflater mInflater;
    private FragmentTabHost mTabHost;
    private CustomToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        initToolbar();

    }

    private void initToolbar() {
        mToolbar = (CustomToolbar) findViewById(R.id.toolbar);
        mToolbar.setToolbarStyle(false);
        mToolbar.showEditButton(false);
        mToolbar.setTitle(R.string.title);
    }

    /**
     * 初始化 Tab 视图
     */
    private void initTab() {
        // 初始化相关数据
        Tab homeTab = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab hotTab = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab categoryTab = new Tab(R.string.category, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab cartTab = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab mineTab = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);

        mTabs.add(homeTab);
        mTabs.add(hotTab);
        mTabs.add(categoryTab);
        mTabs.add(cartTab);
        mTabs.add(mineTab);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.layout_fragment);

        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

    }

    /**
     * Help each Tab to build the indicator
     *
     * @param tab the tab
     * @return indicator view
     */
    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        TextView textView = (TextView) view.findViewById(R.id.tab_title);
        imageView.setBackgroundResource(tab.getIcon());
        textView.setText(tab.getTitle());
        return view;
    }

    /**
     * get {@link CustomToolbar} instance
     *
     * @return {@link #mToolbar}
     */
    public CustomToolbar getToolbar(){
        return mToolbar;
    }
}
