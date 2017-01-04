package com.android.example.sherlock;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
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
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ContactViewHolder> {
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
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int position) {
        contactViewHolder.vCharacterName.setText(list.get(position).getCharacterName());
        contactViewHolder.vRealName.setText(list.get(position).getRealName());

        // Sets image resource for episode image (series 1, episode3 : img_1_2.png)
        String imageName = String.format("img_cast_%s", list.get(position).getRealName().replaceAll(" ", "_").toLowerCase());

        int resourceId = -1;
        resourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());

        if (resourceId != 0) {
            contactViewHolder.thumbnailImage.setImageResource(activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName()));
        } else {
            contactViewHolder.thumbnailImage.setImageResource(R.drawable.user_default);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cast_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCharacterName, vRealName;
        protected ImageView thumbnailImage;

        public ContactViewHolder(View v) {
            super(v);
            vCharacterName =  (TextView) v.findViewById(R.id.cc_character_name);
            vRealName =  (TextView) v.findViewById(R.id.cc_real_name);
            thumbnailImage = (ImageView) v.findViewById(R.id.cc_image);
        }
    }
}
