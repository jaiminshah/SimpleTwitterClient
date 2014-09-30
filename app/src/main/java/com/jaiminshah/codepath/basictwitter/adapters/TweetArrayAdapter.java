package com.jaiminshah.codepath.basictwitter.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jaimins on 9/26/14.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetArrayAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item,parent,false);
            viewHolder.ivRetweetedIcon = (ImageView)convertView.findViewById(R.id.ivRetweetedIcon);
            viewHolder.tvRetweetedBy = (TextView)convertView.findViewById(R.id.tvRetweetedBy);
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenname = (TextView)convertView.findViewById(R.id.tvScreenname);
            viewHolder.tvTimeElapsed = (TextView)convertView.findViewById(R.id.tvTimeElapsed);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.ivMedia = (ImageView)convertView.findViewById(R.id.ivMedia);
            viewHolder.ivReply = (ImageView)convertView.findViewById(R.id.ivReply);
            viewHolder.ivRetweet = (ImageView)convertView.findViewById(R.id.ivRetweet);
            viewHolder.tvRetweet = (TextView)convertView.findViewById(R.id.tvRetweet);
            viewHolder.ivFavCount = (ImageView)convertView.findViewById(R.id.ivFavCount);
            viewHolder.tvFavCount = (TextView)convertView.findViewById(R.id.tvFavCount);
            viewHolder.ivFollow = (ImageView)convertView.findViewById(R.id.ivFollow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if (tweet.getRetweeted_status() != null){
            viewHolder.ivRetweetedIcon.setVisibility(View.VISIBLE);
            viewHolder.tvRetweetedBy.setText(tweet.getUser().getName() + " retweeted");
            viewHolder.tvRetweetedBy.setVisibility(View.VISIBLE);
            tweet = tweet.getRetweeted_status();
        } else {
            viewHolder.ivRetweetedIcon.setVisibility(View.GONE);
            viewHolder.tvRetweetedBy.setVisibility(View.GONE);
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);

        viewHolder.tvUsername.setText(tweet.getUser().getName());
        viewHolder.tvScreenname.setText(tweet.getUser().getScreenName());
        viewHolder.tvTimeElapsed.setText(tweet.getRelativeTimeAgo());
        viewHolder.tvBody.setText(Html.fromHtml(tweet.getFormattedBody()));

        if (tweet.getTwitterMedias() != null && tweet.getTwitterMedias().size() > 0){
            imageLoader.displayImage(tweet.getTwitterMedias().get(0).getMedia_url_https(),viewHolder.ivMedia);
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivMedia.setVisibility(View.GONE);
        }

        if (tweet.isRetweeted()){
            viewHolder.ivRetweet.setImageResource(R.drawable.ic_tweet_action_inline_retweet_on);
        } else {
            viewHolder.ivRetweet.setImageResource(R.drawable.ic_tweet_action_inline_retweet_off);
        }
        viewHolder.tvRetweet.setText(Integer.toString(tweet.getRetweet_count()));

        if (tweet.isFavorited()){
            viewHolder.ivFavCount.setImageResource(R.drawable.ic_tweet_action_inline_favorite_on);
        } else {
            viewHolder.ivFavCount.setImageResource(R.drawable.ic_tweet_action_inline_favorite_off);
        }
        viewHolder.tvFavCount.setText(Integer.toString(tweet.getFavorite_count()));

        return  convertView;
    }


    private static class ViewHolder{
        ImageView ivRetweetedIcon;
        TextView tvRetweetedBy;
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvScreenname;
        TextView tvBody;
        TextView tvTimeElapsed;
        ImageView ivMedia;
        ImageView ivReply;
        ImageView ivRetweet;
        TextView tvRetweet;
        ImageView ivFavCount;
        TextView tvFavCount;
        ImageView ivFollow;

    }
}
