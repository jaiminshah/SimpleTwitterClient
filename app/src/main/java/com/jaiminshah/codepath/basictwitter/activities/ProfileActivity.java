package com.jaiminshah.codepath.basictwitter.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.fragments.ComposeFragment;
import com.jaiminshah.codepath.basictwitter.fragments.UserInfoFragment;
import com.jaiminshah.codepath.basictwitter.fragments.UserTimelineFragment;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.models.User;

public class ProfileActivity extends FragmentActivity implements ComposeFragment.ComposeFragmentListener{

    private static final String TAG = ProfileActivity.class.getName();
    private User user;
    private UserTimelineFragment userTimelineFragment;
    private UserInfoFragment userInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadProfileInfo();
    }

    private void loadProfileInfo(){
        user = getIntent().getParcelableExtra("user");
        getActionBar().setTitle(user.getScreenName());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        userTimelineFragment = UserTimelineFragment.newInstance(user);
        ft.replace(R.id.flUserTimeline,userTimelineFragment);

        userInfoFragment =  UserInfoFragment.newInstance(user);
        ft.replace(R.id.flUserInfo, userInfoFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostTweet(boolean success, Tweet tweet) {
        userTimelineFragment.insert(tweet,0);
    }
}
