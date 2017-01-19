package com.example.refuhoo.dribbbbbo.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.shot_list.ShotListFragment;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/11.
 */

public class ShotActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Shot shot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        shot = ModelUtils.toObject(getIntent().getStringExtra(ShotFragment.KEY_SHOT), new TypeToken<Shot>(){});

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(getActivityTitle());
        
        if(savedInstanceState == null){
            Log.d("mengjie", "create shot fragment");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ShotFragment.newInstance(getShotBundle()))
                    .commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bundle getShotBundle() {
        Bundle args = new Bundle();
        args.putString(ShotFragment.KEY_SHOT,ModelUtils.toString(shot, new TypeToken<Shot>(){}));
        return args;
    }

    private String getActivityTitle() {
        return shot.title;
    }


}
