package com.booking;

public class Movie {

    private String movieId;
    private String title;
    private String genre;
    private int durationMins;

    public Movie(String movieId, String title, String genre, int durationMins) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMins = durationMins;
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDurationMins() { return durationMins; }

    @Override
    public String toString() {
        return title + " [" + genre + ", " + durationMins + "min]";
    }
}
