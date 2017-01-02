package com.android.example.sherlock;

/**
 * Created by Suyash on 29-12-2016.
>Detailed Series Card
 >>Series number
 >>Ratings
 >>AirDate
 >>IMDb link
 >>BBC Link

 */

public class Series {
    private int seriesNumber;
    private String ratings;
    private String airDate;
    private String imdbLink = "https://www.google.com";
    private String bbcLink = "https://www.google.com";

    public Series(int seriesNumber, String ratings, String airDate, String imdbLink, String bbcLink) {
        this.seriesNumber = seriesNumber;
        this.ratings = ratings;
        this.airDate = airDate;
        this.imdbLink = imdbLink;
        this.bbcLink = bbcLink;
    }

    public String getBbcLink() {
        return bbcLink;
    }

    public void setBbcLink(String bbcLink) {
        this.bbcLink = bbcLink;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
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

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

}
