package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by refuhoo on 2017/1/11.
 */

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";

    private Shot shot;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static ShotFragment newInstance(Bundle args){
        ShotFragment shotFragment = new ShotFragment();
        shotFragment.setArguments(args);
        return shotFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("mengjie", "In shotFragment: onCreateView");
        View fragmentView = inflater.inflate(R.layout.fragment_recycler_view,container,false);
        ButterKnife.bind(this,fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("mengjie", "In shotFragment: onViewCreated");
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),new TypeToken<Shot>(){});
        recyclerView.setAdapter(new ShotAdapter(shot));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
