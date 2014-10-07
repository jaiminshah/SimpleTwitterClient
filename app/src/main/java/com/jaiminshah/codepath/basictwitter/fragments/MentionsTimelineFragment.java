package com.jaiminshah.codepath.basictwitter.fragments;

import android.util.Log;
import android.view.View;

import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

/**
 * Created by jaimins on 10/4/14.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    protected void fetchFromApi() {
        // Check if we have already fetch tweets once.
        // if we already fetched than fetch the next set of tweets
        // from (max_id - 1). max_id is inclusive so you need decrement it one.
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getUid() - 1 ;
        }
        client.getMentionsTimeline(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                addAll(Tweet.fromJSONArray(jsonArray));
//                 Log.d("debug", jsonArray.toString());
                mProgressBar.setVisibility(View.GONE);
                for (Tweet tweet : tweets) {
                    tweet.saveTweet();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }
}
