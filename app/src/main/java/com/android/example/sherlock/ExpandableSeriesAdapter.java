package com.android.example.sherlock; /**
 * Created by Suyash on 28-12-2016.
 */
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.id;
import static android.R.attr.packageNames;

/**
 * Created by Suyash on 29-12-2016.
 */

public class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Series> seriesList;

    public ExpandableSeriesAdapter(Context mContext, List<Series> seriesList){
        this.mContext = mContext;
        this.seriesList = seriesList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView seriesTitle, ratings, airDate;
        public Button imdbButton, wikipediaButton, expandButton;

        public MyViewHolder(View view) {
            super(view);
            TextView seriesTitle = (TextView) view.findViewById(R.id.dsc_title_text_view);
            TextView ratings = (TextView) view.findViewById(R.id.dsc_ratings);
            TextView airDate = (TextView) view.findViewById(R.id.dsc_air_date_text_view);
            Button imdbButton = (Button) view.findViewById(R.id.dsc_imbdb_button);
            Button wikipediaButton = (Button) view.findViewById(R.id.dsc_wikipedia_button);
            Button expandButton = (Button) view.findViewById(R.id.dsc_expand_close_button);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_series_card, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Series series = seriesList.get(holder.getAdapterPosition());
        //try {
            holder.ratings.setText("\u2605 " + 2.2);
            //holder.seriesTitle.setText("Series " + series.getSeriesNumber());

        //} catch (Exception e){
            //
        //}


        int[] seasonImg = {
                R.drawable.img_ser_1,
                R.drawable.img_ser_2,
                R.drawable.img_ser_3,
                R.drawable.img_ser_4
        };
        //holder.thumbnail.setImageResource(seasonImg[series.getSeriesNumber() - 1]);
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }
}
