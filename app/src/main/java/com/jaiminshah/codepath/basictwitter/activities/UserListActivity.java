package com.jaiminshah.codepath.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.fragments.UserListFragment;
import com.jaiminshah.codepath.basictwitter.models.User;

public class UserListActivity extends FragmentActivity {

    private User mUser;
    private String mTitle;
    private UserListFragment mUserListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mUser = getIntent().getParcelableExtra("user");
        mTitle = getIntent().getStringExtra("title");
        mUserListFragment = UserListFragment.newInstance(mUser,mTitle);
        getActionBar().setTitle(mUser.getScreenName() + " : " + mTitle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flUserList,mUserListFragment);
        ft.commit();

    }
}
