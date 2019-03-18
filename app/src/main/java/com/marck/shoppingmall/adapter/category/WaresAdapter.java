package com.marck.shoppingmall.adapter.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.model.categorypager.Category;
import com.marck.shoppingmall.model.categorypager.Wares;
import com.marck.shoppingmall.model.hotpager.HotSale;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Marck on 19/03/09
 */
public class WaresAdapter extends RecyclerView.Adapter<WaresAdapter.ViewHolder> {

    private static final String TAG = WaresAdapter.class.getSimpleName();

    /**
     * UI
     */
    private OnItemClickListener mListener;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;

    /**
     * Data
     */
    private List<Wares> mData;

    public WaresAdapter(Context context, List<Wares> data){
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = mInflater.inflate(R.layout.item_category_two, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wares wares = mData.get(position);
        holder.mTitleView.setText(wares.getName());
        holder.mPriceView.setText(mContext.getString(R.string.rmb).concat(wares.getPrice() + ""));
        Picasso.with(mContext).load(wares.getImgUrl()).into(holder.mImageView);
    }

    public void clear(){
        int count = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void addData(List<Wares> data){
        addData(0, data);
    }

    public void addData(int position, List<Wares> data){
        if(mData !=null) {
            this.mData.addAll(data);
            Log.d(TAG, mData.toString());
            notifyItemRangeChanged(position, data.size());
        }
    }

    public List<Wares> getData(){
        return mData;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTitleView;
        TextView mPriceView;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.category_title_text);
            mImageView = (ImageView) itemView.findViewById(R.id.category_image_view);
            mPriceView = (TextView) itemView.findViewById(R.id.category_price_text);
        }
    }

    public interface OnItemClickListener{
        void setOnItemClickListener(View view, int position);
    }
}
