package com.jaiminshah.codepath.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.adapters.TweetArrayAdapter;
import com.jaiminshah.codepath.basictwitter.helpers.EndlessScrollListener;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterClient;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends Activity {

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
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }

    private void composeTweet() {
        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String status = data.getExtras().getString("status");
            client.postUpdate(status,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    aTweets.clear();
                    max_id = 0;
                    populateTimeline();
                }


                @Override
                public void onFailure(Throwable throwable, String s) {
                    Log.d("debug", throwable.toString());
                    Log.d("debug", s);
                }
            });
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

}
