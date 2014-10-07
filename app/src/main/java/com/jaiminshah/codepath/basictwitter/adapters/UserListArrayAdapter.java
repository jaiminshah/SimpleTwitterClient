package com.jaiminshah.codepath.basictwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaiminshah.codepath.basictwitter.R;
import com.jaiminshah.codepath.basictwitter.activities.ProfileActivity;
import com.jaiminshah.codepath.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jaimins on 10/6/14.
 */
public class UserListArrayAdapter extends ArrayAdapter<User> {

    public UserListArrayAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenname = (TextView) convertView.findViewById(R.id.tvScreenname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(user.getProfileImageUrl(), viewHolder.ivProfileImage);
        final User tempUser = user;
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", tempUser);
                getContext().startActivity(i);
            }
        });

        viewHolder.tvUsername.setText(user.getName());
        viewHolder.tvScreenname.setText(user.getScreenName());
        return convertView;
    }
        private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvScreenname;
    }
}
