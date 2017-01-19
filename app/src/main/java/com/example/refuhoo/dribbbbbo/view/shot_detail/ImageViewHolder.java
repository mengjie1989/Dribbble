package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.view.View;
import android.widget.ImageView;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by refuhoo on 2017/1/10.
 */

public class ImageViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_image) public ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }
}
