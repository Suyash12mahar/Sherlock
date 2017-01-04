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

public class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.ContactViewHolder> {
    public Activity activity;
    private List<Series> list;
    private int expandedPosition = -1;
    private List<Integer> expandedPositions = new ArrayList<>();
    private ArrayList<ContactViewHolder> holderList = new ArrayList<ContactViewHolder>();
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
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int position) {
        final Series ci = list.get(contactViewHolder.getAdapterPosition());
        contactViewHolder.vTitle.setText("Series " + ci.getSeriesNumber());
        contactViewHolder.vRatings.setText(ci.getRatings());
        contactViewHolder.vAirDate.setText(ci.getAirDate());

        //
        if (position == expandedPosition) {
            contactViewHolder.vExpandableArea.setVisibility(View.VISIBLE);
        } else {
            contactViewHolder.vExpandableArea.setVisibility(View.GONE);
        }

        contactViewHolder.vBbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        contactViewHolder.vImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        contactViewHolder.vExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactViewHolder holder = (ContactViewHolder) v.getTag();

                // Adds current holder to holder list if it doesn't exists in same
                if (!holderList.contains(holder)){
                    holderList.add(holder);
                }

                // Collapses every item in holderList
                for (ContactViewHolder holderItem : holderList){
                    if (holderItem.vExpandableArea.isShown() && !holderItem.equals(contactViewHolder)){
                        Effects.dsc_collapse(v.getContext(), holderItem.vExpandableArea);
                        holderItem.vExpandableArea.setVisibility(View.GONE);
                        holderItem.vExpandCollapse.setImageResource(R.drawable.expand_arrow);
                    }
                }

                // Toggles expanded state for current holder
                if (contactViewHolder.vExpandableArea.isShown()){
                    Effects.dsc_collapse(v.getContext(), contactViewHolder.vExpandableArea);
                    contactViewHolder.vExpandableArea.setVisibility(View.GONE);
                    contactViewHolder.vExpandCollapse.setImageResource(R.drawable.expand_arrow);
                } else {
                    Effects.dsc_expand(v.getContext(), contactViewHolder.vExpandableArea);
                    activeSeries =Integer.parseInt(contactViewHolder.vTitle.getText().charAt(contactViewHolder.vTitle.getText().length()-1) + "");
                    contactViewHolder.vExpandableArea.setVisibility(View.VISIBLE);
                    contactViewHolder.vExpandCollapse.setImageResource(R.drawable.collapse_arrow);
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
                        resourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
                        if (resourceId != 0) {
                            item.setThumbnailID(activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName()));
                        } else {
                            item.setThumbnailID(R.drawable.thumbnail_extra_wide_png);
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
        contactViewHolder.vEpisodesList.setAdapter(adapter);
        contactViewHolder.vEpisodesList.setOnItemClickListener(episodeOnItemClickListener);
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
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.detailed_series_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle, vRatings, vAirDate;
        protected Button vImdb, vBbc ;
        protected ImageButton vExpandCollapse;
        protected LinearLayout vExpandableArea;
        protected ListView vEpisodesList;

        public ContactViewHolder(View v) {
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
