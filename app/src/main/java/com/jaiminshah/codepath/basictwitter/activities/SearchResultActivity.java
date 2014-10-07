package com.jaiminshah.codepath.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.fragments.SearchResultFragment;

public class SearchResultActivity extends FragmentActivity {
    String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mQuery = getIntent().getStringExtra("query");
        SearchResultFragment searchResultFragment = SearchResultFragment.newInstance(mQuery);
        getActionBar().setTitle("Search: " + mQuery);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flSearchResult,searchResultFragment);
        ft.commit();
    }


}
