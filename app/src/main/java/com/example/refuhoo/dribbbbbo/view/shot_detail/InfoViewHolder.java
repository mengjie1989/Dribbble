package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by refuhoo on 2017/1/10.
 */

public class InfoViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_view_count) public TextView viewCount;
    @BindView(R.id.shot_action_like) public ImageButton actionLike;
    @BindView(R.id.shot_like_count) public TextView likeCount;
    @BindView(R.id.shot_action_bucket) public ImageButton actionBucket;
    @BindView(R.id.shot_bucket_count) public TextView bucketCount;
    @BindView(R.id.shot_action_share) public TextView actionShare;


    @BindView(R.id.shot_title) TextView title;
    @BindView(R.id.shot_description) TextView description;
    @BindView(R.id.shot_author_name) TextView authorName;
    @BindView(R.id.shot_author_picture) ImageView authorPicture;

    public InfoViewHolder(View itemView) {
        super(itemView);
    }
}
