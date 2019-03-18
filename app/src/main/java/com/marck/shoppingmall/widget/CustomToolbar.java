package com.marck.shoppingmall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.marck.shoppingmall.R;

public class CustomToolbar extends Toolbar implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View mView;
    private ImageButton mLeftButton;
    private ImageButton mRightButton;
    private TextView mTitleView;
    private EditText mSearchText;
    private Button mEditButton;

    private OnButtonClickListener mListener;

    public CustomToolbar(Context context) {
        this(context, null, 0);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();
        setContentInsetsRelative(10, 10);
    }

    @Override
    public void setTitle(CharSequence title){
        if(mTitleView != null){
            mTitleView.setText(title);
        }

    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left_button:
                if(this.mListener != null){
                    this.mListener.onLeftButtonClick(v);
                }
                break;
            case R.id.toolbar_right_button:
                if(this.mListener != null){
                    this.mListener.onRightButtonClick(v);
                }
                break;
            case R.id.toolbar_edit_button:
                if(this.mListener != null){
                    this.mListener.onEditButtonClick(v);
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        if(mView == null) {
            // Initialize the view of toolbar
            mView = mInflater.inflate(R.layout.toolbar_layout, null);
            mTitleView = (TextView) mView.findViewById(R.id.toolbar_title_text);
            mSearchText = (EditText) mView.findViewById(R.id.toolbar_search_bar);
            mLeftButton = (ImageButton) mView.findViewById(R.id.toolbar_left_button);
            mRightButton = (ImageButton) mView.findViewById(R.id.toolbar_right_button);
            mEditButton = (Button) mView.findViewById(R.id.toolbar_edit_button);
            mRightButton.setOnClickListener(this);
            mEditButton.setOnClickListener(this);
            mLeftButton.setOnClickListener(this);
            // add mView to toolbar
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            addView(mView, params);
        }
    }

    /**
     * The method to set style of {@link CustomToolbar}
     *
     * @param showTitle true if you want to show the title and hide the search bar,
     *                    false if you want to hide the title and show the search bar.
     */
    public void setToolbarStyle(boolean showTitle){
        // if mTitleView or mSearchText is null, throw NullPointerException directly.
        if(mTitleView == null || mSearchText == null){
            throw new NullPointerException("Do you forget to initialize the toolbar?");
        }

        // we want to show the search bar.
        if(!showTitle){
            mSearchText.setVisibility(VISIBLE);
            mTitleView.setVisibility(GONE);
        } else {
            mSearchText.setVisibility(GONE);
            mTitleView.setVisibility(VISIBLE);
        }
    }

    public void showEditButton(boolean show){
        if(show){
            mEditButton.setVisibility(VISIBLE);
            mRightButton.setVisibility(GONE);
        }else {
            mEditButton.setVisibility(GONE);
            mRightButton.setVisibility(VISIBLE);
        }
    }

    public void setEditButtonText(int  res){
        if(mEditButton != null){
            mEditButton.setText(res);
        }
    }

    public void setEditButtonText(String text){
        if(mEditButton != null && !"".equals(text)){
            mEditButton.setText(text);
        }
    }

    public void setRightButtonBackground(int res) {
        if(mRightButton != null){
            mRightButton.setBackgroundResource(res);
        }
    }

    public void setLeftButtonBackground(int res) {
        if(mLeftButton != null){
            mLeftButton.setBackgroundResource(res);
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.mListener = listener;
    }

    public interface OnButtonClickListener{
        void onLeftButtonClick(View view);
        void onRightButtonClick(View view);
        void onEditButtonClick(View view);
    }
}
