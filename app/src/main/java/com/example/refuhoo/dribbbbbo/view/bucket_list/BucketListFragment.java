package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.dribbble.Dribbble;
import com.example.refuhoo.dribbbbbo.model.Bucket;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.base.SpaceItemDecoration;
import com.example.refuhoo.dribbbbbo.view.shot_list.ShotListFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/2.
 */

public class BucketListFragment extends Fragment implements NewBucketDialogFragment.SaveNewBucketListener{
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_CHOOSING_MODE = "choosing_mode";
    public static final String KEY_CHOSEN_BUCKET_IDS = "chosen_bucket_ids";
    public static final String KEY_COLLECTED_BUCKET_IDS = "collected_bucket_ids";

    private BucketListAdapter adapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private String userId;
    private boolean isChoosingMode;
    private Set<String> collectedBucketIdSet;

    public static final int REQ_CODE_NEW_BUCKET = 100;

    public static BucketListFragment newInstance(@Nullable String userId,
                                                 boolean isChoosingMode,
                                                 @Nullable ArrayList<String> chosenBucketIds) {
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userId);
        args.putBoolean(KEY_CHOOSING_MODE, isChoosingMode);
        args.putStringArrayList(KEY_COLLECTED_BUCKET_IDS, chosenBucketIds);

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_fab_recycler_view, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        userId = getArguments().getString(KEY_USER_ID);
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (isChoosingMode) {
            List<String> chosenBucketIdList = getArguments().getStringArrayList(KEY_COLLECTED_BUCKET_IDS);
            if (chosenBucketIdList != null) {
                collectedBucketIdSet = new HashSet<>(chosenBucketIdList);
            }
        } else {
            collectedBucketIdSet = new HashSet<>();
        }

        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener(){

            @Override
            public void onLoadMore() {
                int page = adapter.bucketSize() / Dribbble.COUNT_PER_PAGE + 1;
                AsyncTaskCompat.executeParallel(new LoadBucketTask(page));;
            }
        }, isChoosingMode);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        NewBucketDialogFragment newBucketDialogFragment = NewBucketDialogFragment.newInstance();
        newBucketDialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
        newBucketDialogFragment.show(fm,NewBucketDialogFragment.TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("mengjie", "isChoosingMode" + ":  " + isChoosingMode);
        if (isChoosingMode) {
            Log.d("mengjie", "save menu created");
            inflater.inflate(R.menu.bucket_list_choose_mode_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("mengjie", "saved buckets   2");
        if (item.getItemId() == R.id.save) {
            Log.d("mengjie", "saved buckets");
            ArrayList<String> chosenBucketIds = new ArrayList<>();
            for (Bucket bucket : adapter.getData()) {
                if (bucket.isChoosing) {
                    chosenBucketIds.add(bucket.id);
                }
            }

            Intent result = new Intent();
            result.putStringArrayListExtra(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveNewBucket(Bucket bucket) {
        new NewBucketTask(bucket).execute();
    }

    private class NewBucketTask extends AsyncTask<Void, Void, Bucket>{

        private Bucket bucket;

        public NewBucketTask(Bucket bucket) {
            this.bucket = bucket;
        }

        @Override
        protected Bucket doInBackground(Void... params) {
            try {
                return Dribbble.createBucket(bucket);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bucket bucketUpdated) {
            if(bucketUpdated != null){
                Log.d("mengjie", "bucket name" + bucketUpdated.name);
                Log.d("mengjie", "bucket id" + bucketUpdated.id);
                Log.d("mengjie", "bucket created at" + bucketUpdated.created_at.toString());
                adapter.prependData(bucketUpdated);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class LoadBucketTask extends AsyncTask<Void, Void, List<Bucket>>{

        private int page;

        public LoadBucketTask(int page){
            this.page = page;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            try {
                return Dribbble.getBuckets(page);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            if(buckets != null){
                for (Bucket bucket : buckets) {
                    if (collectedBucketIdSet.contains(bucket.id)) {
                        bucket.isChoosing = true;
                    }
                }
                adapter.appendData(buckets);
                adapter.setShowLoading(buckets.size() == Dribbble.COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }


    }
}
