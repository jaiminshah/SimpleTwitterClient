package com.jaiminshah.codepath.basictwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

public class ComposeFragment extends DialogFragment {

    private ComposeFragmentListener listener;
    private User user;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvCharsLeft;
    private Button btnTweet;
    private EditText etComposeTweet;

    public ComposeFragment(){

    }

    public static ComposeFragment newInstance(String replyTo){
        ComposeFragment composeFragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("replyTo", replyTo);
        composeFragment.setArguments(args);
        return composeFragment;
    }

    public interface ComposeFragmentListener{
        public void onPostTweet(boolean success, Tweet tweet);
    }

    private void setupViews(View view) {

        listener = (ComposeFragmentListener)getActivity();
        ivProfileImage = (ImageView)view.findViewById(R.id.ivProfileImage);
        tvUsername = (TextView)view.findViewById(R.id.tvUsername);
        tvScreenname = (TextView)view.findViewById(R.id.tvScreenname);
        tvCharsLeft = (TextView)view.findViewById(R.id.tvCharsLeft);
        btnTweet = (Button)view.findViewById(R.id.btnTweet);
        etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);

        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),ivProfileImage);
        tvUsername.setText(user.getName());
        tvScreenname.setText(user.getScreenName());

        btnTweet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet();
            }
        });

        String replyTo = getArguments().getString("replyTo", "");
        tvCharsLeft.setText(Integer.toString(140 - replyTo.length()));
        etComposeTweet.setText(replyTo);
        etComposeTweet.setSelection(etComposeTweet.length());
        etComposeTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int chars_left = 140 - s.length();
                if (chars_left < 0) {
                    tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvCharsLeft.setTextColor(getResources().getColor(R.color.secondary_gary_3));
                }
                tvCharsLeft.setText(Integer.toString(chars_left));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_compose, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TwitterApplication.getRestClient().getVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                user = User.fromJSON(jsonObject);
                setupViews(view);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });

        return view;
    }

    private void postTweet() {

        String status = etComposeTweet.getText().toString();

        TwitterApplication.getRestClient().postUpdate(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Tweet tweet = Tweet.fromJSON(jsonObject);
                listener.onPostTweet(true,tweet);
                getDialog().dismiss();
            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                Toast.makeText(getActivity(), "Sorry. Couldn't post. Please try again.", Toast.LENGTH_SHORT).show();
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });


    }
}
