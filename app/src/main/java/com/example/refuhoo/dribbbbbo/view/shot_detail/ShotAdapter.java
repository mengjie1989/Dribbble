package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Shot;

/**
 * Created by refuhoo on 2017/1/10.
 */

public class ShotAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_INFO = 1;

    private final Shot shotInfo;
    private final ShotFragment shotFragment;

    public ShotAdapter(ShotFragment shotFragment, Shot shot){
        this.shotFragment = shotFragment;
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
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

            Glide
                .with(holder.itemView.getContext())
                .load(shotInfo.getImageURL())
                .centerCrop()
                .placeholder(R.drawable.shot_placeholder)
                .into(imageViewHolder.imageView);

        } else {
            Log.d("mengjie", "shotAdapter: position 1");
            InfoViewHolder infoViewHolder = (InfoViewHolder) holder;

            final Context context = holder.itemView.getContext();

            Glide
                    .with(context)
                    .load(shotInfo.user.avatar_url)
                    .centerCrop()
                    .placeholder(R.drawable.user_picture_placeholder)
                    .into(infoViewHolder.authorPicture);

            infoViewHolder.viewCount.setText(Integer.toString(shotInfo.views_count));
            infoViewHolder.likeCount.setText(Integer.toString(shotInfo.likes_count));
            infoViewHolder.bucketCount.setText(Integer.toString(shotInfo.buckets_count));

            infoViewHolder.title.setText(shotInfo.title);
            infoViewHolder.authorName.setText(shotInfo.user.name);
            infoViewHolder.description.setText(Html.fromHtml(shotInfo.description == null ? "" : shotInfo.description));

            infoViewHolder.likeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Like count clicked", Toast.LENGTH_SHORT).show();
                }
            });

            infoViewHolder.bucketCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Bucket count clicked", Toast.LENGTH_SHORT).show();
                }
            });

            infoViewHolder.actionLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("mengjie", "like clicked");
                    shotFragment.like(shotInfo.id, !shotInfo.liked);
                }
            });

            infoViewHolder.actionBucket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shotFragment.bucket();
                }
            });

            Drawable likeDrawable = shotInfo.liked
                    ? ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_pink_400_18dp)
                    : ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_black_18dp);
            infoViewHolder.actionLike.setImageDrawable(likeDrawable);

            Drawable bucketDrawable = shotInfo.bucketed
                    ? ContextCompat.getDrawable(getContext(), R.drawable.ic_inbox_pink_400_18dp)
                    : ContextCompat.getDrawable(getContext(), R.drawable.ic_inbox_black_18dp);
            infoViewHolder.actionBucket.setImageDrawable(bucketDrawable);

            infoViewHolder.actionShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            shotInfo.title + " " + shotInfo.html_url);
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent,
                            context.getString(R.string.share_shot)));
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private Context getContext(){
        return shotFragment.getContext();
    }

}
