package com.marck.shoppingmall.adapter.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marck.shoppingmall.R;
import com.marck.shoppingmall.adapter.base.BaseRecyclerAdapter;
import com.marck.shoppingmall.model.homepager.CpOne;
import com.marck.shoppingmall.model.homepager.Recommend;
import com.squareup.picasso.Picasso;

/**
 * Created by Marck on 19/03/08
 */
public class HomePagerAdapter extends BaseRecyclerAdapter<Recommend> {

    public static final String TAG = HomePagerAdapter.class.getSimpleName();

    /**
     * Data
     */
    private static final int TYPE_ONE = 3;
    private static final int TYPE_TWO = 4;

    /**
     * UI 相关
     */
    private LayoutInflater mInflater;
    private Context mContext;
    private View mView;
    private OnImageClickListener mListener;

    public HomePagerAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_ONE){
            mView = mInflater.inflate(R.layout.item_home_cardview_one, parent, false);
        } else {
            mView = mInflater.inflate(R.layout.item_home_cardview_two, parent, false);
        }
        return new ViewHolder(mView);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Recommend recommend) {
        ViewHolder holder = (ViewHolder) viewHolder;
        Picasso.with(mContext).load(recommend.getCpOne().getImgUrl()).into(holder.bigImageView);
        Picasso.with(mContext).load(recommend.getCpTwo().getImgUrl()).into(holder.topImageView);
        Picasso.with(mContext).load(recommend.getCpThree().getImgUrl()).into(holder.bottomImageView);
        holder.title.setText(recommend.getTitle());
    }

    @Override
    public int getItemViewType(int position) {
        if(getHeaderView() != null){
            if(position == 0) {
                return BaseRecyclerAdapter.TYPE_HEADER;
            }else {
                return position % 2 != 0 ? TYPE_ONE : TYPE_TWO;
            }
        }else {
            return position % 2 != 0 ? TYPE_ONE : TYPE_TWO;
        }
    }

    public void setOnImageClickListener(OnImageClickListener listener){
        this.mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView bigImageView;
        ImageView topImageView;
        ImageView bottomImageView;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_view_text);
            bigImageView = (ImageView) itemView.findViewById(R.id.image_view_big);
            topImageView = (ImageView) itemView.findViewById(R.id.image_view_small_top);
            bottomImageView = (ImageView) itemView.findViewById(R.id.image_view_small_bottom);
            bigImageView.setOnClickListener(this);
            topImageView.setOnClickListener(this);
            bottomImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "click");
            if(mListener != null){
                Log.d(TAG, "click");
                startAnim(v);
            }
        }

        private void startAnim(final View view) {
            ObjectAnimator anim = ObjectAnimator
                    .ofFloat(view, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    int campaignId;
                    switch (view.getId()){
                        case R.id.image_view_big:
                            campaignId = getData().get(getLayoutPosition()).getCpOne().getId();
                            mListener.onImageClick(view, campaignId);
                            break;
                        case R.id.image_view_small_top:
                            campaignId = getData().get(getLayoutPosition()).getCpTwo().getId();
                            mListener.onImageClick(view, campaignId);
                            break;
                        case R.id.image_view_small_bottom:
                            campaignId = getData().get(getLayoutPosition()).getCpThree().getId();
                            mListener.onImageClick(view, campaignId);
                            break;
                    }
                }
            });
            anim.start();
        }
    }

    public interface OnImageClickListener{
        void onImageClick(View view, int campaignId);
    }
}
