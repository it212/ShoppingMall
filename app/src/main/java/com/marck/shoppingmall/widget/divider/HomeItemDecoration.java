package com.marck.shoppingmall.widget.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description: The divider for {@link RecyclerView}.
 *
 * Created by Marck on 2019/03/08
 */
public class HomeItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // we leave the first child because it is a Header View
        if(parent.getChildAdapterPosition(view) != 0){
            outRect.top = 8;
            outRect.bottom = 8;
            outRect.left = 8;
            outRect.right = 8;
        }
    }
}
