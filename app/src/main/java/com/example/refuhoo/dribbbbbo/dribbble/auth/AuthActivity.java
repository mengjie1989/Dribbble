package com.example.refuhoo.dribbbbbo.dribbble.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.refuhoo.dribbbbbo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/4.
 */

public class AuthActivity extends AppCompatActivity {

    public static final String KEY_CODE = "code";

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getString(R.string.auth_activity_title));

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("mengjie", "redirect callback");
                Uri url = request.getUrl();
                Log.d("mengjie", url.toString());
                if(url.toString().startsWith(Auth.REDIRECT_URI)){
                    Intent intent = new Intent();
                    intent.putExtra(KEY_CODE, url.getQueryParameter(KEY_CODE));
                    Log.d("mengjie",url.getQueryParameter(KEY_CODE));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, request);
            }


        });

        webView.loadUrl(Auth.getAuthorizeURL());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
