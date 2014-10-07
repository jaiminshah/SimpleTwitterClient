package com.jaiminshah.codepath.basictwitter.fragments;



import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SearchResultFragment extends TweetsListFragment {
    private static final String TAG = SearchResultFragment.class.getName();
    String mQuery;

    public static SearchResultFragment newInstance(String query){
        SearchResultFragment searchResultFragment= new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("query",query);
        searchResultFragment.setArguments(args);
        return searchResultFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuery = getArguments().getString("query");
    }

    @Override
    protected void fetchFromApi() {
        // Check if we have already fetch tweets once.
        // if we already fetched than fetch the next set of tweets
        // from (max_id - 1). max_id is inclusive so you need decrement it one.
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getUid() - 1 ;
        }

        client.getSearchTweets(mQuery, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (!jsonObject.isNull("statuses")){
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObject.getJSONArray("statuses");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addAll(Tweet.fromJSONArray(jsonArray));
                for (Tweet tweet : tweets) {
                    tweet.saveTweet();
                }
            }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d(TAG, throwable.toString());
                Log.d(TAG, s);
            }
        });
    }
}