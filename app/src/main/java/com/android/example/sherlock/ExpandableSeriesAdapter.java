package com.android.example.sherlock; /**
 * Created by Suyash on 28-12-2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.R.attr.id;
import static android.R.attr.packageNames;

/**
 * Created by Suyash on 29-12-2016.
 */

public class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.SeriesViewHolder > {
    public Activity activity;
    private List<Series> list;
    private int expandedPosition = -1;

    private String bbcLink;
    private String imdbLink;

    private List<Integer> expandedPositions = new ArrayList<>();
    private ArrayList<SeriesViewHolder > holderList = new ArrayList<SeriesViewHolder>();
    int activeSeries = 1;

    public ExpandableSeriesAdapter(List<Series> list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final SeriesViewHolder  seriesViewHolder, int position) {
        final Series ci = list.get(seriesViewHolder.getAdapterPosition());
        seriesViewHolder.vTitle.setText("Series " + ci.getSeriesNumber());
        seriesViewHolder.vRatings.setText(ci.getRatings());
        seriesViewHolder.vAirDate.setText(ci.getAirDate());
        bbcLink = ci.getBbcLink();
        imdbLink = ci.getImdbLink();

        //
        if (position == expandedPosition) {
            seriesViewHolder.vExpandableArea.setVisibility(View.VISIBLE);
        } else {
            seriesViewHolder.vExpandableArea.setVisibility(View.GONE);
        }

        seriesViewHolder.vBbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        seriesViewHolder.vImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        seriesViewHolder.vExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesViewHolder  holder = (SeriesViewHolder ) v.getTag();

                // Adds current holder to holder list if it doesn't exists in same
                if (!holderList.contains(holder)){
                    holderList.add(holder);
                }

                // Collapses every item in holderList
                for (SeriesViewHolder holderItem : holderList){
                    if (holderItem.vExpandableArea.isShown() && !holderItem.equals(seriesViewHolder)){
                        Effects.dsc_collapse(v.getContext(), holderItem.vExpandableArea);
                        holderItem.vExpandableArea.setVisibility(View.GONE);
                        holderItem.vExpandCollapse.setImageResource(R.drawable.expand_arrow);
                    }
                }

                // Toggles expanded state for current holder
                if (seriesViewHolder.vExpandableArea.isShown()){
                    Effects.dsc_collapse(v.getContext(), seriesViewHolder.vExpandableArea);
                    seriesViewHolder.vExpandableArea.setVisibility(View.GONE);
                    seriesViewHolder.vExpandCollapse.setImageResource(R.drawable.expand_arrow);
                } else {
                    Effects.dsc_expand(v.getContext(), seriesViewHolder.vExpandableArea);
                    activeSeries =Integer.parseInt(seriesViewHolder.vTitle.getText()
                                        .charAt(seriesViewHolder.vTitle.getText().length()-1) + "");
                    seriesViewHolder.vExpandableArea.setVisibility(View.VISIBLE);
                    seriesViewHolder.vExpandCollapse.setImageResource(R.drawable.collapse_arrow);
                }
            }
        });

        int seriesNumber = ci.getSeriesNumber();
        DatabaseHelper dbHelper = new DatabaseHelper(activity);
        try{
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        }
        catch(SQLException e){
            Toast.makeText(activity, "Error reading database", Toast.LENGTH_LONG);
            Log.d("TAG", "Error accessing database");
        }

        String queryString = String.format(
                "SELECT * FROM \"EpisodeDetails\" WHERE SeasonNumber=\"%d\"",seriesNumber);
        ArrayList<dscEpisode> episodeList = new ArrayList<dscEpisode>();
        try{
            Cursor cursor = dbHelper.QueryData(queryString);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        dscEpisode item = new dscEpisode();
                        item.setTitle(cursor.getString(3));
                        item.setSeriesNumber(cursor.getInt(1));
                        item.setEpisodeNumber(cursor.getInt(2));

                        String imageName = String.format("img_%d_%d", seriesNumber, item.getEpisodeNumber());
                        int resourceId = -1;
                        resourceId = activity.getResources()
                                        .getIdentifier(imageName, "drawable", activity.getPackageName());
                        if (resourceId != 0) {
                            item.setThumbnailID(activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName()));
                        } else {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();

                            item.setThumbnailID(R.drawable.thumbnail_low_res);
                        }
                        episodeList.add(item);
                    }while (cursor.moveToNext());

                }
            }
        } catch (SQLiteException e){
            Log.d("TAG", "Error accessing database 2");
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        }
        dscEpisodeAdapter adapter = new dscEpisodeAdapter(activity, R.layout.dsc_episode_item, episodeList);
        seriesViewHolder.vEpisodesList.setAdapter(adapter);
        seriesViewHolder.vEpisodesList.setOnItemClickListener(episodeOnItemClickListener);

        seriesViewHolder.vImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imdbLink));
                v.getContext().startActivity(browserIntent);
            }
        });

        seriesViewHolder.vBbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bbcLink));
                v.getContext().startActivity(browserIntent);
            }
        });
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener episodeOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // This comment is of no use
            int i = parent.getCount();
            TextView ij = (TextView)parent.findViewById(R.id.ei_episode_title);
            Log.e("This",ij.getText().toString());
            Intent newerIntent = new Intent(activity, EpisodesDetails.class);
            newerIntent.putExtra("season_number", String.valueOf(activeSeries));
            newerIntent.putExtra("episode_number", String.valueOf(id+1));
            activity.startActivity(newerIntent);
        }
    };

    @Override
    public SeriesViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.detailed_series_card, viewGroup, false);

        return new SeriesViewHolder (itemView);
    }

    public static class SeriesViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle, vRatings, vAirDate;
        protected Button vImdb, vBbc ;
        protected ImageButton vExpandCollapse;
        protected LinearLayout vExpandableArea;
        protected ListView vEpisodesList;

        public SeriesViewHolder(View v) {
            super(v);


            vTitle =  (TextView) v.findViewById(R.id.dsc_title_text_view);
            vRatings =  (TextView) v.findViewById(R.id.dsc_ratings);
            vAirDate =  (TextView) v.findViewById(R.id.dsc_air_date_text_view);
            vImdb =  (Button) v.findViewById(R.id.dsc_imbdb_button);
            vBbc =  (Button) v.findViewById(R.id.dsc_bbc_button);
            vExpandCollapse =  (ImageButton) v.findViewById(R.id.dsc_expand_close_button);
            vExpandableArea = (LinearLayout) v.findViewById(R.id.dsc_expand_layout);
            vEpisodesList = (ListView) v.findViewById(R.id.dsc_episode_list);

            vExpandCollapse.setTag(this);
        }
    }
}
