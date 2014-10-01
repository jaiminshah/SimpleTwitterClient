package com.jaiminshah.codepath.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jaimins on 9/28/14.
 */
@Table(name = "TwitterUrl")
public class TwitterUrl extends Model implements Parcelable {
    @Column(name = "url")
    private String url;
    @Column(name = "expanded_url")
    private String expanded_url;
    @Column(name = "display_url")
    private String display_url;

    public TwitterUrl() {
        super();
    }

    public static ArrayList<TwitterUrl> fromJSONArray(JSONArray jsonArray) {
        ArrayList<TwitterUrl> twitterUrls = new ArrayList<TwitterUrl>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject urlJSON = null;
            try {
                urlJSON = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            TwitterUrl twitterUrl = TwitterUrl.fromJSON(urlJSON);
            if (twitterUrl != null) {
                twitterUrls.add(twitterUrl);
            }
        }

        return twitterUrls;
    }

    public static TwitterUrl fromJSON(JSONObject jsonObject) {
        TwitterUrl twitterUrl = new TwitterUrl();
        try {
            twitterUrl.url = jsonObject.getString("url");
            twitterUrl.expanded_url = jsonObject.getString("expanded_url");
            twitterUrl.display_url = jsonObject.getString("display_url");
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return twitterUrl;
    }

    public String gethtmlUrl(){
        return "<a href=\"" + expanded_url + "\">" + display_url + "</a>";
    }

    public String getUrl() {
        return url;
    }

    public String getExpanded_url() {
        return expanded_url;
    }

    public String getDisplay_url() {
        return display_url;
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
    }

    private TwitterUrl(Parcel in) {
        this();
        this.url = in.readString();
        this.expanded_url = in.readString();
        this.display_url = in.readString();
    }

    public static final Parcelable.Creator<TwitterUrl> CREATOR = new Parcelable.Creator<TwitterUrl>() {
        public TwitterUrl createFromParcel(Parcel source) {
            return new TwitterUrl(source);
        }

        public TwitterUrl[] newArray(int size) {
            return new TwitterUrl[size];
        }
    };
}
