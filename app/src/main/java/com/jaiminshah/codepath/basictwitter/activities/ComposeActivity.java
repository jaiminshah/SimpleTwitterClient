package com.jaiminshah.codepath.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jaiminshah.codepath.basictwitter.R;

public class ComposeActivity extends Activity {

    EditText etComposeTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        setupViews();
    }

    private void setupViews() {
        etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);
    }

    private void postTweet() {
        Intent data = new Intent();
        String status = etComposeTweet.getText().toString();
        //Trim the status to 140 chars
//        status = status.substring(0,139);
        data.putExtra("status",status);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_post_tweet:
                 postTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
