package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Shot;

/**
 * Created by refuhoo on 2017/1/10.
 */

public class ShotAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_INFO = 1;

    private Shot shotInfo;

    public ShotAdapter(Shot shot){
        shotInfo = shot;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_TYPE_SHOT_IMAGE;
        } else {
            return VIEW_TYPE_SHOT_INFO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("mengjie", "create shot viewholder");
        View view;
        switch(viewType){
            case VIEW_TYPE_SHOT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_item_image,parent,false);
                return new ImageViewHolder(view);
            case VIEW_TYPE_SHOT_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot_item_info,parent,false);
                return new InfoViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position == 0){
            Log.d("mengjie", "shotAdapter: position 0");
            return;
        } else {
            Log.d("mengjie", "shotAdapter: position 1");
            InfoViewHolder infoViewHolder = (InfoViewHolder) holder;

            infoViewHolder.viewCount.setText(Integer.toString(shotInfo.views_count));
            infoViewHolder.likeCount.setText(Integer.toString(shotInfo.likes_count));
            infoViewHolder.bucketCount.setText(Integer.toString(shotInfo.buckets_count));

            infoViewHolder.title.setText(shotInfo.title);
            infoViewHolder.authorName.setText(shotInfo.user.name);
            infoViewHolder.description.setText(shotInfo.description);

        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
