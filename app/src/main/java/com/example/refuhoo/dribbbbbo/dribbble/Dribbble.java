package com.example.refuhoo.dribbbbbo.dribbble;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;


import com.example.refuhoo.dribbbbbo.model.Bucket;
import com.example.refuhoo.dribbbbbo.model.Like;
import com.example.refuhoo.dribbbbbo.model.Shot;
import com.example.refuhoo.dribbbbbo.model.User;
import com.example.refuhoo.dribbbbbo.utils.ModelUtils;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by refuhoo on 2017/1/4.
 */

public class Dribbble {
    private static final String TAG = "Dribbble API";
    private static final String SP_AUTH = "auth";
    private static final String KEY_SHOT_ID = "shot_id";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER = "user";
    private static final String KEY_BUCKET_NAME = "name";
    private static final String KEY_BUCKET_DESCRIPTION = "description";

    private static final String API_URL_END_POINT = "https://api.dribbble.com/v1/";
    private static final String URI_USER = API_URL_END_POINT + "user";
    private static final String URI_SHOTS = API_URL_END_POINT + "shots";
    private static final String URI_LIKES_SHOTS = URI_USER + "/likes";
    private static final String URI_BUCKET_LIST = URI_USER + "/buckets";
    private static final String URI_BUCKET = API_URL_END_POINT + "buckets";
    
    private static final TypeToken<User> USER_TYPE= new TypeToken<User>(){};
    private static final TypeToken<Bucket> BUCKET_TYPE = new TypeToken<Bucket>(){};
    private static final TypeToken<Like> LIKE_TYPE = new TypeToken<Like>(){};
    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>(){};
    private static final TypeToken<List<Like>> LIKE_LIST_TYPE = new TypeToken<List<Like>>(){};
    private static final TypeToken<List<Bucket>> BUCKET_LIST_TYPE = new TypeToken<List<Bucket>>(){};

    private static OkHttpClient client = new OkHttpClient();

    private static User user;
    private static String accessToken;

    public static final int COUNT_PER_PAGE = 12;

    private static Response makePutRequest(String url,
                                           RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .put(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    private static Response makePostRequest(String url, RequestBody requestBody) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .post(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    private static Response makeDeleteRequest(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .delete()
                .build();
        return client.newCall(request).execute();
    }

    private static Response makeDeleteRequest(String url, RequestBody requestBody) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessToken)
                .delete(requestBody)
                .build();
        return client.newCall(request).execute();
    }

    public static Bucket createBucket(Bucket bucket) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                                    .add(KEY_BUCKET_NAME, bucket.name)
                                    .add(KEY_BUCKET_DESCRIPTION, bucket.description)
                                    .build();

        Response response = makePostRequest(URI_BUCKET,formBody);
        return parseResponse(response,BUCKET_TYPE);
    }

    private static Response getFromDribbble(String token, String URI) throws IOException{
        Request request = new Request.Builder()
                                .url(URI)
                                .header("Authorization", "Bearer " + token)
                                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken) throws IOException{
        String s = response.body().string();
        Log.d("mengjie", s);
        return ModelUtils.toObject(s,typeToken);
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
        return sp.getString(KEY_ACCESS_TOKEN,null);
    }

    private static User loadUser(Context context) {
        return ModelUtils.read(context,KEY_USER, USER_TYPE);
    }

    public static List<Shot> getShots(int page) throws IOException {
        String URL = URI_SHOTS + "?page=" + page;
        return parseResponse(getFromDribbble(accessToken,URL), SHOT_LIST_TYPE);
    }

    public static List<Like> getLikes(int page) throws IOException {
        String URL = URI_LIKES_SHOTS + "?page=" + page;
        return parseResponse(getFromDribbble(accessToken,URL), LIKE_LIST_TYPE);
    }

    public static List<Shot> getLikedShots(int page) throws IOException {
        List<Like> likeList = getLikes(page);
        List<Shot> resultList = new ArrayList<>();
        for(Like like : likeList){
            resultList.add(like.shot);
        }
        return resultList;
    }

    public static Boolean checkiflike(String id) throws IOException {
        String URL = URI_SHOTS + "/" + id + "/like";
        Response response = getFromDribbble(accessToken, URL);
        switch(response.code()){
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                throw new IOException();
        }
    }

    private static void checkStatusCode(Response response,
                                        int statusCode) throws IOException {
        if(response.code() != statusCode) {
            throw new IOException();
        }
    }

    public static Like likeShot(String id) throws IOException{
        String url = URI_SHOTS + "/" + id + "/like";
        Response response = makePostRequest(url, new FormBody.Builder().build());

        checkStatusCode(response, HttpURLConnection.HTTP_CREATED);

        return parseResponse(response, LIKE_TYPE);
    }

    public static void unLikeShot(String id) throws IOException{
        String url = URI_SHOTS + "/" + id + "/like";
        Response response = makeDeleteRequest(url);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static List<Bucket> getBuckets(int page) throws IOException {
        String URL = URI_BUCKET_LIST + "?page=" + page;
        return parseResponse(getFromDribbble(accessToken,URL), BUCKET_LIST_TYPE);
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

    public static List<Bucket> getShotBuckets(String id) throws IOException{
        String url = URI_SHOTS + "/" + id + "/buckets";
        return parseResponse(getFromDribbble(accessToken, url), BUCKET_LIST_TYPE);
    }

    public static List<Bucket> getUserBuckets() throws IOException{
        return parseResponse(getFromDribbble(accessToken, URI_BUCKET_LIST), BUCKET_LIST_TYPE);
    }

    public static void addBucketShot(@NonNull String bucketId,
                                     @NonNull String shotId) throws IOException {
        String url = URI_BUCKET + "/" + bucketId + "/shots";
        FormBody formBody = new FormBody.Builder()
                .add(KEY_SHOT_ID, shotId)
                .build();

        Response response = makePutRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }

    public static void removeBucketShot(@NonNull String bucketId,
                                        @NonNull String shotId) throws IOException {
        String url = URI_BUCKET + "/" + bucketId + "/shots";
        FormBody formBody = new FormBody.Builder()
                .add(KEY_SHOT_ID, shotId)
                .build();

        Response response = makeDeleteRequest(url, formBody);
        checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
    }
}
