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
    private int retweet_count;
    private boolean retweeted;
    private Tweet retweeted_status;
    private int favorite_count;
    private boolean favorited;

    private ArrayList<TwitterUrl> twitterUrls;
    private ArrayList<TwitterMedia> twitterMedias;

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
            tweet.retweet_count = jsonObject.getInt("retweet_count");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.favorite_count = jsonObject.getInt("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            if (!jsonObject.isNull("retweeted_status")){
                tweet.retweeted_status = Tweet.fromJSON(jsonObject.getJSONObject("retweeted_status"));
            }
            if (!jsonObject.getJSONObject("entities").isNull("urls")) {
                tweet.twitterUrls = TwitterUrl.fromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("urls"));
            }
            if (!jsonObject.getJSONObject("entities").isNull("media")) {
                tweet.twitterMedias = TwitterMedia.fromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("media"));
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
        for (TwitterUrl twitterUrl : this.twitterUrls) {
            result = result.replaceAll(twitterUrl.getUrl(), twitterUrl.gethtmlUrl());
        }
//        for (TwitterMedia twitterMedia : this.twitterMedias){
//            result = result.replaceAll(twitterMedia.getUrl(), twitterMedia.gethtmlUrl());
//        }
        for (TwitterMedia twitterMedia : this.twitterMedias){
            result = result.replaceAll(twitterMedia.getUrl(), "");
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

    public int getRetweet_count() {
        return retweet_count;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public ArrayList<TwitterMedia> getTwitterMedias() {
        return twitterMedias;
    }

    public ArrayList<TwitterUrl> getTwitterUrls() {
        return twitterUrls;
    }

    public Tweet() {
        this.twitterUrls = new ArrayList<TwitterUrl>();
        this.twitterMedias = new ArrayList<TwitterMedia>();
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
        dest.writeInt(this.retweet_count);
        dest.writeByte(retweeted ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.retweeted_status, 0);
        dest.writeInt(this.favorite_count);
        dest.writeByte(favorited ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.twitterUrls);
        dest.writeTypedList(this.twitterMedias);
    }

    private Tweet(Parcel in) {
        this();
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.retweet_count = in.readInt();
        this.retweeted = in.readByte() != 0;
        this.retweeted_status = in.readParcelable(Tweet.class.getClassLoader());
        this.favorite_count = in.readInt();
        this.favorited = in.readByte() != 0;
        in.readTypedList(this.twitterUrls, TwitterUrl.CREATOR);
        in.readTypedList(this.twitterMedias, TwitterMedia.CREATOR);
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
