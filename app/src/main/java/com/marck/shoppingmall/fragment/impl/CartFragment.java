package com.marck.shoppingmall.fragment.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.activity.impl.MainActivity;
import com.marck.shoppingmall.adapter.cart.CartPagerAdapter;
import com.marck.shoppingmall.fragment.BaseFragment;
import com.marck.shoppingmall.model.cartpager.CartData;
import com.marck.shoppingmall.util.CartProvider;
import com.marck.shoppingmall.widget.CustomToolbar;

import java.util.List;

/**
 * Created by Marck on 19/03/11
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = CartFragment.class.getSimpleName();

    /**
     * UI 相关
     */
    private RecyclerView mRecyclerView;
    private CheckBox mSelectAllBox;   // Fragment 的控件
    private TextView mTotalPriceView; // Fragment 的控件
    private Button mSettleButton;
    private Button mDeleteButton;
    private View mView;
    private CustomToolbar mToolbar;   // Activity 的控件
    private MainActivity mMainActivity;

    /**
     * Support
     */
    private CartPagerAdapter mCartPagerAdapter;
    private CartProvider mCartProvider;

    /**
     * State
     */
    enum State{
        STATE_NORMAL,  // 普通模式
        STATE_EDIT     // 编辑模式，可进行删除
    }
    private State mState = State.STATE_NORMAL;  // 默认为普通模式

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCartProvider = CartProvider.getInstance(getContext());
        changeToolbar();  // Toolbar 做相应的变化
    }

    /**
     * 改变 {@link MainActivity} Toolbar 显示的方法
     */
    private void changeToolbar() {
        mMainActivity = (MainActivity) getActivity();
        mToolbar = mMainActivity.getToolbar();
        mToolbar.setToolbarStyle(true);
        mToolbar.showEditButton(true);
        mToolbar.setOnButtonClickListener(new CustomToolbar.OnButtonClickListener() {
            @Override
            public void onLeftButtonClick(View view) {
                // do nothing
            }

            @Override
            public void onRightButtonClick(View view) {
                // do nothing, it is invisible
            }

            @Override
            public void onEditButtonClick(View view) {
                // 编辑模式的切换
                if(mState == State.STATE_NORMAL) {
                    // open edit mode, wo can edit and delete the ware
                    mState = State.STATE_EDIT;
                    mToolbar.setEditButtonText(R.string.complete);
                    mCartPagerAdapter.selectAllOrNone(false);
                    mSelectAllBox.setChecked(false);
                    mSettleButton.setVisibility(View.GONE);
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mTotalPriceView.setVisibility(View.GONE);
                } else {
                    // completed, now we go back to normal state
                    mState = State.STATE_NORMAL;
                    mToolbar.setEditButtonText(R.string.edit);
                    mCartPagerAdapter.selectAllOrNone(true);
                    mSelectAllBox.setChecked(true);
                    mSettleButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setVisibility(View.GONE);
                    mTotalPriceView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_layout, container, false);
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // release the reference to help the GC
        mToolbar = null;
        mMainActivity = null;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        // 切换相关状态的时候 mToolbar 需要做的相应变化
        if(!hidden){
            mToolbar.setToolbarStyle(true);
            mToolbar.showEditButton(true);
            reloadView();
        }else {
            mToolbar.setToolbarStyle(false);
            mToolbar.showEditButton(false);
        }
    }

    /**
     * 初始化相关视图
     */
    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mSelectAllBox = (CheckBox) mView.findViewById(R.id.cart_check_box_all);
        mTotalPriceView = (TextView) mView.findViewById(R.id.cart_total_text);
        mSettleButton = (Button) mView.findViewById(R.id.cart_settle_button);
        mDeleteButton = (Button) mView.findViewById(R.id.cart_delete_button);
        mSettleButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    /**
     * 加载相关的视图
     */
    private void loadView(){
        // 从本地数据库中获取数据
        List<CartData> data = mCartProvider.getAll();
        if(data != null && data.size() > 0){
            mCartPagerAdapter = new CartPagerAdapter(getContext(), data, mSelectAllBox, mTotalPriceView);
            mRecyclerView.setAdapter(mCartPagerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
    }

    /**
     * 重新加载数据
     */
    private void reloadView() {
        // 从本地数据库中获取数据
        List<CartData> data = mCartProvider.getAll();
        if(data != null && data.size() > 0){
            mCartPagerAdapter = new CartPagerAdapter(getContext(), data, mSelectAllBox, mTotalPriceView);
            mCartPagerAdapter.notifyItemRangeChanged(0, data.size());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            mRecyclerView.setAdapter(mCartPagerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cart_settle_button:
                Toast.makeText(getContext(), "去结算", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart_delete_button:
                // 删除按钮点击事件，在编辑模式下按下会删除所有被选中的商品
                mCartPagerAdapter.removeCartItem();
                break;
        }
    }
}
