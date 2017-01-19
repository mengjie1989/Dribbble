package com.example.refuhoo.dribbbbbo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.refuhoo.dribbbbbo.R;
import com.example.refuhoo.dribbbbbo.dribbble.Dribbble;
import com.example.refuhoo.dribbbbbo.dribbble.auth.Auth;
import com.example.refuhoo.dribbbbbo.dribbble.auth.AuthActivity;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by refuhoo on 2017/1/4.
 */

public class LoginActivity extends AppCompatActivity {

    public static final int REQ_CODE = 100;

    @BindView(R.id.activity_login_btn)TextView loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Dribbble.logout(LoginActivity.this);
        //Dribbble.init(LoginActivity.this);
        Log.d("mengjie","LoginActivity-oncreate");
        if(!Dribbble.isLoggedIn()) {
            Log.d("mengjie","Dribbble.isLoggedIn() is false");
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                    startActivityForResult(intent, REQ_CODE);

                }
            });
        } else {
            Log.d("mengjie","Dribbble.isLoggedIn() is true");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("mengjie","at LoginActivity back from AuthActivity");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            Log.d("mengjie", "authCode" + authCode);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String token = Auth.fetchAccessToken(authCode);
                        Dribbble.login(LoginActivity.this, token);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

