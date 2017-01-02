package com.android.example.sherlock;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EpisodesDetails extends AppCompatActivity {
    int seasonNumber;
    int episodeNumber;
    DatabaseHelper dbHelper;
    final String imdbLink = null;
    final String bbcLink = null;
    final String wikipediaLink = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_details_scroll);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.aed_toolbar);

        Bundle extras = getIntent().getExtras();
        seasonNumber = Integer.parseInt(extras.getString("season_number"));
        episodeNumber = Integer.parseInt(extras.getString("episode_number"));

        dbHelper = new DatabaseHelper(this);

        final CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout)findViewById(R.id.aed_toolbar_layout);
        final ImageView episodeImage = (ImageView) findViewById(R.id.episode_image);
        final TextView seasonNumberTextView = (TextView) findViewById(R.id.episode_season_number);
        final TextView runtimeTextView = (TextView) findViewById(R.id.episode_des_runtime);
        final TextView viewsTextView = (TextView) findViewById(R.id.episode_des_views);
        final TextView episodeDescriptionTextView = (TextView) findViewById(R.id.episode_des_des);
        final TextView basedOnTextView = (TextView) findViewById(R.id.based_on_textView);
        final Button imdbButton = (Button) findViewById(R.id.imbdb_button);
        final Button bbcButton = (Button) findViewById(R.id.bbc_button);
        final Button wikipediaButton = (Button) findViewById(R.id.wikipedia_button);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String textToSend = collapsingToolbarLayout.getTitle() + "\n" +
                        "\n" +
                        "Series " + seasonNumber + " | " + " Episode " + episodeNumber + " of " + 3 + "\n" +
                        "\n" +
                        runtimeTextView.getText() + " | " + viewsTextView.getText() + "\n" +
                        "\n" +
                        "Description: " + "\n" +
                        episodeDescriptionTextView.getText() + "\n" +
                        "\n" +
                        "[IMDb link](" + imdbLink + ")" + "\n" +
                        "[BBC link](" + bbcLink + ")" + "\n" +
                        "[Wikipedia link](" + wikipediaLink + ")" + "\n" +
                        "\n" +
                        "Sent using Sherlock App";
                        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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
