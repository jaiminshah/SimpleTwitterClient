package com.jaiminshah.codepath.basictwitter.helpers;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "Bm78Q6SJFyariQ3CUyfKgwfS7";       // Change this
    public static final String REST_CONSUMER_SECRET = "Eu2yPpkhzzTCDzemDVfUjIPC45FnWd8JfPgzsTBWmrW93Usw2y"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", "1");
        if (max_id > 0) {
            params.put("max_id", Long.toString(max_id));
        }
        client.get(apiUrl, params, handler);
    }

    public void postUpdate(String status, long inReplyId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        if (inReplyId != 0) {
            params.put("in_reply_to_status_id", Long.toString(inReplyId));
        }
        client.post(apiUrl, params, handler);
    }

    public void getVerifyCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, handler);
    }

    public void getMentionsTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", "1");
        if (max_id > 0) {
            params.put("max_id", Long.toString(max_id));
        }
        client.get(apiUrl, params, handler);
    }
    public void getUserTimeline(long user_id, long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", "1");
        if (max_id > 0) {
            params.put("max_id", Long.toString(max_id));
        }
        if (user_id > 0) {
            params.put("user_id", Long.toString(user_id));
        }
        client.get(apiUrl, params, handler);
    }
    public void postFavoriteCreate(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", Long.toString(uid));
        client.post(apiUrl, params, handler);
    }

    public void postFavoriteDestroy(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", Long.toString(uid));
        client.post(apiUrl, params, handler);
    }

    public void postRetweet(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/" + uid + ".json");
        client.post(apiUrl, handler);
    }

    public void postDestroy(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/destroy/" + uid + ".json");
        client.post(apiUrl, handler);
    }

    public void getFriendsList(long uid, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("user_id",Long.toString(uid));
        client.get(apiUrl,params,handler);
    }

    public void getFollowersList(long uid, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("user_id",Long.toString(uid));
        client.get(apiUrl,params,handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}