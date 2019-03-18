package com.marck.shoppingmall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marck.shoppingmall.R;

public class CustomAddSubButton extends LinearLayout implements View.OnClickListener {

    public static final String TAG = CustomAddSubButton.class.getSimpleName();

    private static final int DEFAULT_MAX = 100;

    private LayoutInflater mInflater;
    private Button addButton;
    private TextView numberText;
    private Button subButton;

    private onButtonClickListener mListener;

    private int value;
    private int minValue = 0;
    private int maxValue = DEFAULT_MAX;

    public CustomAddSubButton(Context context) {
        this(context, null, 0);
    }

    public CustomAddSubButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CustomAddSubButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();
        if(attrs != null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomAddSubButton, defStyleAttr, 0);
            int val = a.getInt(R.styleable.CustomAddSubButton_value, 0);
            int minVal = a.getInt(R.styleable.CustomAddSubButton_minValue, 0);
            int maxVal = a.getInt(R.styleable.CustomAddSubButton_maxValue, 0);
            Drawable numTextBg = a.getDrawable(R.styleable.CustomAddSubButton_textBackground);
            Drawable addBtnBg = a.getDrawable(R.styleable.CustomAddSubButton_addButtonBackground);
            Drawable subBtnBg = a.getDrawable(R.styleable.CustomAddSubButton_subButtonBackground);
            setValue(minVal, val, maxVal);
            setTextBackground(numTextBg);
            setAddButtonBackground(addBtnBg);
            setSubButtonBackground(subBtnBg);
            a.recycle();
        }
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.widget_add_sub_layout, this, true);
        addButton = (Button) view.findViewById(R.id.widget_button_add);
        subButton = (Button) view.findViewById(R.id.widget_button_sub);
        numberText = (TextView) view.findViewById(R.id.widget_text_num);

        addButton.setOnClickListener(this);
        subButton.setOnClickListener(this);
    }

    public void setValue(int minValue, int value, int maxValue){
        this.minValue = minValue;
        this.value = value;
        this.maxValue = maxValue;
        numberText.setText(Integer.toString(value));
    }

    public void setTextBackground(Drawable drawable){
        if(numberText != null && drawable != null){
            numberText.setBackground(drawable);
        }
    }

    public void setAddButtonBackground(Drawable drawable){
        if(addButton != null && drawable != null){
            addButton.setBackground(drawable);
        }
    }

    public void setSubButtonBackground(Drawable drawable){
        if(subButton != null && drawable != null){
            subButton.setBackground(drawable);
        }
    }

    public void setOnButtonListener(onButtonClickListener listener){
        this.mListener = listener;
    }

    private int getValue(){
        String valString = numberText.getText().toString();
        if(!"".equals(valString)){
           this.value = Integer.parseInt(valString);
        }
        return this.value;
    }

    private void addNumber(){
        int currentVal = getValue();
        if(currentVal < this.maxValue){
            this.value++;
        }
        numberText.setText(Integer.toString(value));
    }

    private void subNumber(){
        int currentVal = getValue();
        if(currentVal > this.minValue){
            this.value--;
        }
        numberText.setText(Integer.toString(value));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.widget_button_add:
                addNumber();
                if(mListener != null){
                    mListener.onAddClickListener(v,value);
                }
                break;
            case R.id.widget_button_sub:
                subNumber();
                if(mListener != null){
                    mListener.onSubClickListener(v, value);
                }
                break;
        }
    }

    public interface onButtonClickListener{
        void onAddClickListener(View view, int value);
        void onSubClickListener(View view, int value);
    }
}
