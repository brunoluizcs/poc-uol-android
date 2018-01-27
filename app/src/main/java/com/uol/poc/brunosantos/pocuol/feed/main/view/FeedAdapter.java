package com.uol.poc.brunosantos.pocuol.feed.main.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.repository.FeedRepositoryManager;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;
import com.uol.poc.brunosantos.pocuol.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunosantos on 27/01/2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>{

    private Cursor mCursor;
    private int mLastPosition = RecyclerView.NO_POSITION;

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bind(FeedRepositoryManager.getNews(mCursor));

        Animation animation = AnimationUtils.loadAnimation(holder.mContext,
                (position > mLastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        mLastPosition = position;


    }

    @Override
    public void onViewDetachedFromWindow(FeedViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_thumb) ImageView mThumbImageView;
        @BindView(R.id.tv_title) TextView mTitleTextView;
        @BindView(R.id.tv_update) TextView mUpdateTextView;

        Context mContext;

        FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mContext = itemView.getContext();
        }

        void bind(News news){
            Glide.with(mContext)
                    .load(news.getThumb())
                    .into(mThumbImageView);

            mTitleTextView.setText(news.getTitle());
            mUpdateTextView.setText(DateUtils.parseDate(news.getUpdated()));


        }

    }
}
