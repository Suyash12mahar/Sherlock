package com.android.example.sherlock;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suyash on 25-11-2016.
 */
public class SeasonAdapter extends ArrayAdapter<SeasonItem> {
    private Activity activity;
    int id;
    ArrayList<SeasonItem> items;
    public SeasonAdapter(Activity context, int resource, ArrayList<SeasonItem> objects) {
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
        SeasonItem item = items.get(position);
        TextView season_text = (TextView)convertView.findViewById(R.id.season_number);
        TextView year_text = (TextView)convertView.findViewById(R.id.year_view);
        TextView rating_text = (TextView)convertView.findViewById(R.id.rating_view);

        season_text.setText(item.getSeasonNumber());
        year_text.setText(item.getYear());
        rating_text.setText("★ " + item.getRatings());
        return convertView;
    }
}
