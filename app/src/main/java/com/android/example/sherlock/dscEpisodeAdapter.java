package com.android.example.sherlock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.Settings;
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

    final static String PREFERENCE_NAME = "com.android.example.sherlock.favourite";
    SharedPreferences settings;
    String bookmarkString = "";

    public int seriesNumber;
    public int episodeNumber;

    public ImageView bookmarkImageView;

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
        settings = activity.getSharedPreferences(PREFERENCE_NAME, 0);
        bookmarkString = settings.getString("bookmark", "");

        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        dscEpisode episode = items.get(position);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.ei_episode_title);
        ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.ei_episode_image);
        ImageView bookmarkImage = (ImageView) convertView.findViewById(R.id.ei_bookmark_icon);
        bookmarkImageView = bookmarkImage;
        if (!SettingsStorage.isCurrentPageBookmarked(bookmarkString, episode.getSeriesNumber(), episode.getEpisodeNumber())){
            bookmarkImage.setVisibility(View.GONE);
        }
        titleTextView.setText((episode.getTitle()));

        episodeNumber = episode.getEpisodeNumber();
        seriesNumber = episode.getSeriesNumber();
        // Sets image resource for episode image (series 1, episode3 : img_1_2.png)
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
