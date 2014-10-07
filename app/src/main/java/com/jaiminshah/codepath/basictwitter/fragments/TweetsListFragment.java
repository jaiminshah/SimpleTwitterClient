package com.jaiminshah.codepath.basictwitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.activities.DetailActivity;
import com.jaiminshah.codepath.basictwitter.adapters.TweetArrayAdapter;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterClient;
import com.jaiminshah.codepath.basictwitter.listeners.EndlessScrollListener;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.jaiminshah.codepath.basictwitter.utils.NetworkUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jaimins on 10/3/14.
 */
public abstract class TweetsListFragment extends Fragment {

    protected TwitterClient client;
    protected ArrayList<Tweet> tweets;
    protected TweetArrayAdapter aTweets;
    protected ListView lvTweets;
    protected SwipeRefreshLayout swipeContainer;
    protected ProgressBar mProgressBar;
    protected long max_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(getActivity(),tweets);
        //Initialize the max_id to the least possible number.
        max_id = 0;
        client = TwitterApplication.getRestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list,container,false);
        setupViews(view);
        mProgressBar.setVisibility(View.VISIBLE);
        populateTimeline();
        return view;
    }

    private void setupViews(View view) {
        lvTweets = (ListView)view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
//                Toast.makeText(getBaseContext(),"Load More!!",Toast.LENGTH_SHORT).show();
                if (!NetworkUtils.isNetworkAvailable(getActivity())){
                    return;
                }
                populateTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet",tweet);
                startActivity(i);
            }
        });

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkUtils.isNetworkAvailable(getActivity())){
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

        mProgressBar = (ProgressBar)view.findViewById(R.id.pbTweetList);
    }

    public void populateTimeline(){

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            fetchFromApi();
        } else {
            fetchFromDB();
            Toast.makeText(getActivity(), "Offline Mode", Toast.LENGTH_SHORT).show();
        }

    }

    protected void fetchFromApi(){
        // Check if we have already fetch tweets once.
        // if we already fetched than fetch the next set of tweets
        // from (max_id - 1). max_id is inclusive so you need decrement it one.
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getUid() - 1 ;
        }

        client.getHomeTimeline(max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray jsonArray) {
                addAll(Tweet.fromJSONArray(jsonArray));
//                 Log.d("debug", jsonArray.toString());
                for (Tweet tweet : tweets){
                    tweet.saveTweet();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }

    protected void fetchFromDB(){
        aTweets.clear();
        aTweets.addAll(Tweet.getAll());
    }

    public void insert(Tweet tweet,int position){
        aTweets.insert(tweet,position);
    }
    public void addAll(ArrayList<Tweet> tweets){
        aTweets.addAll(tweets);

    }

}
