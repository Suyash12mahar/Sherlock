package com.android.example.sherlock;

/**
 * Created by Suyash on 28-12-2016.
 */
public class SeasonCard{
    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public SeasonCard(String seasonNumber, String airDate, String ratings) {
        this.seasonNumber = seasonNumber;
        this.airDate = airDate;
        this.ratings = ratings;
    }

    private String seasonNumber;
    private String airDate;
    private String ratings;

}