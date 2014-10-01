package com.jaiminshah.codepath.basictwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.adapters.TweetArrayAdapter;
import com.jaiminshah.codepath.basictwitter.fragments.ComposeFragment;
import com.jaiminshah.codepath.basictwitter.helpers.EndlessScrollListener;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterClient;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

public class TimelineActivity extends FragmentActivity implements ComposeFragment.ComposeFragmentListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;
    long max_id;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
//        ActiveAndroid.setLoggingEnabled(true);
        setupView();
        populateTimeline();

    }

    private void setupView() {

        getActionBar().setTitle("");
        client = TwitterApplication.getRestClient();
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this,tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
//                Toast.makeText(getBaseContext(),"Load More!!",Toast.LENGTH_SHORT).show();
                if (!isNetworkAvailable()){
                    return;
                }
                populateTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(TimelineActivity.this, DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet",tweet);
                startActivity(i);
            }
        });

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isNetworkAvailable()){
                    swipeContainer.setRefreshing(false);
                    return;
                }
                aTweets.clear();
                max_id = 0;
                populateTimeline();
            }
            
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Initialize the max_id to the least possible number.
        max_id = 0;
    }

    public void populateTimeline(){

        if (isNetworkAvailable()) {
            fetchFromApi();
        } else {
            fetchFromDB();
            Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchFromApi(){
        // Check if we have already fetch tweets once.
        // if we already fetched than fetch the next set of tweets
        // from (max_id - 1). max_id is inclusive so you need decrement it one.
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getUid() - 1 ;
        }

        client.getHomeTimeline(max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray jsonArray) {
                aTweets.addAll(Tweet.fromJSONArray(jsonArray));
//                 Log.d("debug", jsonArray.toString());
                for (Tweet tweet : tweets){
                    tweet.saveTweet();
                }
                int numberInDB = Tweet.getAll().size();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }

    private void fetchFromDB(){
        aTweets.clear();
        aTweets.addAll(Tweet.getAll());
    }

    private void composeTweet() {
        ComposeFragment composeFragment =  ComposeFragment.newInstance("");
        composeFragment.show(getSupportFragmentManager(),"compose_fragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
//                    aTweets.clear();
//                    max_id = 0;
//                    populateTimeline();
            Tweet tweet = data.getParcelableExtra("tweet");
            aTweets.insert(tweet,0);
        }
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
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onPostTweet(boolean success, Tweet tweet) {
        if (success){
//
//            aTweets.clear();
//            max_id = 0;
//            populateTimeline();
            aTweets.insert(tweet,0);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
