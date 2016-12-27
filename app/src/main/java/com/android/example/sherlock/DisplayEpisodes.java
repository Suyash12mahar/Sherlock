package com.android.example.sherlock;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayEpisodes extends AppCompatActivity {
    private int seasonNumber;

    ListView listView;
    ArrayList<EpisodeItem> arrayList = new ArrayList<EpisodeItem>();
    EpisodeAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_display_episodes);


        listView = (ListView) findViewById(R.id.episodesList);
        Bundle extras = getIntent().getExtras();

        seasonNumber = Integer.parseInt(extras.getString("season_number"));
        dbHelper = new DatabaseHelper(this);

        TextView titleText = (TextView) findViewById(R.id.textView3);
        titleText.setText("Season " + seasonNumber);

        try {
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        } catch (SQLException e) {
            Log.d("TAG", "Error accessing database");
        }

        // TODO : Remove this TOast
        Toast.makeText(this, "THis is idfnkjasndbfja", Toast.LENGTH_SHORT).show();
        String queryString = String.format(
                "SELECT * FROM \"EpisodeDetails\" WHERE SeasonNumber=\"%d\"",seasonNumber);

        try {
            Cursor cursor = dbHelper.QueryData(queryString);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EpisodeItem item = new EpisodeItem();
                        item.setEpTitle(cursor.getInt(2) + ". " + cursor.getString(3));
                        item.setAirDate(cursor.getString(10));
                        item.setShortDescription(cursor.getString(11));
                        item.setViews("\uD83D\uDD51 " + cursor.getString(6));
                        arrayList.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e) {
            Log.d("TAG", "Error accessing database 2");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        adapter = new EpisodeAdapter(this, R.layout.activity_episode_item, arrayList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(onItemClickListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // This comment is of no use
        Intent newerIntent = new Intent(DisplayEpisodes.this, EpisodeDetails.class);
        newerIntent.putExtra("season_number", seasonNumber);
        newerIntent.putExtra("episode_number", String.valueOf(id + 1));
        startActivity(newerIntent);
        }
    };
}
