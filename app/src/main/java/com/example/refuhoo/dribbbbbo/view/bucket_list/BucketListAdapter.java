package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Bucket;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.view.shot_list.ShotListAdapter;

import java.util.List;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class BucketListAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_BUCKET_LIST = 0;
    private final int VIEW_TYPE_PROGRESS_BAR = 1;

    private List<Bucket> buckets;
    private boolean showLoading;
    private LoadMoreListener listener;
    private boolean isChoosingMode;

    public BucketListAdapter(List<Bucket> data, LoadMoreListener l, boolean isChoosingMode){
        buckets = data;
        showLoading = true;
        listener = l;
        this.isChoosingMode = isChoosingMode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_BUCKET_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bucket, parent, false);
                return new BucketViewHolder(view);
            case VIEW_TYPE_PROGRESS_BAR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
                return new RecyclerView.ViewHolder(view){};
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_PROGRESS_BAR){
            listener.onLoadMore();
        } else {
            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            Context context = holder.itemView.getContext();
            final Bucket bucket = buckets.get(position);

            bucketViewHolder.bucketName.setText(bucket.name);
            bucketViewHolder.bucketShotCount.setText(String.valueOf(bucket.shots_count));

            if(isChoosingMode) {
                bucketViewHolder.bucketShotChosen.setVisibility(View.VISIBLE);
                bucketViewHolder.bucketShotChosen.setImageDrawable(
                        bucket.isChoosing ?
                                ContextCompat.getDrawable(context, R.drawable.ic_check_box_pink_600_24dp)
                                : ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank_black_24dp));
                bucketViewHolder.bucketName.setTextColor(ContextCompat.
                        getColor(context, bucket.isChoosing ? R.color.colorPrimary
                                                            : R.color.colorAccent));

                bucketViewHolder.bucketCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("mengjie", "clicked bucketcover");
                        bucket.isChoosing = ! bucket.isChoosing;
                        notifyDataSetChanged();
                    }
                });
            } else {
                bucketViewHolder.bucketShotChosen.setVisibility(View.INVISIBLE);
                bucketViewHolder.bucketCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // need to call shotlistFragment inside the same activity.
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (showLoading) ? buckets.size() + 1 : buckets.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == buckets.size()){
            return VIEW_TYPE_PROGRESS_BAR;
        } else {
            return VIEW_TYPE_BUCKET_LIST;
        }
    }

    public List<Bucket> getData() {
        return buckets;
    }

    public interface LoadMoreListener{
        void onLoadMore();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void appendData(List<Bucket> bucket){
        buckets.addAll(bucket);
        notifyDataSetChanged();
    }

    public void prependData(Bucket bucket){
        buckets.add(0, bucket);
        notifyDataSetChanged();
    }

    public int bucketSize(){
        return buckets.size();
    }
}
