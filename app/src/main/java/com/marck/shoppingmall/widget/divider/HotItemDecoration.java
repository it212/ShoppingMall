package com.marck.shoppingmall.widget.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HotItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = 2;
    }
}
