package com.jaiminshah.codepath.basictwitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

/**
 * Created by jaimins on 10/4/14.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private static final String TAG = UserTimelineFragment.class.getName();
    private User mUser;

    public static UserTimelineFragment newInstance(User user){
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("user",user);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable("user");
    }

    @Override
    protected void fetchFromApi() {
        // Check if we have already fetch tweets once.
        // if we already fetched than fetch the next set of tweets
        // from (max_id - 1). max_id is inclusive so you need decrement it one.
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getUid() - 1 ;
        }

        //TODO: get the correct user_id here.
        long userId = 0;
        if (mUser != null){
            userId = mUser.getUid();
        }
//        mProgressBar.setVisibility(View.VISIBLE);

        client.getUserTimeline(userId, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                addAll(Tweet.fromJSONArray(jsonArray));
//                 Log.d("debug", jsonArray.toString());
                for (Tweet tweet : tweets) {
                    tweet.saveTweet();
                }
                swipeContainer.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d(TAG, throwable.toString());
                Log.d(TAG, s);
            }
        });
    }
}
