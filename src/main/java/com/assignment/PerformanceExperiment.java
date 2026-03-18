package com.assignment;


import java.util.*;

/**
 * PerformanceExperiment
 *
 * <p>Measures wall-clock time (nanoseconds) for the three core AVL operations:
 * <ul>
 *   <li>Insert — populate the tree with N movies</li>
 *   <li>Search — 1 000 random ID lookups</li>
 *   <li>Delete — remove 10% of elements</li>
 * </ul>
 *
 * <p>Each experiment is repeated {@code RUNS} times and the mean is reported,
 * removing warm-up noise via JVM JIT compilation.
 */
public class PerformanceExperiment {

    private static final int   RUNS         = 5;
    private static final int[] DATASET_SIZES = {100, 500, 1_000, 5_000, 10_000, 50_000};
    private static final int   SEARCH_QUERIES = 1_000;

    private final Random rng = new Random(42);

    // ─────────────────────────────────────────────────────────────────────────
    //  Entry Point
    // ─────────────────────────────────────────────────────────────────────────

    public void runAll() {
        System.out.println("=".repeat(80));
        System.out.println("  Movie Ticket System — AVL Tree Performance Experiments");
        System.out.println("=".repeat(80));
        System.out.printf("%-12s  %-18s  %-18s  %-18s  %-8s%n",
                "N", "Insert (ms)", "Search (ms)", "Delete (ms)", "Height");
        System.out.println("-".repeat(80));

        for (int n : DATASET_SIZES) {
            ExperimentResult result = runExperiment(n);
            System.out.printf("%-12d  %-18.4f  %-18.4f  %-18.4f  %-8d%n",
                    n,
                    result.avgInsertMs,
                    result.avgSearchMs,
                    result.avgDeleteMs,
                    result.treeHeight);
        }

        System.out.println("=".repeat(80));
        System.out.println("\nNotes:");
        System.out.println("  • Each data point is the mean over " + RUNS + " runs.");
        System.out.println("  • Search measures " + SEARCH_QUERIES + " random lookups per run.");
        System.out.println("  • Delete removes 10% of elements per run.");
        System.out.println("  • Theoretical complexity: O(log n) for all three operations.");
        System.out.println("  • Expected height ≈ 1.44 * log₂(n) for AVL trees.\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Core Experiment
    // ─────────────────────────────────────────────────────────────────────────

    private ExperimentResult runExperiment(int n) {
        double totalInsert = 0, totalSearch = 0, totalDelete = 0;
        int lastHeight = 0;

        for (int run = 0; run < RUNS; run++) {
            List<Movie> dataset = generateDataset(n);
            MovieTicketService service = new MovieTicketService();

            // --- Insert ---
            long t0 = System.nanoTime();
            for (Movie m : dataset) service.addMovie(m);
            long t1 = System.nanoTime();
            totalInsert += (t1 - t0) / 1_000_000.0;

            lastHeight = service.getTreeHeight();

            // --- Search ---
            List<Integer> ids = new ArrayList<>();
            for (Movie m : dataset) ids.add(m.getMovieId());
            long t2 = System.nanoTime();
            for (int i = 0; i < SEARCH_QUERIES; i++) {
                int id = ids.get(rng.nextInt(ids.size()));
                service.findById(id);
            }
            long t3 = System.nanoTime();
            totalSearch += (t3 - t2) / 1_000_000.0;

            // --- Delete (10 % of elements) ---
            int deleteCount = Math.max(1, n / 10);
            long t4 = System.nanoTime();
            for (int i = 0; i < deleteCount; i++) {
                int id = ids.get(rng.nextInt(ids.size()));
                service.removeMovie(id);
            }
            long t5 = System.nanoTime();
            totalDelete += (t5 - t4) / 1_000_000.0;
        }

        return new ExperimentResult(
                totalInsert / RUNS,
                totalSearch / RUNS,
                totalDelete / RUNS,
                lastHeight
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Dataset Generator
    // ─────────────────────────────────────────────────────────────────────────

    private List<Movie> generateDataset(int n) {
        String[] genres    = {"Action", "Drama", "Comedy", "Sci-Fi", "Horror", "Romance"};
        String[] directors = {"Nolan", "Spielberg", "Scorsese", "Tarantino", "Kubrick"};

        Set<Integer> used = new HashSet<>();
        List<Movie>  list = new ArrayList<>(n);

        while (list.size() < n) {
            int id = rng.nextInt(n * 10) + 1;
            if (used.contains(id)) continue;
            used.add(id);

            list.add(new Movie(
                    id,
                    "Movie_" + id,
                    genres[rng.nextInt(genres.length)],
                    4.0 + rng.nextDouble() * 6.0,            // 4.0 – 10.0
                    directors[rng.nextInt(directors.length)],
                    1990 + rng.nextInt(36),                   // 1990 – 2025
                    rng.nextInt(200) + 1,                     // 1 – 200 seats
                    5.0 + rng.nextDouble() * 20.0             // £5 – £25
            ));
        }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Result Record
    // ─────────────────────────────────────────────────────────────────────────

    private static class ExperimentResult {
        final double avgInsertMs;
        final double avgSearchMs;
        final double avgDeleteMs;
        final int    treeHeight;

        ExperimentResult(double ins, double srch, double del, int h) {
            avgInsertMs = ins;
            avgSearchMs = srch;
            avgDeleteMs = del;
            treeHeight  = h;
        }
    }
}