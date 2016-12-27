package com.android.example.sherlock;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EpisodeDetails extends AppCompatActivity {
    private int seasonNumber;
    private int episodeNumber;
    private String  linkToImage;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);

        Bundle extras = getIntent().getExtras();

        ImageView episodeImage = (ImageView) findViewById(R.id.episode_des_img);
        TextView seasonNumberTextView = (TextView) findViewById(R.id.episode_season_number);
        TextView episodeTitleTextView = (TextView) findViewById(R.id.episode_des_title);
        TextView runtimeTextView = (TextView) findViewById(R.id.episode_des_runtime);
        TextView viewsTextView = (TextView) findViewById(R.id.episode_des_views);
        TextView episodeDescriptionTextView = (TextView) findViewById(R.id.episode_des_des);
        TextView basedOnTextView = (TextView) findViewById(R.id.based_on_textView);
        //TextView airDateTextView = (TextView) findViewById(R.id.);

        seasonNumber = Integer.parseInt(extras.getString("season_number"));
        episodeNumber = Integer.parseInt(extras.getString("episode_number"));

        dbHelper = new DatabaseHelper(this);
        // id 0-> SeasonNumber 1-> ReleativeEps 2-> EpisodeName 3-> EpisodeDescription 4-> RuntTIm 5-> Views 6-> LinkToImage
        //7 -> BasedON  8->  US 9-> UK
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
                        seasonNumberTextView.setText("Season " + cursor.getString(1));
                        episodeTitleTextView.setText(cursor.getInt(2) + ". " + cursor.getString(3));
                        episodeDescriptionTextView.setText(cursor.getString(4));
                        runtimeTextView.setText("\uD83D\uDD51 " + cursor.getString(5));
                        viewsTextView.setText("\uD83D\uDC41 " + cursor.getString(6) + " mn");
                        linkToImage = cursor.getString(7);
                        basedOnTextView.setText(cursor.getString(8));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException e) {
            Log.d("TAG", "Error accessing database 2");
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            episodeDescriptionTextView.setText(e.toString());
        }

        // Displays image corresponding to current episode
        Toast.makeText(this, "season Number : " + seasonNumber + "\nEpisode Number : " + episodeNumber, Toast.LENGTH_LONG).show();

        String folderPath = "@drawable/";
        String imageName = String.format("img_%d_%d", seasonNumber, episodeNumber);

        int drawableResourceId = -1;
        try{
            drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
            episodeImage.setImageResource(drawableResourceId);
        } catch (Exception e){
            episodeDescriptionTextView.setText(drawableResourceId + "\n" +imageName + "\n" + e.getStackTrace() + "\n" +  e.toString() + "\n" +  e.getMessage());
            Toast.makeText(this, "Error displaying image." , Toast.LENGTH_LONG).show();
        }
    }
}
