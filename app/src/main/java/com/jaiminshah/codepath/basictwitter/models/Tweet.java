package com.jaiminshah.codepath.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jaimins on 9/26/14.
 */
public class Tweet implements Parcelable {
    private String body;
    private long uid;
    private String createdAt;
    protected User user;
    //    private ArrayList<Url> urls;
    private Url url;

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject tweetJSON = null;
            try {
                tweetJSON = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJSON(tweetJSON);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
//            tweet.urls = Url.fromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("urls"));
            ArrayList<Url> urls = Url.fromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("urls"));
            if (urls.size() > 0) {
                tweet.url = urls.get(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return tweet;
    }


    public String getBody() {
        return body;
    }

    public String getFormattedBody() {
        String result = body;
//        for(Url url: this.urls){
//            result = result.replaceAll(url.getUrl(),url.gethtmlUrl());
//        }
        if (url != null) {
            result = result.replaceAll(url.getUrl(), url.gethtmlUrl());
        }
        return result;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Url getUrl(){
        return url;
    }
    public Tweet() {
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            String[] tokens = relativeDate.split(" ");
            // Convert "2 minutes ago" into "2m"
            if (tokens.length == 3) {
                relativeDate = tokens[0] + tokens[1].substring(0, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getFormattedCreatedAt() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String formattedCreatedAt = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            sf = new SimpleDateFormat("M/d/yy, K:ss a", Locale.ENGLISH);
            formattedCreatedAt = sf.format(dateMillis);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedCreatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.user, 0);
        dest.writeParcelable(this.url, 0);
    }

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.url = in.readParcelable(Url.class.getClassLoader());
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
