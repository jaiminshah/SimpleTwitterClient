package com.jaiminshah.codepath.basictwitter.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.fragments.ComposeFragment;
import com.jaiminshah.codepath.basictwitter.fragments.HomeTimelineFragment;
import com.jaiminshah.codepath.basictwitter.fragments.MentionsTimelineFragment;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.listeners.FragmentTabListener;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.jaiminshah.codepath.basictwitter.utils.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class TimelineActivity extends FragmentActivity implements ComposeFragment.ComposeFragmentListener {

    private static final String TAG = TimelineActivity.class.getName();
    private HomeTimelineFragment homeTimelineFragment;
    private User user;
    SharedPreferences mSettings;
    SearchView mSearchView;
    String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mSettings = getSharedPreferences("Settings", 0);
        setupView();
        setupTabs();
        homeTimelineFragment = (HomeTimelineFragment)getSupportFragmentManager().findFragmentByTag("HomeTimelineFragment");
    }

    private void setupView() {
        //TODO: Figure out a better way.
        getActionBar().setTitle("");
        if (NetworkUtils.isNetworkAvailable(this)) {
            TwitterApplication.getRestClient().getVerifyCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    user = User.fromJSON(jsonObject);
                    getActionBar().setTitle(user.getScreenName());
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putLong("user_id", user.getUid());
                    editor.commit();
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    Log.d(TAG, throwable.toString());
                    Log.d(TAG, s);
                }
            });
        } else {
            user = User.getUser(mSettings.getLong("user_id",0));
        }

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
        ComposeFragment composeFragment =  ComposeFragment.newInstance(user,"",0);
        composeFragment.show(getSupportFragmentManager(),"compose_fragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                Intent i = new Intent(getBaseContext(), SearchResultActivity.class);
                i.putExtra("query",mQuery);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

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
