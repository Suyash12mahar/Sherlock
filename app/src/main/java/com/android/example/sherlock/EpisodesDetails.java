package com.android.example.sherlock;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EpisodesDetails extends YouTubeBaseActivity {
    int seasonNumber;
    int episodeNumber;
    DatabaseHelper dbHelper;
    String imdbLink = null;
    String bbcLink = null;
    String wikipediaLink = null;
    String trailorLink = null;
    String castString = null;
    CastAdapter castAdapter;

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
        ImageView episodeImage = (ImageView) findViewById(R.id.episode_image);
        TextView seasonNumberTextView = (TextView) findViewById(R.id.episode_season_number);
        final TextView runtimeTextView = (TextView) findViewById(R.id.episode_des_runtime);
        final TextView viewsTextView = (TextView) findViewById(R.id.episode_des_views);
        final TextView episodeDescriptionTextView = (TextView) findViewById(R.id.episode_des_des);
        TextView basedOnTextView = (TextView) findViewById(R.id.based_on_textView);
        final TextView expandDescription = (TextView) findViewById(R.id.episode_des_expand_des);
        final TextView castViewSeperator = (TextView) findViewById(R.id.cast_view_seperator);
        final Button imdbButton = (Button) findViewById(R.id.imbdb_button);
        final Button bbcButton = (Button) findViewById(R.id.bbc_button);
        final Button wikipediaButton = (Button) findViewById(R.id.wikipedia_button);
        final TextView ratings = (TextView) findViewById(R.id.episode_des_ratings);
        final TextView ratingsScale = (TextView) findViewById(R.id.episode_des_ratings_scale);
        RecyclerView castView = (RecyclerView) findViewById(R.id.episode_des_cast);

        //setSupportActionBar(toolbar);

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

                        ratings.setText(cursor.getString(16));
                        if (ratings.getText().equals("NA")){
                            ratingsScale.setVisibility(View.INVISIBLE);
                        } else {
                            ratingsScale.setText("/" + cursor.getString(17));
                        }

                        castString = cursor.getString(18);

                        bbcLink = cursor.getString(12);
                        imdbLink = cursor.getString(13);
                        wikipediaLink = cursor.getString(14);

                        trailorLink = cursor.getString(15);
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
        String imageName = String.format("img_%d_%d_high_res", seasonNumber, episodeNumber);

        int drawableResourceId = -1;

        try{
            drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
            episodeImage.setImageResource(drawableResourceId);
        } catch (Exception e){
            Toast.makeText(this, "Error displaying image." , Toast.LENGTH_LONG).show();
        }

        if (imdbLink.equals("NA")){
            imdbButton.setEnabled(false);
        }

        if (bbcLink.equals("NA")){
            bbcButton.setEnabled(false);
        }

        if (wikipediaLink.equals("NA")){
            wikipediaButton.setEnabled(false);
        }

        // Sets onClickListener for link buttons
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(imdbLink);
            }
        });
        bbcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(bbcLink);
            }
        });
        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(wikipediaLink);
            }
        });

        // initialize YouTube video player
        final YouTubePlayerView videoPlayer = (YouTubePlayerView) findViewById(R.id.aed_youtube_trailor_view);
        intitializeYouTubePlayer(videoPlayer);

        //Handles expand and collapse of description
        expandDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescriptionLength(episodeDescriptionTextView,expandDescription);
            }
        });
        episodeDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescriptionLength(episodeDescriptionTextView,expandDescription);
            }
        });

        // Fills recycler view with corresponding data
        populateCastView(castView);

        // Hides Recycler View for Cast in-case data is not available
        if (!castString.equals("NA")) {
            castAdapter.notifyDataSetChanged();
        } else{
            castViewSeperator.setVisibility(View.GONE);
            castView.setVisibility(View.GONE);
        }
    }

    public void intitializeYouTubePlayer(YouTubePlayerView videoPlayer){
        videoPlayer.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if (trailorLink != null){
                    if (!trailorLink.equals("NA")){
                        youTubePlayer.loadVideo(trailorLink);
                        youTubePlayer.pause();
                    }
                } else {
                    Toast.makeText(EpisodesDetails.this,"Error: Unable to load video",Toast.LENGTH_LONG).show();
                }

                // Pauses the video after loading
                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        youTubePlayer.pause();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(EpisodesDetails.this,"Error: Unable to initialize player",Toast.LENGTH_LONG).show();
            }
        });

    }

    // Toggles length of episode description
    public void toggleDescriptionLength(TextView deccriptionView, TextView indicatorTextView){
        if (deccriptionView.getMaxLines()==10){
            deccriptionView.setMaxLines(Integer.MAX_VALUE);
            indicatorTextView.setText("show less");
        }
        else {
            deccriptionView.setMaxLines(10);
            indicatorTextView.setText("show more");
        }
    }

    public void openBrowser(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    public void populateCastView(RecyclerView recyclerView){
        if (!castString.equals("NA")){
            List<Cast> list = getCastFromString(castString);

            castAdapter = new CastAdapter(list, this);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(castAdapter);
        }
    }

    public List<Cast> getCastFromString(String constructorString){
        List<Cast> listToReturn = new ArrayList<Cast>();

        String[] splittedString = constructorString.split(";");
        for (String string : splittedString){
            String[] nameArray = string.split(",");
            Cast cast = new Cast();
            cast.setCharacterName(nameArray[0]);
            cast.setRealName(nameArray[1]);

            listToReturn.add(cast);
        }
        return listToReturn;
    }
}
