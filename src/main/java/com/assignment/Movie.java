package com.assignment;

/**
 * Movie entity representing a film entry in the booking system.
 * Implements Comparable to support AVL Tree ordering by movieId.
 */
public class Movie implements Comparable<Movie> {

    private int movieId;
    private String title;
    private String genre;
    private double rating;
    private String director;
    private int year;
    private int availableSeats;
    private double ticketPrice;

    public Movie(int movieId, String title, String genre, double rating,
                 String director, int year, int availableSeats, double ticketPrice) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.director = director;
        this.year = year;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    // --- Getters ---
    public int getMovieId()         { return movieId; }
    public String getTitle()        { return title; }
    public String getGenre()        { return genre; }
    public double getRating()       { return rating; }
    public String getDirector()     { return director; }
    public int getYear()            { return year; }
    public int getAvailableSeats()  { return availableSeats; }
    public double getTicketPrice()  { return ticketPrice; }

    // --- Setters ---
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public void setTicketPrice(double ticketPrice)    { this.ticketPrice = ticketPrice; }
    public void setRating(double rating)              { this.rating = rating; }

    @Override
    public int compareTo(Movie other) {
        return Integer.compare(this.movieId, other.movieId);
    }

    @Override
    public String toString() {
        return String.format(
            "Movie{id=%d, title='%s', genre='%s', rating=%.1f, director='%s', " +
            "year=%d, seats=%d, price=£%.2f}",
            movieId, title, genre, rating, director, year, availableSeats, ticketPrice
        );
    }
}