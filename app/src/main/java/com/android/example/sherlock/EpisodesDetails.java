package com.android.example.sherlock;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EpisodesDetails extends AppCompatActivity {
    int seasonNumber;
    int episodeNumber;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_details_scroll);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aed_toolbar);

        Bundle extras = getIntent().getExtras();
        seasonNumber = Integer.parseInt(extras.getString("season_number"));
        episodeNumber = Integer.parseInt(extras.getString("episode_number"));

        dbHelper = new DatabaseHelper(this);

        CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout)findViewById(R.id.aed_toolbar_layout);
        ImageView episodeImage = (ImageView) findViewById(R.id.episode_image);
        TextView seasonNumberTextView = (TextView) findViewById(R.id.episode_season_number);
        TextView runtimeTextView = (TextView) findViewById(R.id.episode_des_runtime);
        TextView viewsTextView = (TextView) findViewById(R.id.episode_des_views);
        TextView episodeDescriptionTextView = (TextView) findViewById(R.id.episode_des_des);
        TextView basedOnTextView = (TextView) findViewById(R.id.based_on_textView);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            dbHelper.checkAndCopyDatabase();
            dbHelper.openDatabase();
        } catch (SQLException e) {
            Log.d("TAG", "Error accessing database");
        }

        String queryString = String.format("SELECT * FROM \"EpisodeDetails\" WHERE SeasonNumber=\"%d\" AND ReleativeEpisodeNumber=\"%d\"",seasonNumber,episodeNumber);

        try {
            Cursor cursor = dbHelper.QueryData(queryString);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        EpisodeItem item = new EpisodeItem();
                        collapsingToolbarLayout.setTitle(cursor.getString(3));

                        seasonNumberTextView.setText(cursor.getString(1));
                        episodeDescriptionTextView.setText(cursor.getString(4));
                        runtimeTextView.setText("\uD83D\uDD51 " + cursor.getString(5));
                        viewsTextView.setText("Views " + cursor.getString(6) + " mn");
                        basedOnTextView.setText(cursor.getString(8));

                        toolbar.setTitle(cursor.getString(3));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e) {
            Log.d("TAG", "Error accessing database 2");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        String folderPath = "@drawable/";
        String imageName = String.format("img_%d_%d", seasonNumber, episodeNumber);

        int drawableResourceId = -1;

        try{
            drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
            episodeImage.setImageResource(drawableResourceId);
        } catch (Exception e){
            Toast.makeText(this, "Error displaying image." , Toast.LENGTH_LONG).show();
        }

    }
}
