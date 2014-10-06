package com.jaiminshah.codepath.basictwitter.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.fragments.ComposeFragment;
import com.jaiminshah.codepath.basictwitter.fragments.HomeTimelineFragment;
import com.jaiminshah.codepath.basictwitter.fragments.MentionsTimelineFragment;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.listeners.FragmentTabListener;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class TimelineActivity extends FragmentActivity implements ComposeFragment.ComposeFragmentListener {

    private static final String TAG = TimelineActivity.class.getName();
    private HomeTimelineFragment homeTimelineFragment;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupView();
        setupTabs();
        homeTimelineFragment = (HomeTimelineFragment)getSupportFragmentManager().findFragmentByTag("HomeTimelineFragment");
    }

    private void setupView() {
        //TODO: Figure out a better way.
        getActionBar().setTitle("");
        TwitterApplication.getRestClient().getVerifyCredentials( new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                user = User.fromJSON(jsonObject);
                getActionBar().setTitle(user.getScreenName());
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d(TAG, throwable.toString());
                Log.d(TAG, s);
            }
        });
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab homeTab = actionBar
                .newTab()
                .setText("Home")
                .setTag("HomeTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "first",
                                HomeTimelineFragment.class));

        actionBar.addTab(homeTab);
        actionBar.selectTab(homeTab);

        Tab mentionsTab = actionBar
                .newTab()
                .setText("Mentions")
                .setTag("MentionsTimelineFragment")
                .setTabListener(
                        new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "second",
                                MentionsTimelineFragment.class));

        actionBar.addTab(mentionsTab);
    }

    private void composeTweet() {
        //TODO: pass user in compose fragment.
        ComposeFragment composeFragment =  ComposeFragment.newInstance("",0);
        composeFragment.show(getSupportFragmentManager(),"compose_fragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_compose_tweet:
                composeTweet();
                return true;
            case R.id.action_profile:
                onProfileView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void onProfileView() {
        Intent i = new Intent(this,ProfileActivity.class);
        i.putExtra("user",user);
        startActivity(i);
    }

    @Override
    public void onPostTweet(boolean success, Tweet tweet) {
        if (success){
            homeTimelineFragment.insert(tweet,0);
        }
    }
}
