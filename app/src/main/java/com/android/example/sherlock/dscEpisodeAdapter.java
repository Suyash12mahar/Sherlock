package com.android.example.sherlock;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suyash on 29-12-2016.
 */

public class dscEpisodeAdapter extends ArrayAdapter<dscEpisode>{
    private Activity activity;
    int id;
    ArrayList<dscEpisode> items;

    public dscEpisodeAdapter(Activity activity, int id, ArrayList<dscEpisode> items) {
        super(activity,id,items);
        this.activity = activity;
        this.id = id;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        dscEpisode episode = items.get(position);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.ei_episode_title);
        ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.ei_episode_image);

        titleTextView.setText((episode.getTitle()));

        String imageName = String.format("img_%d_%d", episode.getSeriesNumber(), episode.getEpisodeNumber());
        int resourceId = -1;
        resourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
        if (resourceId != 0) {
            thumbnailImage.setImageResource(activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName()));
        } else {
            thumbnailImage.setImageResource(R.drawable.thumbnail_extra_wide_png);
        }

        return convertView;
    }
}
