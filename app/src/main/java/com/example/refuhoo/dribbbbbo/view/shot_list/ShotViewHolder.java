package com.example.refuhoo.dribbbbbo.view.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.view.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2016/12/23.
 */

public class ShotViewHolder extends BaseViewHolder {
    @BindView (R.id.shot_view_count) public TextView view_count;
    @BindView (R.id.shot_like_count) public TextView like_count;
    @BindView (R.id.shot_bucket_count) public TextView bucket_count;
    @BindView (R.id.shot_image) public ImageView shot_image;
    @BindView (R.id.shot_clickable_cover) public View cover;

    public ShotViewHolder(View itemView) {
        super(itemView);
    }
}
