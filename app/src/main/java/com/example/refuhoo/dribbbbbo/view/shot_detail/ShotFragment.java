package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.dribbble.Dribbble;
import com.example.refuhoo.dribbbbbo.model.Bucket;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.bucket_list.BucketListActivity;
import com.example.refuhoo.dribbbbbo.view.bucket_list.BucketListFragment;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by refuhoo on 2017/1/11.
 */

public class ShotFragment extends Fragment {

    public static final String KEY_SHOT = "shot";

    private static final int REQ_CODE_BUCKET = 100;

    private Shot shot;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private boolean isLiking;
    private ArrayList<String> collectedBucketIds;

    public static ShotFragment newInstance(Bundle args) {
        ShotFragment shotFragment = new ShotFragment();
        shotFragment.setArguments(args);
        return shotFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("mengjie", "In shotFragment: onCreateView");
        View fragmentView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("mengjie", "In shotFragment: onViewCreated");
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT), new TypeToken<Shot>() {
        });
        recyclerView.setAdapter(new ShotAdapter(this, shot));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        isLiking = true;
        AsyncTaskCompat.executeParallel(new CheckLikeTask());
        AsyncTaskCompat.executeParallel(new LoadBucketsTask());
    }

    public void like(String shotId, boolean toLike) {
        if (!isLiking) {
            isLiking = true;
            AsyncTaskCompat.executeParallel(new LikeTask(shotId, toLike));
        }
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>() {
        }));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    public void bucket() {
        if (collectedBucketIds == null) {
            Snackbar.make(getView(), R.string.shot_detail_loading_buckets, Snackbar.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), BucketListActivity.class);
            intent.putExtra(BucketListFragment.KEY_CHOOSING_MODE, true);
            intent.putStringArrayListExtra(BucketListFragment.KEY_COLLECTED_BUCKET_IDS,
                    collectedBucketIds);
            startActivityForResult(intent, REQ_CODE_BUCKET);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_BUCKET && resultCode == Activity.RESULT_OK) {
            List<String> chosenBucketIds = data.getStringArrayListExtra(
                    BucketListFragment.KEY_CHOSEN_BUCKET_IDS);
            List<String> addedBucketIds = new ArrayList<>();
            List<String> removedBucketIds = new ArrayList<>();
            for (String chosenBucketId : chosenBucketIds) {
                if (!collectedBucketIds.contains(chosenBucketId)) {
                    addedBucketIds.add(chosenBucketId);
                }
            }

            for (String collectedBucketId : collectedBucketIds) {
                if (!chosenBucketIds.contains(collectedBucketId)) {
                    removedBucketIds.add(collectedBucketId);
                }
            }

            AsyncTaskCompat.executeParallel(new UpdateBucketTask(addedBucketIds, removedBucketIds));
        }
    }

    private class LikeTask extends AsyncTask<Void, Void, Void> {
        private String shotId;
        private boolean toLike;
        private IOException exception;

        public LikeTask(String shotId, boolean toLike) {
            this.shotId = shotId;
            this.toLike = toLike;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("mengjie", "liking" + toLike);
                if (toLike) {
                    Dribbble.likeShot(shotId);
                } else {
                    Dribbble.unLikeShot(shotId);
                }
            } catch (IOException e) {
                exception = e;
                e.printStackTrace();
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            isLiking = false;
            if (exception == null) {
                shot.liked = toLike;
                shot.likes_count += (toLike) ? 1 : -1;
                recyclerView.getAdapter().notifyDataSetChanged();
                setResult();

            }
        }
    }

    private class CheckLikeTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return Dribbble.checkiflike(new String(shot.id));
            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            isLiking = false;
            if (aBoolean != null) {
                shot.liked = aBoolean;
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private class LoadBucketsTask extends AsyncTask<Void, Void, ArrayList<String>> {
        IOException exception;

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                List<Bucket> shotBuckets = Dribbble.getShotBuckets(shot.id);
                List<Bucket> userBuckets = Dribbble.getUserBuckets();

                Set<String> userBucketIds = new HashSet<>();
                for (Bucket bucket : userBuckets) {
                    userBucketIds.add(bucket.id);
                }
                ArrayList<String> collectedBucketIds = new ArrayList<>();
                for (Bucket bucket : shotBuckets) {
                    if (userBucketIds.contains(bucket.id)) {
                        collectedBucketIds.add(bucket.id);
                    }
                }
                return collectedBucketIds;
            } catch (IOException e){
                exception = e;
                e.printStackTrace();
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if(strings != null){
                collectedBucketIds = strings;
                if(collectedBucketIds.size() > 0){
                    shot.bucketed = true;
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }

    private class UpdateBucketTask extends AsyncTask<Void, Void, Void> {
        private IOException exception;
        private List<String> added;
        private List<String> removed;

        private UpdateBucketTask(@NonNull List<String> added,
                                 @NonNull List<String> removed) {
            this.added = added;
            this.removed = removed;
        }

        @Override
        protected Void doInBackground(Void... params)  {
            try {
                for (String addedId : added) {
                    Dribbble.addBucketShot(addedId, shot.id);
                }

                for (String removedId : removed) {
                    Dribbble.removeBucketShot(removedId, shot.id);
                }
                return null;
            }catch (IOException e){
                exception = e;
                e.printStackTrace();
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            collectedBucketIds.addAll(added);
            collectedBucketIds.removeAll(removed);

            shot.bucketed = !collectedBucketIds.isEmpty();
            shot.buckets_count += added.size() - removed.size();

            recyclerView.getAdapter().notifyDataSetChanged();

            setResult();
        }

    }
}
