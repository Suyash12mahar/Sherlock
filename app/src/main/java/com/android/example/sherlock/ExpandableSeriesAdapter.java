package com.android.example.sherlock; /**
 * Created by Suyash on 28-12-2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suyash on 29-12-2016.
 */

class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.SeriesViewHolder > {
    public Activity activity;
    private List<Series> list;
    private int expandedPosition = -1;

    ImageView bookmarkImageView;

    private String bbcLink;
    private String imdbLink;

    private ArrayList<SeriesViewHolder> holderList = new ArrayList<SeriesViewHolder>();
    int activeSeries = 1;

    public dscEpisodeAdapter adapter;

    ExpandableSeriesAdapter(List<Series> list, Activity activity) {
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
                        int resourceId;
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
            Log.d("DatabaseError", "ExpandableSeriesAdapter 179: Error accessing database");
            Toast.makeText(activity, "Error accessing database", Toast.LENGTH_SHORT).show();
        }
        adapter = new dscEpisodeAdapter(activity, R.layout.dsc_episode_item, episodeList);
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
            TextView ij = (TextView)parent.findViewById(R.id.ei_episode_title);

            bookmarkImageView = (ImageView) parent.findViewById(R.id.ei_bookmark_icon);
            Log.e("This",ij.getText().toString());
            Intent newerIntent = new Intent(activity, EpisodesDetails.class);
            newerIntent.putExtra("season_number", String.valueOf(activeSeries));
            newerIntent.putExtra("episode_number", String.valueOf(id+1));
            activity.startActivityForResult(newerIntent, 1);
        }
    };


    @Override
    public SeriesViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.detailed_series_card, viewGroup, false);

        return new SeriesViewHolder (itemView);
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle, vRatings, vAirDate;
        Button vImdb, vBbc ;
        ImageButton vExpandCollapse;
        LinearLayout vExpandableArea;
        ListView vEpisodesList;

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
