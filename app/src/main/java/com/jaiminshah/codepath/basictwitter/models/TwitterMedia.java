package com.jaiminshah.codepath.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jaimins on 9/28/14.
 */
public class TwitterMedia implements Parcelable {
    private String url;
    private String expanded_url;
    private String display_url;
    private String media_url_https;

    public String getExpanded_url() {
        return expanded_url;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public String getMedia_url_https() {
        return media_url_https;
    }

    public static ArrayList<TwitterMedia> fromJSONArray(JSONArray jsonArray) {
        ArrayList<TwitterMedia> twitterMedias = new ArrayList<TwitterMedia>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject twitterMediaJSON = null;
            try {
                twitterMediaJSON = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            TwitterMedia twitterMedia = TwitterMedia.fromJSON(twitterMediaJSON);
            if (twitterMedia != null) {
                twitterMedias.add(twitterMedia);
            }
        }

        return twitterMedias;
    }
    public static TwitterMedia fromJSON(JSONObject jsonObject) {
        TwitterMedia twitterMedia = new TwitterMedia();
        try {
            twitterMedia.url = jsonObject.getString("url");
            twitterMedia.expanded_url = jsonObject.getString("expanded_url");
            twitterMedia.display_url = jsonObject.getString("display_url");
            twitterMedia.media_url_https = jsonObject.getString("media_url_https");
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return twitterMedia;
    }

    public String gethtmlUrl(){
        return "<a href=\"" + media_url_https + "\">" + display_url + "</a>";
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.expanded_url);
        dest.writeString(this.display_url);
        dest.writeString(this.media_url_https);
    }

    public TwitterMedia() {
    }

    private TwitterMedia(Parcel in) {
        this.url = in.readString();
        this.expanded_url = in.readString();
        this.display_url = in.readString();
        this.media_url_https = in.readString();
    }

    public static final Parcelable.Creator<TwitterMedia> CREATOR = new Parcelable.Creator<TwitterMedia>() {
        public TwitterMedia createFromParcel(Parcel source) {
            return new TwitterMedia(source);
        }

        public TwitterMedia[] newArray(int size) {
            return new TwitterMedia[size];
        }
    };
}
