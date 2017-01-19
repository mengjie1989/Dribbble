package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Bucket;

import java.util.List;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class BucketListAdapter extends RecyclerView.Adapter {

    private List<Bucket> buckets;
    public BucketListAdapter(List<Bucket> data){
        buckets = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bucket, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
        Bucket bucket = buckets.get(position);

        bucketViewHolder.bucketName.setText(bucket.name);
        bucketViewHolder.bucketShotCount.setText(String.valueOf(bucket.shots_count));
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }
}
