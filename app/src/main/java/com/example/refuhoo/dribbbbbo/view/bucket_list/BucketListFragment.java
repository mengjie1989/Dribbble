package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Bucket;
import com.example.refuhoo.dribbbbbo.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class BucketListFragment extends Fragment {
    private RecyclerView recyclerView;

    public static BucketListFragment newInstance() {
        BucketListFragment fragment = new BucketListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new BucketListAdapter(fakeData()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
    }

    private List<Bucket> fakeData() {
        List<Bucket> BucketLists = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < 200; i++){
            Bucket bucket = new Bucket();
            bucket.name = "Bucket" + i;
            bucket.shots_count = random.nextInt(10000);
            BucketLists.add(bucket);
        }

        return BucketLists;
    }
}
