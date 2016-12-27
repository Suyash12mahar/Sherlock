package com.android.example.sherlock;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends ArrayAdapter<EpisodeItem> {
    private Activity activity;
    int id;
    ArrayList<EpisodeItem> items;

    public EpisodeAdapter(Activity context, int resource, ArrayList<EpisodeItem> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.id = resource;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }
        EpisodeItem item = items.get(position);
        TextView title_text_view = (TextView)convertView.findViewById(R.id.title_text_view);
        ImageView episode_image_view = (ImageView) convertView.findViewById(R.id.episode_image_view);
        TextView short_details_view = (TextView) convertView.findViewById(R.id.short_details_view);
        TextView air_date_view = (TextView)convertView.findViewById(R.id.air_date_view);
        //TextView Views_view = (TextView)convertView.findViewById(R.id.views_view);

        title_text_view.setText(item.getEpTitle());
        air_date_view.setText(item.getAirDate());
        //Views_view.setText(item.getViews() + " mn");
        short_details_view.setText(item.getShortDescription());
        episode_image_view.setImageResource(item.getImageId());
        return convertView;
    }
}
