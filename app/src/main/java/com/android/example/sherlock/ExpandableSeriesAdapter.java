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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.id;
import static android.R.attr.packageNames;

/**
 * Created by Suyash on 29-12-2016.
 */

public class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.ContactViewHolder> {
    public Activity activity;
    private List<Series> list;
    private int expandedPosition = -1;

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

        if (position == expandedPosition) {
            contactViewHolder.vExpandableArea.setVisibility(View.VISIBLE);
        } else {
            contactViewHolder.vExpandableArea.setVisibility(View.GONE);
        }

        contactViewHolder.vBbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        contactViewHolder.vImdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        contactViewHolder.vExpandCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactViewHolder holder = (ContactViewHolder) v.getTag();

                // Check for an expanded view, collapse if you find one
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                // Set the current position to "expanded"
                expandedPosition = holder.getAdapterPosition();
                notifyItemChanged(expandedPosition);

                ListView episodesList = (ListView) holder.vEpisodeslist;

                int seasonNumber = holder.getAdapterPosition() + 1;

                DatabaseHelper dbHelper = new DatabaseHelper(activity);

                try{
                    dbHelper.checkAndCopyDatabase();
                    dbHelper.openDatabase();
                }
                catch(SQLException e){
                    Toast.makeText(activity, "Error reading database", Toast.LENGTH_LONG);
                    Log.d("TAG", "Error accessing database");//MinGW
                }
                String queryString = String.format(
                        "SELECT * FROM \"EpisodeDetails\" WHERE SeasonNumber=\"%d\"",seasonNumber);

                /*
                try{
                    Cursor cursor = dbHelper.QueryData(queryString);
                    if (cursor != null){
                        if (cursor.moveToFirst()){
                            do {
                                // TODO Update to include new adapters
                                EpisodeItem item = new EpisodeItem();
                                item.setEpTitle(cursor.getInt(2)  + ". " + cursor.getString(3));
                                item.setAirDate(cursor.getString(10));
                                item.setShortDescription(cursor.getString(11));

                                String imageName = String.format("img_%d_%d", seasonNumber, Integer.parseInt(item.getEpTitle().charAt((0)) + "") );
                                int resourceId = -1;
                                resourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
                                if (resourceId != 0) {
                                    item.setImageId(this.getResources().getIdentifier(imageName, "drawable", this.getPackageName()));
                                } else {
                                    item.setImageId(R.drawable.thumbnail_extra_wide_png);
                                }

                                item.setViews(cursor.getString(5));
                                arrayList.add(item);
                            }while (cursor.moveToNext());

                        }
                    }
                } catch (SQLiteException e){
                    Log.d("TAG", "Error accessing database 2");
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
                */
                String a = "This is a new String";
            }
        });

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.detailed_series_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle, vRatings, vAirDate;
        protected Button vImdb, vBbc, vExpandCollapse;
        protected LinearLayout vExpandableArea;
        protected ListView vEpisodeslist;

        public ContactViewHolder(View v) {
            super(v);
            vTitle =  (TextView) v.findViewById(R.id.dsc_title_text_view);
            vRatings =  (TextView) v.findViewById(R.id.dsc_ratings);
            vAirDate =  (TextView) v.findViewById(R.id.dsc_air_date_text_view);
            vImdb =  (Button) v.findViewById(R.id.dsc_imbdb_button);
            vBbc =  (Button) v.findViewById(R.id.dsc_bbc_button);
            vExpandCollapse =  (Button) v.findViewById(R.id.dsc_expand_close_button);
            vExpandableArea = (LinearLayout) v.findViewById(R.id.dsc_expand_layout);
            vEpisodeslist = (ListView) v.findViewById(R.id.dsc_episode_list);

            vExpandCollapse.setTag(this);
        }
    }
}
