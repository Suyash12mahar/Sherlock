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

public class ExpandableSeriesAdapter extends RecyclerView.Adapter<ExpandableSeriesAdapter.ContactViewHolder> {

    private List<Series> contactList;

    public ExpandableSeriesAdapter(List<Series> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Series ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.getSeriesNumber() + "");
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.detailed_series_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.dsc_title_text_view);
        }
    }
}
