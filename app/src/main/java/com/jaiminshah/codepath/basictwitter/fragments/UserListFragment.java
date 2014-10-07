package com.jaiminshah.codepath.basictwitter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.adapters.UserListArrayAdapter;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterClient;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UserListFragment extends Fragment {

    private User mUser;
    private ArrayList<User> mUserList;
    private String mTitle;
    private TwitterClient client = TwitterApplication.getRestClient();
    private UserListArrayAdapter maUserList;


    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(User user, String title){
        UserListFragment userListFragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putParcelable("user",user);
        args.putString("title",title);
        userListFragment.setArguments(args);
        return userListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserList = new ArrayList<User>();
        maUserList = new UserListArrayAdapter(getActivity(),mUserList);

        mUser = getArguments().getParcelable("user");
        mTitle = getArguments().getString("title");

        if (mTitle.equals("Following")){
            client.getFriendsList(mUser.getUid(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(JSONObject jsonObject) {
                  updateUserList(jsonObject);
                }
            });
        } else if (mTitle.equals("Followers")){
            client.getFollowersList(mUser.getUid(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    updateUserList(jsonObject);
                }
            });
        }


    }

    public void updateUserList(JSONObject jsonObject){
        if (!jsonObject.isNull("users")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = jsonObject.getJSONArray("users");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            maUserList.addAll(User.fromJSONArray(jsonArray));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ListView lvUserList = (ListView)view.findViewById(R.id.lvUserList);
        lvUserList.setAdapter(maUserList);

        return view;
    }


}
