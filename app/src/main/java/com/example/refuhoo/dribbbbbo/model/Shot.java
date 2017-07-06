package com.example.refuhoo.dribbbbbo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by refuhoo on 2016/12/23.
 */

public class Shot {

    public static final String IMAGE_NORMAL = "normal";
    public static final String IMAGE_HIDPI = "hidpi";

    public int views_count;
    public int likes_count;
    public int buckets_count;

    public String id;
    public String title;
    public String description;
    public User user;
    public String html_url;

    public boolean liked;
    public boolean bucketed;

    public Map<String,String> images;
    //public boolean animated;


    public String getImageURL(){
        if(images == null){
            return null;
        }

        return (images.get(IMAGE_HIDPI) != null)
                ? images.get(IMAGE_HIDPI) : images.get(IMAGE_NORMAL);
    }



}
