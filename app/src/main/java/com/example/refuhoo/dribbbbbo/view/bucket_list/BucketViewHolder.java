package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class BucketViewHolder extends BaseViewHolder {

    @BindView(R.id.bucket_name) public TextView bucketName;
    @BindView(R.id.bucket_shot_count) public TextView bucketShotCount;
    @BindView(R.id.bucket_shot_chosen) ImageView bucketShotChosen;
    @BindView(R.id.bucket_clickable_cover) View bucketCover;

    public BucketViewHolder(View itemView) {
        super(itemView);
    }
}
