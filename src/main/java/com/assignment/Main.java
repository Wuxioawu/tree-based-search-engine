package com.assignment;


import java.util.List;
import java.util.Scanner;

/**
 * Main — interactive console UI for the Movie Ticket Query System.
 *
 * <p>Demonstrates all service operations against a pre-loaded dataset,
 * then offers a live interactive menu.
 */
public class Main {

    private static final MovieTicketService service = new MovieTicketService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();
        seedData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            running = handleChoice(choice);
        }
        System.out.println("\n  Goodbye! Thank you for using Movie Ticket System.");
        scanner.close();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Menu
    // ─────────────────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  🎬  MOVIE TICKET QUERY SYSTEM  (AVL Tree)");
        System.out.println("═".repeat(60));
    }

    private static void printMenu() {
        System.out.println("\n┌─ MENU " + "─".repeat(52) + "┐");
        System.out.println("│  1. View all movies                                   │");
        System.out.println("│  2. Search by ID                                      │");
        System.out.println("│  3. Search by genre                                   │");
        System.out.println("│  4. Search by director                                │");
        System.out.println("│  5. Search by rating range                            │");
        System.out.println("│  6. Search by release year range                      │");
        System.out.println("│  7. Show available movies (sorted by price)           │");
        System.out.println("│  8. Top-5 rated movies                                │");
        System.out.println("│  9. Book tickets                                      │");
        System.out.println("│ 10. Add new movie                                     │");
        System.out.println("│ 11. Remove movie                                      │");
        System.out.println("│ 12. Tree statistics                                   │");
        System.out.println("│ 13. Run performance experiments                       │");
        System.out.println("│  0. Exit                                              │");
        System.out.println("└" + "─".repeat(59) + "┘");
    }

    private static boolean handleChoice(int choice) {
        switch (choice) {
            case 1  -> listAll();
            case 2  -> searchById();
            case 3  -> searchByGenre();
            case 4  -> searchByDirector();
            case 5  -> searchByRating();
            case 6  -> searchByYear();
            case 7  -> availableMovies();
            case 8  -> topRated();
            case 9  -> bookTickets();
            case 10 -> addMovie();
            case 11 -> removeMovie();
            case 12 -> treeStats();
            case 13 -> runExperiments();
            case 0  -> { return false; }
            default -> System.out.println("  ⚠  Invalid option. Please try again.");
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Handlers
    // ─────────────────────────────────────────────────────────────────────────

    private static void listAll() {
        List<Movie> movies = service.getAllMovies();
        System.out.println("\n  All Movies (" + movies.size() + " total):");
        movies.forEach(m -> System.out.println("  " + m));
    }

    private static void searchById() {
        int id = readInt("  Enter movie ID: ");
        Movie m = service.findById(id);
        if (m != null) System.out.println("  Found: " + m);
        else            System.out.println("  ✗ No movie found with ID " + id);
    }

    private static void searchByGenre() {
        String genre = readString("  Enter genre: ");
        List<Movie> results = service.findByGenre(genre);
        printResults("Genre: " + genre, results);
    }

    private static void searchByDirector() {
        String dir = readString("  Enter director name: ");
        List<Movie> results = service.findByDirector(dir);
        printResults("Director: " + dir, results);
    }

    private static void searchByRating() {
        double lo = readDouble("  Min rating (0-10): ");
        double hi = readDouble("  Max rating (0-10): ");
        List<Movie> results = service.findByRatingRange(lo, hi);
        printResults("Rating " + lo + "–" + hi, results);
    }

    private static void searchByYear() {
        int start = readInt("  Start year: ");
        int end   = readInt("  End year:   ");
        List<Movie> results = service.findByYearRange(start, end);
        printResults("Year " + start + "–" + end, results);
    }

    private static void availableMovies() {
        List<Movie> results = service.getAvailableMovies();
        printResults("Available movies (by price)", results);
    }

    private static void topRated() {
        List<Movie> results = service.getTopRated(5);
        printResults("Top-5 rated", results);
    }

    private static void bookTickets() {
        int id  = readInt("  Movie ID: ");
        int qty = readInt("  Number of tickets: ");
        if (service.bookTickets(id, qty)) {
            System.out.println("  ✓ Booking confirmed! " + qty + " ticket(s) reserved.");
        } else {
            System.out.println("  ✗ Booking failed. Movie not found or insufficient seats.");
        }
    }

    private static void addMovie() {
        System.out.println("  --- Add New Movie ---");
        int    id       = readInt("  ID:            ");
        String title    = readString("  Title:         ");
        String genre    = readString("  Genre:         ");
        double rating   = readDouble("  Rating (0-10): ");
        String director = readString("  Director:      ");
        int    year     = readInt("  Year:          ");
        int    seats    = readInt("  Seats:         ");
        double price    = readDouble("  Price (£):     ");
        try {
            service.addMovie(new Movie(id, title, genre, rating, director, year, seats, price));
            System.out.println("  ✓ Movie added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✗ " + e.getMessage());
        }
    }

    private static void removeMovie() {
        int id = readInt("  Enter movie ID to remove: ");
        Movie removed = service.removeMovie(id);
        if (removed != null) System.out.println("  ✓ Removed: " + removed);
        else                  System.out.println("  ✗ Movie with ID " + id + " not found.");
    }

    private static void treeStats() {
        System.out.println("\n  ── Tree Statistics ──────────────────────────────");
        System.out.println("  Total movies  : " + service.getTotalMovies());
        System.out.println("  Tree height   : " + service.getTreeHeight());
        int n = service.getTotalMovies();
        double theoretical = n > 0 ? 1.44 * (Math.log(n + 2) / Math.log(2)) : 0;
        System.out.printf("  Theoretical h : %.2f  (≈ 1.44 × log₂(n))%n", theoretical);
        System.out.println("  ─────────────────────────────────────────────────");
    }

    private static void runExperiments() {
        System.out.println();
        new PerformanceExperiment().runAll();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Seed Data
    // ─────────────────────────────────────────────────────────────────────────

    private static void seedData() {
        Movie[] movies = {
            new Movie(101, "Inception",              "Sci-Fi",  8.8, "Christopher Nolan",   2010, 120, 12.50),
            new Movie(102, "The Dark Knight",        "Action",  9.0, "Christopher Nolan",   2008, 85,  14.00),
            new Movie(103, "Parasite",               "Drama",   8.6, "Bong Joon-ho",        2019, 60,  10.00),
            new Movie(104, "The Godfather",          "Drama",   9.2, "Francis Ford Coppola",1972, 40,   9.50),
            new Movie(105, "Interstellar",           "Sci-Fi",  8.6, "Christopher Nolan",   2014, 100, 13.00),
            new Movie(106, "Spirited Away",          "Animation",8.6,"Hayao Miyazaki",      2001, 75,  11.00),
            new Movie(107, "Pulp Fiction",           "Drama",   8.9, "Quentin Tarantino",   1994, 50,   9.00),
            new Movie(108, "The Shawshank Redemption","Drama",  9.3, "Frank Darabont",      1994, 30,   8.50),
            new Movie(109, "Avengers: Endgame",      "Action",  8.4, "Russo Brothers",      2019, 200, 15.50),
            new Movie(110, "Get Out",                "Horror",  7.7, "Jordan Peele",        2017, 90,  10.50),
            new Movie(111, "La La Land",             "Romance", 8.0, "Damien Chazelle",     2016, 110, 11.50),
            new Movie(112, "Schindler's List",       "Drama",   9.0, "Steven Spielberg",    1993, 20,   8.00),
            new Movie(113, "Mad Max: Fury Road",     "Action",  8.1, "George Miller",       2015, 130, 12.00),
            new Movie(114, "The Matrix",             "Sci-Fi",  8.7, "Lana Wachowski",      1999, 95,  10.00),
            new Movie(115, "Coco",                   "Animation",8.4,"Lee Unkrich",         2017, 180, 13.50),
        };

        for (Movie m : movies) service.addMovie(m);
        System.out.println("  ✓ " + movies.length + " movies loaded into AVL tree.\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  I/O Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private static void printResults(String label, List<Movie> results) {
        System.out.println("\n  Results for [" + label + "] — " + results.size() + " found:");
        if (results.isEmpty()) System.out.println("  (none)");
        else results.forEach(m -> System.out.println("  → " + m));
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  ⚠ Please enter a valid integer."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  ⚠ Please enter a valid number."); }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}