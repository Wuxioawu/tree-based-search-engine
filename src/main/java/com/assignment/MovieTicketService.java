package com.assignment;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieTicketService — the application's core service layer.
 *
 * <p>Three AVL Trees are maintained simultaneously so that the most
 * common query patterns all execute in O(log n):
 *
 * <ul>
 *   <li>{@code idTree}     — keyed on {@code movieId}   (primary key)</li>
 *   <li>{@code ratingTree} — keyed on {@code rating}    (for rating-based queries)</li>
 *   <li>{@code yearTree}   — keyed on {@code year}      (for release-year queries)</li>
 * </ul>
 *
 * <p>Genre and director searches are O(n) linear scans over the in-order
 * traversal, because those attributes are not naturally ordered keys in this
 * implementation.  A future enhancement could maintain additional index trees.
 */
public class MovieTicketService {

    // Primary index — keyed by movieId
    private final AVLTree<Integer, Movie> idTree;

    // Secondary index — keyed by rating (stored as integer x10 to avoid
    // floating-point comparison issues, e.g. 8.5 → 85)
    private final AVLTree<Integer, List<Movie>> ratingTree;

    // Secondary index — keyed by release year
    private final AVLTree<Integer, List<Movie>> yearTree;

    public MovieTicketService() {
        idTree     = new AVLTree<>();
        ratingTree = new AVLTree<>();
        yearTree   = new AVLTree<>();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  CRUD Operations
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Add a new movie to all three index trees.
     *
     * @throws IllegalArgumentException if a movie with the same ID already exists
     */
    public void addMovie(Movie movie) {
        if (idTree.contains(movie.getMovieId())) {
            throw new IllegalArgumentException(
                "Movie with ID " + movie.getMovieId() + " already exists.");
        }
        idTree.insert(movie.getMovieId(), movie);
        indexByRating(movie);
        indexByYear(movie);
    }

    /**
     * Remove a movie from all index trees.
     *
     * @return the removed movie, or {@code null} if not found
     */
    public Movie removeMovie(int movieId) {
        Movie movie = idTree.search(movieId);
        if (movie == null) return null;

        idTree.delete(movieId);
        removeFromRatingIndex(movie);
        removeFromYearIndex(movie);
        return movie;
    }

    /**
     * Update the number of available seats for a movie.
     *
     * @return {@code true} on success, {@code false} if movie not found or
     *         insufficient seats
     */
    public boolean bookTickets(int movieId, int quantity) {
        Movie movie = idTree.search(movieId);
        if (movie == null) return false;
        if (movie.getAvailableSeats() < quantity) return false;
        movie.setAvailableSeats(movie.getAvailableSeats() - quantity);
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Query Operations
    // ─────────────────────────────────────────────────────────────────────────

    /** O(log n) — exact lookup by movie ID */
    public Movie findById(int movieId) {
        return idTree.search(movieId);
    }

    /** O(n) — returns all movies matching the given genre (case-insensitive) */
    public List<Movie> findByGenre(String genre) {
        return idTree.inOrderValues().stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    /** O(n) — returns all movies matching the given director (case-insensitive) */
    public List<Movie> findByDirector(String director) {
        return idTree.inOrderValues().stream()
                .filter(m -> m.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }

    /** O(log n + k) — range search by rating (e.g. 7.0 to 9.0) */
    public List<Movie> findByRatingRange(double minRating, double maxRating) {
        int lo = (int) Math.round(minRating * 10);
        int hi = (int) Math.round(maxRating * 10);
        List<Movie> result = new ArrayList<>();
        ratingTree.rangeSearch(lo, hi).forEach(result::addAll);
        return result;
    }

    /** O(log n + k) — range search by release year */
    public List<Movie> findByYearRange(int startYear, int endYear) {
        List<Movie> result = new ArrayList<>();
        yearTree.rangeSearch(startYear, endYear).forEach(result::addAll);
        return result;
    }

    /** O(n) — returns movies with available seats > 0, sorted by price */
    public List<Movie> getAvailableMovies() {
        return idTree.inOrderValues().stream()
                .filter(m -> m.getAvailableSeats() > 0)
                .sorted(Comparator.comparingDouble(Movie::getTicketPrice))
                .collect(Collectors.toList());
    }

    /** O(n) — returns the top-N movies sorted by rating descending */
    public List<Movie> getTopRated(int n) {
        return idTree.inOrderValues().stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /** O(n) — returns all movies, in-order by ID */
    public List<Movie> getAllMovies() {
        return idTree.inOrderValues();
    }

    /** @return total number of movies in the system */
    public int getTotalMovies() { return idTree.size(); }

    /** @return height of the primary (ID) AVL tree */
    public int getTreeHeight() { return idTree.height(); }

    // ─────────────────────────────────────────────────────────────────────────
    //  Private Index Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private void indexByRating(Movie movie) {
        int key = (int) Math.round(movie.getRating() * 10);
        List<Movie> bucket = ratingTree.search(key);
        if (bucket == null) {
            bucket = new ArrayList<>();
            ratingTree.insert(key, bucket);
        }
        bucket.add(movie);
    }

    private void indexByYear(Movie movie) {
        List<Movie> bucket = yearTree.search(movie.getYear());
        if (bucket == null) {
            bucket = new ArrayList<>();
            yearTree.insert(movie.getYear(), bucket);
        }
        bucket.add(movie);
    }

    private void removeFromRatingIndex(Movie movie) {
        int key = (int) Math.round(movie.getRating() * 10);
        List<Movie> bucket = ratingTree.search(key);
        if (bucket != null) bucket.removeIf(m -> m.getMovieId() == movie.getMovieId());
    }

    private void removeFromYearIndex(Movie movie) {
        List<Movie> bucket = yearTree.search(movie.getYear());
        if (bucket != null) bucket.removeIf(m -> m.getMovieId() == movie.getMovieId());
    }
}