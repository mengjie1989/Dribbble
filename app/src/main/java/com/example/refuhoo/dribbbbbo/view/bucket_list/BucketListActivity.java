package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.view.shot_detail.ShotFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/28.
 */

public class BucketListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_fragment);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getActivityTitle());

        if(savedInstanceState == null){
            Log.d("mengjie", "create bucketlist fragment");
            boolean isChoosingMode = getIntent().getExtras().getBoolean(
                    BucketListFragment.KEY_CHOOSING_MODE);
            ArrayList<String> chosenBucketIds = getIntent().getExtras().getStringArrayList(
                    BucketListFragment.KEY_COLLECTED_BUCKET_IDS);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, BucketListFragment.newInstance(null, isChoosingMode, chosenBucketIds))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
//            case R.id.save:
//                //go back to shot activity with updated bucketlist data.
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getActivityTitle() {
        return R.string.choose_bucket;
    }
}
