package com.example.refuhoo.dribbbbbo.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.refuhoo.dribbbbbo.dribbble.auth.Auth;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.model.User;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;
import com.example.refuhoo.dribbbbbo.view.LoginActivity;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by refuhoo on 2017/1/4.
 */

public class Dribbble {
    private static final String TAG = "Dribbble API";
    private static final String SP_AUTH = "auth";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";

    private static final String API_URL_END_POINT = "https://api.dribbble.com/v1/";
    private static final String URI_USER = API_URL_END_POINT + "user";
    private static final String URI_SHOTS = API_URL_END_POINT + "shots";

    private static final TypeToken<User> USER_TYPE= new TypeToken<User>(){};
    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>(){};

    private static User user;
    private static String accessToken;

    private static Response getFromDribbble(String token, String URI) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                                .url(URI)
                                .header("Authorization", "Bearer " + token)
                                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken) throws IOException{
        return ModelUtils.toObject(response.body().string(),typeToken);
    }


    private static void storeAccessToken(Context context, String token){
        SharedPreferences sp = context.getSharedPreferences(SP_AUTH,Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token);
    }

    private static void storeUser(Context context, User user) {
        ModelUtils.save(context,KEY_USER,user);
    }

    private static String loadAccessToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_AUTH,Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN,"");
    }

    private static User loadUser(Context context) {
        return ModelUtils.read(context,KEY_USER, USER_TYPE);
    }

    public static List<Shot> getShots(int page) throws IOException {
        String URL = URI_SHOTS + "?page=" + page;
        return parseResponse(getFromDribbble(accessToken,URL), SHOT_LIST_TYPE);
    }

    public static void login(Context context, String token) throws IOException{
        accessToken = token;
        storeAccessToken(context, token);

        user = parseResponse(getFromDribbble(token, URI_USER), USER_TYPE);
        storeUser(context, user);
    }

    public static void logout(Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);
        user = null;
        accessToken = null;
        Log.d("mengjie", "logout");
    }

    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    public static User getCurrentUser() {
        return user;
    }

    public static boolean isLoggedIn() {
        Log.d("mengjie", String.valueOf((accessToken != null)));
        return accessToken != null;
    }
}
