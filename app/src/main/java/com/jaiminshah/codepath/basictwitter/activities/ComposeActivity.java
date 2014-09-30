package com.jaiminshah.codepath.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.helpers.TwitterApplication;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class ComposeActivity extends Activity {

    EditText etComposeTweet;
    MenuItem action_chars_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        setupViews();
    }

    private void setupViews() {

        etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        String replyTo = getIntent().getStringExtra("reply");
        etComposeTweet.setText(replyTo);
        etComposeTweet.setSelection(replyTo.length());
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
//                if (chars_left < 0) {
//                    chars_left = chars_left*(-1);
//                    SpannableString spanString = new SpannableString(Integer.toString(chars_left));
//                    spanString.setSpan(new ForegroundColorSpan(Color.RED),0,spanString.length(),0);
//                    action_chars_left.setTitle(spanString);
//                } else {
//
//                    action_chars_left.setTitle(Integer.toString(chars_left));
//                }
                action_chars_left.setTitle(Integer.toString(chars_left));
            }
        });
    }

    private void postTweet() {
        final Intent data = new Intent();
        String status = etComposeTweet.getText().toString();
        //Trim the status to 140 chars
//        status = status.substring(0,139);
//        data.putExtra("status", status);

        TwitterApplication.getRestClient().postUpdate(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Tweet tweet = Tweet.fromJSON(jsonObject);
                data.putExtra("tweet",tweet);
                setResult(RESULT_OK, data);
                finish();
            }


            @Override
            public void onFailure(Throwable throwable, String s) {
                Toast.makeText(getBaseContext(),"Sorry. Couldn't post. Please try again.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED,data);
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        action_chars_left = menu.findItem(R.id.action_chars_left);
        SpannableString spanString = new SpannableString(Integer.toString(140 - etComposeTweet.length()));
        spanString.setSpan(new ForegroundColorSpan(Color.RED),0,spanString.length(),0);
        action_chars_left.setTitle(spanString);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post_tweet:
                postTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
