package com.android.example.sherlock;

/**
 * Created by Suyash on 26-11-2016.
 */

public class SeasonItem {
    private String seasonNumber;
    private String year;
    private String ratings;

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        if (ratings.equals("0")) this.ratings = "NA";
        else this.ratings = ratings;
    }
}
