package com.android.example.sherlock;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suyash on 03-01-2017.
 */
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    public Activity activity;
    private List<Cast> list;

    public CastAdapter(List<Cast> list, Activity activity) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final CastViewHolder castViewHolder, int position) {
        castViewHolder.vCharacterName.setText(list.get(position).getCharacterName());
        castViewHolder.vRealName.setText(list.get(position).getRealName());

        // Sets image resource for episode image (series 1, episode3 : img_1_3.png)
        String imageName = String.format(
                "img_cast_%s", list.get(position)
                            .getRealName()
                            .replaceAll(" ", "_")
                            .toLowerCase());

        int resourceId = -1;
        resourceId = activity.getResources()
                        .getIdentifier(imageName, "drawable", activity.getPackageName());

        if (resourceId != 0) {
            castViewHolder.thumbnailImage.setImageResource(
                    activity.getResources()
                        .getIdentifier(imageName, "drawable", activity.getPackageName()));
        } else {
            castViewHolder.thumbnailImage.setImageResource(R.drawable.user_default);
        }
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cast_card, viewGroup, false);

        return new CastViewHolder(itemView);
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCharacterName, vRealName;
        protected ImageView thumbnailImage;

        public CastViewHolder(View v) {
            super(v);

            // Adds listener to open google search for current cast memeber
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(createSearchString(
                                    String.valueOf(vRealName.getText())
                            )));
                    v.getContext().startActivity(browserIntent);
                }
            });

            vCharacterName =  (TextView) v.findViewById(R.id.cc_character_name);
            vRealName =  (TextView) v.findViewById(R.id.cc_real_name);
            thumbnailImage = (ImageView) v.findViewById(R.id.cc_image);
        }
    }

    public static String createSearchString(String termToSearch){
        String searchString = "https://www.google.co.in/search?q=";
        searchString += termToSearch.replace(" ","+");
        return searchString;
    }

}
