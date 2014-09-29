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
            viewHolder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenname = (TextView)convertView.findViewById(R.id.tvScreenname);
            viewHolder.tvTimeElapsed = (TextView)convertView.findViewById(R.id.tvTimeElapsed);
            viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.ivMedia = (ImageView)convertView.findViewById(R.id.ivMedia);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
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

        return  convertView;
    }


    private static class ViewHolder{
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvScreenname;
        TextView tvBody;
        TextView tvTimeElapsed;
        ImageView ivMedia;
    }
}
