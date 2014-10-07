package com.jaiminshah.codepath.basictwitter.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

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
    public void onPostTweet(boolean success, Tweet tweet) {
        userTimelineFragment.insert(tweet,0);
    }

    public void onFollowingClick(View view){
        userInfoFragment.onFollowingClick(view);
    }

    public void onFollowersClick(View view){
        userInfoFragment.onFollowersClick(view);
    }
}
