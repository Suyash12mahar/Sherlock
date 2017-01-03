package com.android.example.sherlock;

/**
 * Created by Suyash on 29-12-2016.
 */

public class dscEpisode {
    private int seriesNumber;
    private int episodeNumber;
    private int thumbnailID;
    private String title;
    private String imdbLink;
    private String bbcLink;

    public dscEpisode(){

    }
    public dscEpisode(int seriesNumber, int episodeNumber, int thumbnailID, String title) {
        this.seriesNumber = seriesNumber;
        this.episodeNumber = episodeNumber;
        this.thumbnailID = thumbnailID;
        this.title = title;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public String getBbcLink() {
        return bbcLink;
    }

    public void setBbcLink(String bbcLink) {
        this.bbcLink = bbcLink;
    }

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(int thumbnailID) {
        this.thumbnailID = thumbnailID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
