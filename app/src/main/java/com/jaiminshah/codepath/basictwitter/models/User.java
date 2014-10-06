package com.jaiminshah.codepath.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jaimins on 9/26/14.
 */
@Table(name = "User")
public class User extends Model implements Parcelable {
//    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @Column(name = "uid")
    private long uid;
    @Column(name = "followers")
    private long followers;
    @Column(name = "friends")
    private long friends;
    @Column(name = "statusCount")
    private long statusCount;
    @Column(name = "name")
    private String name;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "profileBgUrl")
    private String profileBgUrl;



    public User() {
        super();
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.followers = jsonObject.getLong("followers_count");
            user.friends = jsonObject.getLong("friends_count");
            user.statusCount = jsonObject.getLong("statuses_count");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            if (!jsonObject.isNull("profile_banner_url")){
                user.profileBgUrl = jsonObject.getString("profile_banner_url");
            } else {
                user.profileBgUrl = "";
            }
            if (user.profileImageUrl.contains("_normal.")){
                user.profileImageUrl = user.profileImageUrl.replace("_normal.",".");
            }
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public long getFollowers() {
        return followers;
    }

    public long getFriends() {
        return friends;
    }

    public long getStatusCount() {
        return statusCount;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBgUrl() {
        return profileBgUrl;
    }

    public List<Tweet> tweets(){
        return getMany(Tweet.class,"user");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeLong(this.followers);
        dest.writeLong(this.friends);
        dest.writeLong(this.statusCount);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.profileBgUrl);
    }

    private User(Parcel in) {
        this.uid = in.readLong();
        this.followers = in.readLong();
        this.friends = in.readLong();
        this.statusCount = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.profileBgUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
