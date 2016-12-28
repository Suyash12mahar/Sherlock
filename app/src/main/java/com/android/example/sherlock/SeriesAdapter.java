package com.android.example.sherlock; /**
 * Created by Suyash on 28-12-2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.id;
import static android.R.attr.packageNames;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {
    private  final OnItemClickListener listener;
    private Context mContext;
    private List<SeasonCard> seriesList;

    public SeriesAdapter (Context mContext, List<SeasonCard> seriesList, OnItemClickListener listener){
        this.mContext = mContext;
        this.seriesList = seriesList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, ratings;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.sc_series);
            ratings = (TextView) view.findViewById(R.id.sc_ratings);
            thumbnail = (ImageView) view.findViewById(R.id.sc_series_image);
        }

        public void bind(final SeasonCard item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v){
                    listener.OnItemClick(item);
                }
            });
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SeasonCard series = seriesList.get(position);
        holder.title.setText("Series " + series.getSeasonNumber().replaceAll("Season ",""));
        holder.ratings.setText("\u2605 " + series.getRatings());

        int[] seasonImg = {
                R.drawable.img_ser_1,
                R.drawable.img_ser_2,
                R.drawable.img_ser_3,
                R.drawable.img_ser_4
        };
        int seasonNumber = Integer.parseInt(series.getSeasonNumber().charAt(series.getSeasonNumber().length()-1) + "");

        holder.thumbnail.setImageResource(seasonImg[seasonNumber - 1]);
        holder.bind(seriesList.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return seriesList.size();
    }
}
