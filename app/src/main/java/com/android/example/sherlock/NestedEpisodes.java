package com.android.example.sherlock;

/**
 * Created by Suyash on 29-12-2016.
 */

public class NestedEpisodes {
    private int series;
    private int episodeNumber;
    private String episodeTitle;

    public NestedEpisodes(String episodeTitle, int episodeNumber, int series) {
        this.episodeTitle = episodeTitle;
        this.episodeNumber = episodeNumber;
        this.series = series;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }
}
