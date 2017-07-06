package com.example.refuhoo.dribbbbbo.view.shot_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
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
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.base.SpaceItemDecoration;
import com.example.refuhoo.dribbbbbo.view.shot_detail.ShotFragment;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class ShotListFragment extends Fragment {

    public static final int REQ_CODE_SHOT = 100;
    private RecyclerView recyclerView;
    private ShotListAdapter adapter;

    private int listType;

    public static final int COUNT_TOTAL = 50;
    public static final String KEY_LIST_TYPE = "listType";
    public static final int LIST_TYPE_POPULAR = 1;
    public static final int LIST_TYPE_LIKED = 2;
    public static final int LIST_TYPE_BUCKET = 3;

    public static ShotListFragment newInstance(int type) {
        ShotListFragment fragment = new ShotListFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_LIST_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SHOT && resultCode == Activity.RESULT_OK) {
            Shot updatedShot = ModelUtils.toObject(data.getStringExtra(ShotFragment.KEY_SHOT),
                                                    new TypeToken<Shot>(){});
            for (Shot shot : adapter.getData()) {
                if (TextUtils.equals(shot.id, updatedShot.id)) {
                    shot.likes_count = updatedShot.likes_count;
                    shot.buckets_count = updatedShot.buckets_count;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
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

        listType = getArguments().getInt(KEY_LIST_TYPE);
        adapter = new ShotListAdapter(new ArrayList<Shot>(), this,
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

                    int page = adapter.shotSize() / Dribbble.COUNT_PER_PAGE + 1;
                    //new LoadShotTask(page).execute();
                    AsyncTaskCompat.executeParallel(new LoadShotTask(page));
                }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
    }
//
//    private int getCount() {
//        int count = COUNT_TOTAL - adapter.shotSize();
//        if(count < 0){
//            count = 0;
//        } else if(count > COUNT_PER_PAGE){
//            count = COUNT_PER_PAGE;
//        }
//        return count;
//    }

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
                switch(listType) {
                    case LIST_TYPE_POPULAR:
                        return Dribbble.getShots(page);
                    case LIST_TYPE_LIKED:
                        return Dribbble.getLikedShots(page);
                    default:
                        return Dribbble.getShots(page);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if(shots != null){
                adapter.appendData(shots);
                adapter.setShowLoading(shots.size() == Dribbble.COUNT_PER_PAGE);
                Snackbar.make(getView(), shots.get(0).title, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }


    }

}
