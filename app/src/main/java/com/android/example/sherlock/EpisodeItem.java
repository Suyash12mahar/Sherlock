package com.android.example.sherlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EpisodeItem extends AppCompatActivity {
    private String epTitle;
    private String airDate;
    private String views;
    private String url;
    private String shortDescription;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    private int imageId;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViews() { return views; }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpTitle() {
        return epTitle;
    }

    public void setEpTitle(String epTitle) {
        this.epTitle = epTitle;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_item);
    }
}
