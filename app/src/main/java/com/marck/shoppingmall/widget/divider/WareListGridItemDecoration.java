package com.marck.shoppingmall.widget.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class WareListGridItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = 6;
        outRect.bottom = 6;
        outRect.left = 2;
        outRect.right = 2;
    }
}
