package com.marck.shoppingmall.model.cartpager;

import com.marck.shoppingmall.model.categorypager.Wares;

import java.util.List;

public class CartData extends Wares {

    private int count;

    private boolean isChecked=true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
