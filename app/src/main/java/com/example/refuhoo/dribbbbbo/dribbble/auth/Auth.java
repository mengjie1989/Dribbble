package com.example.refuhoo.dribbbbbo.dribbble.auth;

import android.app.DownloadManager;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by refuhoo on 2017/1/4.
 */

public class Auth {

    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CLIENT_SECRET = "client_secret";
    private static final String KEY_REDIRECT_URI = "redirect_uri";
    private static final String KEY_SCOPE = "scope";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private static final String CLIENT_ID = "3604baf603fe93104d154ad30fcb9c24a2a850f213f6fda6a6d0ed000331b005";
    private static final String CLIENT_SECRET = "82d01fe70dae7374f11000532b5d66f95748a3e5dfe503332f497496d7f570ca";

    private static final String SCOPE = "public+write";
    private static final String URI_AUTHORIZE = "https://dribbble.com/oauth/authorize";
    private static final String URI_TOKEN = "https://dribbble.com/oauth/token";
    public static final String REDIRECT_URI = "https://www.google.com";

    public static String getAuthorizeURL(){
        String url = Uri.parse(URI_AUTHORIZE)
                    .buildUpon()
                    .appendQueryParameter(KEY_CLIENT_ID,CLIENT_ID)
                    .build().toString();

        // fix encode issue
        url += "&" + KEY_REDIRECT_URI + "=" + REDIRECT_URI;
        url += "&" + KEY_SCOPE + "=" + SCOPE;

        Log.d("mengjie", url);
        return url;
    }

    public static String fetchAccessToken(String authCode) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                                    .add(KEY_CLIENT_ID, CLIENT_ID)
                                    .add(KEY_CLIENT_SECRET,CLIENT_SECRET)
                                    .add(AuthActivity.KEY_CODE,authCode)
                                    .add(KEY_REDIRECT_URI,REDIRECT_URI)
                                    .build();
        Request request = new Request.Builder()
                                .url(URI_TOKEN)
                                .post(formBody)
                                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        try{
            JSONObject obj = new JSONObject(responseString);
            return obj.getString(KEY_ACCESS_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
