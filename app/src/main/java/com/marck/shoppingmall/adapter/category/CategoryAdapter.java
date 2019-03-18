package com.marck.shoppingmall.adapter.category;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.model.categorypager.Category;

import java.util.List;

/**
 * Created by Marck on 19/03/09
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    /**
     * Data
     */
    private List<Category> mData;

    /**
     * UI
     */
    private OnItemClickListener mListener;
    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;

    public CategoryAdapter(Context context, List<Category> data){
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = mInflater.inflate(R.layout.item_category_one, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Category category = mData.get(position);
        holder.mTextView.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextView;
        View parentView;

        ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.category_text_view);
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
