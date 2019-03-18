package com.marck.shoppingmall.adapter.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.model.cartpager.CartData;
import com.marck.shoppingmall.util.CartProvider;
import com.marck.shoppingmall.widget.CustomAddSubButton;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Marck on 19/03/11
 */
public class CartPagerAdapter extends RecyclerView.Adapter<CartPagerAdapter.ViewHolder> {

    private static final String TAG = CartPagerAdapter.class.getSimpleName();

    /**
     * UI 相关
     */
    private Context mContext;
    private TextView mTotalPriceView;  // parent's textView
    private List<CartData> mData;
    private CheckBox mSelectAllBox;    // parent's checkBox

    /**
     * Support
     */
    private CartProvider mCartProvider;

    /**
     * Data
     */
    private double totalPrice = 0.0;

    public CartPagerAdapter(Context context, List<CartData> data, CheckBox selectAllBox, TextView textView){
        this.mContext = context;
        this.mData = data;
        this.mTotalPriceView = textView;
        mCartProvider = CartProvider.getInstance(context);
        setCheckBox(selectAllBox);  // 设置选中所有按钮相关的配置
        updateTotalPrice();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CartData cart = mData.get(position);
        holder.mPriceView.setText(mContext.getString(R.string.rmb).concat(cart.getPrice() + ""));
        holder.mTitleView.setText(cart.getName());
        Picasso.with(mContext).load(cart.getImgUrl()).into(holder.mImageView);
        if(cart.getCount() == 0) {
            // 当商品选择数量为 0 时直接设置为补选中状态
            holder.mCheckBox.setChecked(false);
            mData.get(position).setIsChecked(false);
            mCartProvider.update(cart);
        }else {
            holder.mCheckBox.setChecked(cart.isChecked());
        }
        holder.mAddSubButton.setValue(0, cart.getCount(), 100);
        // 对商品数量加减按钮设置事件监听
        holder.mAddSubButton.setOnButtonListener(new CustomAddSubButton.onButtonClickListener() {
            @Override
            public void onAddClickListener(View view, int value) {
                cart.setCount(value);
                if(value == 1){
                    cart.setIsChecked(true);
                    holder.mCheckBox.setChecked(true);
                }
                mCartProvider.update(cart);
                updateTotalPrice();
            }

            @Override
            public void onSubClickListener(View view, int value) {
                cart.setCount(value);
                if(value == 0){
                    cart.setIsChecked(false);
                    holder.mCheckBox.setChecked(false);
                }
                mCartProvider.update(cart);
                updateTotalPrice();
            }
        });
        // 设置商品 item 的点击事件监听
        holder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = holder.mCheckBox.isChecked();
                // 每次点击时翻转商品的选中状态
                holder.mCheckBox.setChecked(!isCheck);
                cart.setIsChecked(!isCheck);
                mCartProvider.update(cart);
                updateTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void setCheckBox(CheckBox checkBox) {
        this.mSelectAllBox = checkBox;
        mSelectAllBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 子项按钮跟随 selectAll 的状态进行变化
                selectAllOrNone(mSelectAllBox.isChecked());
                updateTotalPrice();
            }
        });
    }

    /**
     * 选中或不选中所有的 item 设置
     * @param checked checked or not
     */
    public void selectAllOrNone(boolean checked) {
        if(mData == null || mData.size() <= 0){
            return;
        }
        int i = 0;
        for (CartData cart : mData){
            cart.setIsChecked(checked);
            i++;
        }
        notifyItemRangeChanged(0, mData.size());
    }

    /**
     * remove the cart item if {@link com.marck.shoppingmall.fragment.impl.CartFragment} is in
     * edit mode. If the item is cheese, it's checkBox is true, what we need is delete the item
     * if its checkBox is true and update the price view.
     */
    public void removeCartItem(){
        if(mData != null && mData.size() > 0){
            // 使用迭代器进行迭代
            for (Iterator iterator = mData.iterator(); iterator.hasNext();){
                CartData item = (CartData) iterator.next();
                if(item.isChecked()){
                    // 被选中的时候从数据库和 mData 中同时进行移除
                    int position = mData.indexOf(item);
                    mCartProvider.delete(item);
                    iterator.remove();
                    notifyItemRemoved(position);
                }else {
                    // 如果没有被选中，为了在完成编辑之后能够正确显示价格，需要对
                    // 未选中的 item 进行一次选中更新
                    item.setIsChecked(true);
                    mCartProvider.update(item);
                }
            }
            // 更新价格所在视图
            updateTotalPrice();
        }
    }

    /**
     * Update the {@link #mTotalPriceView} if we change the item count.
     */
    private void updateTotalPrice() {
        float sum = 0;
        if(mData != null && mData.size() > 0){
            for (CartData cart : mData){
                // 当被选中时，计算价钱
                if(cart.isChecked()){
                    sum += (cart.getCount() * cart.getPrice());
                }
            }
            totalPrice = sum;
            mTotalPriceView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>"
                    + totalPrice + "</span>"), TextView.BufferType.SPANNABLE);
        }else {
            // 如果 mData 为空，直接显示总价格为 0
            mTotalPriceView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>0.0</span>"), TextView.BufferType.SPANNABLE);
        }
    }

    public double getTotalPrice(){
        return totalPrice;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView mTitleView;
        TextView mPriceView;
        CustomAddSubButton mAddSubButton;
        private View mParentView;

        ViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cart_check_box);
            mTitleView = (TextView) itemView.findViewById(R.id.cart_title_text);
            mPriceView = (TextView) itemView.findViewById(R.id.cart_price_text);
            mImageView = (ImageView) itemView.findViewById(R.id.cart_image_view);
            mAddSubButton = (CustomAddSubButton) itemView.findViewById(R.id.cart_add_sub_button);
        }
    }
}
