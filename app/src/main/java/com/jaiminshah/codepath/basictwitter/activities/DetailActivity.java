package com.jaiminshah.codepath.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends Activity {

    private Tweet tweet;
    private ImageView ivRetweetedIcon;
    private TextView tvRetweetBy;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvStatus;
    private TextView tvCreatedAt;
    private ImageView ivMedia;
    private TextView tvRetweet;
    private TextView tvFavCount;
    private ImageView ivRetweet;
    private ImageView ivFavCount;

    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupViews();
    }

    private void setupViews() {
        tweet = (Tweet) getIntent().getParcelableExtra("tweet");

        ivRetweetedIcon = (ImageView)findViewById(R.id.ivRetweetedIcon);
        tvRetweetBy = (TextView)findViewById(R.id.tvRetweetedBy);
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvScreenname = (TextView)findViewById(R.id.tvScreenname);
        tvStatus = (TextView)findViewById(R.id.tvStatus);
        tvCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
        ivMedia = (ImageView)findViewById(R.id.ivMedia);
        ivRetweet = (ImageView)findViewById(R.id.ivRetweet);
        tvRetweet = (TextView)findViewById(R.id.tvRetweet);
        ivFavCount = (ImageView)findViewById(R.id.ivFavCount);
        tvFavCount = (TextView)findViewById(R.id.tvFavCount);

        if (tweet.getRetweeted_status() != null){
            ivRetweetedIcon.setVisibility(View.VISIBLE);
            tvRetweetBy.setText(tweet.getUser().getName());
            tvRetweetBy.setVisibility(View.VISIBLE);
            tweet = tweet.getRetweeted_status();
        } else {
            ivRetweetedIcon.setVisibility(View.GONE);
            tvRetweetBy.setVisibility(View.GONE);

        }

        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        tvUsername.setText(tweet.getUser().getName());
        tvScreenname.setText(tweet.getUser().getScreenName());
        tvStatus.setText(Html.fromHtml(tweet.getFormattedBody()));
        tvStatus.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreatedAt.setText(tweet.getFormattedCreatedAt());

        if (tweet.isRetweeted()){
            ivRetweet.setImageResource(R.drawable.ic_tweet_action_inline_retweet_on);
        } else {
            ivRetweet.setImageResource(R.drawable.ic_tweet_action_inline_retweet_off);
        }
        tvRetweet.setText(Integer.toString(tweet.getRetweet_count()));

        if (tweet.isFavorited()){
            ivFavCount.setImageResource(R.drawable.ic_tweet_action_inline_favorite_on);
        } else {
            ivFavCount.setImageResource(R.drawable.ic_tweet_action_inline_favorite_off);
        }
        tvFavCount.setText(Integer.toString(tweet.getFavorite_count()));

        if (tweet.getTwitterMedias() != null && tweet.getTwitterMedias().size() > 0){
            ImageLoader.getInstance().displayImage(tweet.getTwitterMedias().get(0).getMedia_url_https(),ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        } else {
            ivMedia.setVisibility(View.GONE);
        }

    }


    public void replyTweet(View view) {
        Intent i = new Intent(this,ComposeActivity.class);
        i.putExtra("reply",tweet.getUser().getScreenName());
        startActivityForResult(i,REQUEST_CODE );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
