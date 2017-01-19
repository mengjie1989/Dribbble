package com.example.refuhoo.dribbbbbo.view.shot_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.dribbble.Dribbble;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.model.User;
import com.example.refuhoo.dribbbbbo.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class ShotListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShotListAdapter adapter;

    public static final int COUNT_PER_PAGE = 30;
    public static final int COUNT_TOTAL = 50;

    public static ShotListFragment newInstance() {
        ShotListFragment fragment = new ShotListFragment();
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

        adapter = new ShotListAdapter(new ArrayList<Shot>(), getContext(),
            new ShotListAdapter.LoadMoreListener() {

                @Override
                public void onLoadMore() {

//                    final Handler uiHandler = new Handler();
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(2000);
//                                uiHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        int count = getCount();
//                                        adapter.appendData(fakeData(count));
//                                        adapter.setShowLoading(count);
//                                    }
//                                });
//
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }).start();

                    int page = adapter.shotSize() / COUNT_PER_PAGE + 1;
                    new LoadShotTask(page).execute();

                }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
    }

    private int getCount() {
        int count = COUNT_TOTAL - adapter.shotSize();
        if(count < 0){
            count = 0;
        } else if(count > COUNT_PER_PAGE){
            count = COUNT_PER_PAGE;
        }
        return count;
    }

//    private List<Shot> fakeData(int count) {
//        List<Shot> shotLists = new ArrayList<>();
//        Random random = new Random();
//
//        count = (count > COUNT_TOTAL) ? COUNT_TOTAL : count;
//        for(int i = 0; i < count; i++){
//            Shot shot = new Shot();
//            shot.title = "shot" + i;
//            shot.views_count = random.nextInt(10000);
//            shot.likes_count = random.nextInt(200);
//            shot.buckets_count = random.nextInt(50);
//            shot.description = makeDescription();
//
//            shot.user = new User();
//            shot.user.name = shot.title + " author";
//            shotLists.add(shot);
//        }
//
//        return shotLists;
//    }
//
//    private static final String[] words = {
//            "bottle", "bowl", "brick", "building", "bunny", "cake", "car", "cat", "cup",
//            "desk", "dog", "duck", "elephant", "engineer", "fork", "glass", "griffon", "hat", "key",
//            "knife", "lawyer", "llama", "manual", "meat", "monitor", "mouse", "tangerine", "paper",
//            "pear", "pen", "pencil", "phone", "physicist", "planet", "potato", "road", "salad",
//            "shoe", "slipper", "soup", "spoon", "star", "steak", "table", "terminal", "treehouse",
//            "truck", "watermelon", "window"
//    };
//
//    private String makeDescription() {
//        return TextUtils.join(" ", words);
//    }

    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>>{

        private int page;

        public LoadShotTask(int page){
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                return Dribbble.getShots(page);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if(shots != null){
                adapter.appendData(shots);
            }
        }


    }

}
