package com.marck.shoppingmall.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.marck.shoppingmall.R;
import com.marck.shoppingmall.model.homepager.Head;

import java.util.List;

public class CustomSliderView extends RelativeLayout {

    private static final String TAG = CustomSliderView.class.getSimpleName();

    private Context mContext;
    private RelativeLayout mSliderView;
    private SliderLayout mSliderLayout;
    private PagerIndicator mPagerIndicator;

    private List<Head> mData;

    public CustomSliderView(Context context, List<Head> data) {
        super(context);
        this.mContext = context;
        this.mData = data;
        initView();
    }

    public void recycle(){
        mSliderLayout.stopAutoCycle();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mSliderView = (RelativeLayout) inflater.inflate(R.layout.slider_view, this);
        mSliderLayout = (SliderLayout) mSliderView.findViewById(R.id.slider);
        mSliderLayout.setDuration(2000);
        mSliderLayout.addOnPageChangeListener(mPagerIndicator);
        mPagerIndicator = (PagerIndicator) mSliderView.findViewById(R.id.custom_indicator);
        if(mData != null) {
            for (Head head : mData) {
                TextSliderView textSliderView = new TextSliderView(mContext);
                textSliderView.description(head.getName());
                textSliderView.image(head.getImgUrl());
                mSliderLayout.addSlider(textSliderView);
            }
        }
    }


}
