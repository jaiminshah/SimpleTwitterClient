package com.jaiminshah.codepath.basictwitter.activities;

import android.app.Activity;
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
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvStatus;
    private TextView tvCreatedAt;
    private ImageView ivMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupViews();
    }

    private void setupViews() {
        tweet = (Tweet) getIntent().getParcelableExtra("tweet");

        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvScreenname = (TextView)findViewById(R.id.tvScreenname);
        tvStatus = (TextView)findViewById(R.id.tvStatus);
        tvCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
        ivMedia = (ImageView)findViewById(R.id.ivMedia);

        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        tvUsername.setText(tweet.getUser().getName());
        tvScreenname.setText(tweet.getUser().getScreenName());
        tvStatus.setText(Html.fromHtml(tweet.getFormattedBody()));
        tvStatus.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreatedAt.setText(tweet.getFormattedCreatedAt());

        if (tweet.getTwitterMedias() != null && tweet.getTwitterMedias().size() > 0){
            ImageLoader.getInstance().displayImage(tweet.getTwitterMedias().get(0).getMedia_url_https(),ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        } else {
            ivMedia.setVisibility(View.GONE);
        }

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
