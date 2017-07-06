package com.example.refuhoo.dribbbbbo.view.bucket_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.model.Bucket;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/25.
 */

public class NewBucketDialogFragment extends DialogFragment {

    @BindView(R.id.bucket_name) EditText bucketName;
    @BindView(R.id.bucket_description) EditText bucketDescription;

    public static final String TAG = "NewBucketDialogFragment";

    public static NewBucketDialogFragment newInstance(){
        NewBucketDialogFragment frag = new NewBucketDialogFragment();
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_bucket,null);
        ButterKnife.bind(this,view);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_bucket_title)
                .setView(view)
                .setPositiveButton(R.string.new_bucket_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bucket bucket = new Bucket();
                        bucket.name = bucketName.getText().toString();
                        bucket.description = bucketDescription.getText().toString();
                        SaveNewBucketListener listener = (SaveNewBucketListener) getTargetFragment();
                        listener.onSaveNewBucket(bucket);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.new_bucket_cancel, null)
                .create();
    }

    public interface SaveNewBucketListener{
        void onSaveNewBucket(Bucket bucket);
    }
}
