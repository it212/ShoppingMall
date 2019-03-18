package com.marck.shoppingmall.adapter.hot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.model.cartpager.CartData;
import com.marck.shoppingmall.model.hotpager.HotSale;
import com.marck.shoppingmall.util.CartProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Marck on 19/03/09
 */
public class HotPagerAdapter extends RecyclerView.Adapter<HotPagerAdapter.ViewHolder> {

    public static final String TAG = HotPagerAdapter.class.getSimpleName();

    /**
     * UI
     */
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private OnItemClickListener mListener;

    /**
     * Data
     */
    private List<HotSale> mData;

    /**
     * Support
     */
    private CartProvider mCartProvider;

    public HotPagerAdapter(Context context, List<HotSale> data){
        this.mContext = context;
        this.mData = data;
        this.mCartProvider = CartProvider.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        mView = mInflater.inflate(R.layout.item_hot_layout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HotSale hotSale = mData.get(position);
        Picasso.with(mContext).load(hotSale.getImgUrl()).into(holder.imageView);
        holder.titleText.setText(hotSale.getName());
        holder.priceText.setText(mContext.getString(R.string.rmb).concat(hotSale.getPrice() + ""));
        holder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "商品已添加到购物车中！", Toast.LENGTH_SHORT).show();
                mCartProvider.put(hotSale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clear(){
        int count = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void addData(List<HotSale> data){
        addData(0, data);
    }

    public void addData(int position, List<HotSale> data){
        if(mData !=null) {
            this.mData.addAll(data);
            notifyItemRangeChanged(position, data.size());
        }
    }

    public List<HotSale> getData(){
        return mData;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView  titleText;
        TextView  priceText;
        Button    buyButton;
        View      parentView;

        ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.hot_image_view);
            titleText = (TextView) itemView.findViewById(R.id.text_title);
            priceText = (TextView) itemView.findViewById(R.id.text_price);
            buyButton = (Button) itemView.findViewById(R.id.buy_button);
            parentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
