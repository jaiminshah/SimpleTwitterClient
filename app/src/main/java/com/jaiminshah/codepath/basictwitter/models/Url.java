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
public class Url implements Parcelable {
    private String url;
    private String expanded_url;
    private String display_url;

    public static ArrayList<Url> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Url> urls = new ArrayList<Url>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject urlJSON = null;
            try {
                urlJSON = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Url url = Url.fromJSON(urlJSON);
            if (url != null) {
                urls.add(url);
            }
        }

        return urls;
    }
    public static Url fromJSON(JSONObject jsonObject) {
        Url url = new Url();
        try {
            url.url = jsonObject.getString("url");
            url.expanded_url = jsonObject.getString("expanded_url");
            url.display_url = jsonObject.getString("display_url");
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return url;
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

    public Url() {
    }

    private Url(Parcel in) {
        this.url = in.readString();
        this.expanded_url = in.readString();
        this.display_url = in.readString();
    }

    public static final Parcelable.Creator<Url> CREATOR = new Parcelable.Creator<Url>() {
        public Url createFromParcel(Parcel source) {
            return new Url(source);
        }

        public Url[] newArray(int size) {
            return new Url[size];
        }
    };
}
