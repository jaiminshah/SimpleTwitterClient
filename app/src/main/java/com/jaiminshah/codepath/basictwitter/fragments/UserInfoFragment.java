package com.jaiminshah.codepath.basictwitter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserInfoFragment extends Fragment {
    private User mUser;
    private ImageView ivProfileBgImage;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvTweetsCount;
    private TextView tvFollowingCount;
    private TextView tvFollowersCount;


    public UserInfoFragment() {
        // Required empty public constructor
    }

    public static UserInfoFragment newInstance(User user){
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("user",user);
        userInfoFragment.setArguments(args);
        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = getArguments().getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        ivProfileBgImage = (ImageView)view.findViewById(R.id.ivProfileBackground);
        ivProfileImage = (ImageView)view.findViewById(R.id.ivProfileImage);
        tvUsername = (TextView)view.findViewById(R.id.tvUsername);
        tvScreenname = (TextView)view.findViewById(R.id.tvScreenname);
        tvTweetsCount = (TextView)view.findViewById(R.id.tvTweetCount);
        tvFollowingCount = (TextView)view.findViewById(R.id.tvFollowingCount);
        tvFollowersCount = (TextView)view.findViewById(R.id.tvFollowersCount);

        ImageLoader.getInstance().displayImage(mUser.getProfileBgUrl(),ivProfileBgImage);
        ImageLoader.getInstance().displayImage(mUser.getProfileImageUrl(),ivProfileImage);
        tvUsername.setText(mUser.getName());
        tvScreenname.setText(mUser.getScreenName());
        tvTweetsCount.setText(Long.toString(mUser.getStatusCount()));
        tvFollowingCount.setText(Long.toString(mUser.getFriends()));
        tvFollowersCount.setText(Long.toString(mUser.getFollowers()));

        return view;
    }


}
