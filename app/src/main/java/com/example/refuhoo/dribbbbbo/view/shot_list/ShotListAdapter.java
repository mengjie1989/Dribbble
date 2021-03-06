package com.example.refuhoo.dribbbbbo.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.shot_detail.ShotActivity;
import com.example.refuhoo.dribbbbbo.view.shot_detail.ShotFragment;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by refuhoo on 2016/12/23.
 */

public class ShotListAdapter extends RecyclerView.Adapter{

    private final int VIEW_TYPE_SHOT_LIST = 0;
    private final int VIEW_TYPE_PROGRESS_BAR = 1;
    //private final int TOTAL_SHOTS = 50;

    private List<Shot> shotList;
    private final ShotListFragment shotListFragment;
    private LoadMoreListener listener;
    private boolean showLoading;

    public ShotListAdapter(List<Shot> shots, ShotListFragment fragment, LoadMoreListener l){
        shotList = shots;
        shotListFragment = fragment;
        listener = l;
        showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_SHOT_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shot, parent, false);
                return new ShotViewHolder(view);
            case VIEW_TYPE_PROGRESS_BAR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loading, parent, false);
                return new RecyclerView.ViewHolder(view){};
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPE_PROGRESS_BAR){
            listener.onLoadMore();
        } else {
            ShotViewHolder shotViewHolder = (ShotViewHolder) holder;
            final Shot shot = shotList.get(position);

            shotViewHolder.bucket_count.setText(String.valueOf(shot.buckets_count));
            shotViewHolder.like_count.setText(String.valueOf(shot.likes_count));
            shotViewHolder.view_count.setText(String.valueOf(shot.views_count));

            shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotListFragment.getContext(), ShotActivity.class);
                    intent.putExtra(ShotFragment.KEY_SHOT, ModelUtils.toString(shot,new TypeToken<Shot>(){}));
                    shotListFragment.startActivityForResult(intent, ShotListFragment.REQ_CODE_SHOT);
                }
            });

            Glide
                .with(holder.itemView.getContext())
                .load(shotList.get(position).getImageURL())
                .centerCrop()
                .placeholder(R.drawable.shot_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(shotViewHolder.shot_image);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position < shotList.size()){
            return VIEW_TYPE_SHOT_LIST;
        } else {
            return VIEW_TYPE_PROGRESS_BAR;
        }
    }

    @Override
    public int getItemCount() {
        if(showLoading) {
            return shotList.size() + 1;
        } else {
            return shotList.size();
        }
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener{
        void onLoadMore();
    }

    public void appendData(List<Shot> shots){
        shotList.addAll(shots);
        notifyDataSetChanged();
    }

    public int shotSize(){
        return shotList.size();
    }

    public List<Shot> getData(){
        return shotList;
    }
}
