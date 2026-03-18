package com.assignment;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for AVLTree and MovieTicketService.
 */

public class MovieTicketSystemTest {

    // ─── AVL Tree Tests ───────────────────────────────────────────────────────

    @Test
    public void testInsertAndSearch() {
        AVLTree<Integer, String> tree = new AVLTree<>();
        tree.insert(10, "ten");
        tree.insert(5,  "five");
        tree.insert(15, "fifteen");
        assertEquals("ten",     tree.search(10));
        assertEquals("five",    tree.search(5));
        assertEquals("fifteen", tree.search(15));
        assertNull(tree.search(99));
    }

    @Test
    public void MovieTicketSystemTest() {
        AVLTree<Integer, String> tree = new AVLTree<>();
        for (int i = 1; i <= 10; i++) tree.insert(i, "v" + i);
        tree.delete(5);
        assertNull(tree.search(5));
        assertEquals(9, tree.size());
    }

    @Test
    public void testBalance() {
        // Insert 1–15 in order — an unbalanced BST would have height 15;
        // the AVL tree must keep height ≤ ceil(1.44 * log2(16)) ≈ 6
        AVLTree<Integer, Integer> tree = new AVLTree<>();
        for (int i = 1; i <= 15; i++) tree.insert(i, i);
        assertTrue("AVL height should be ≤ 6 for 15 nodes, got " + tree.height(),
                tree.height() <= 6);
    }

    @Test
    public void testInOrder() {
        AVLTree<Integer, Integer> tree = new AVLTree<>();
        int[] values = {5, 3, 7, 1, 4, 6, 8};
        for (int v : values) tree.insert(v, v);
        List<Integer> sorted = tree.inOrderValues();
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i) < sorted.get(i + 1));
        }
    }

    @Test
    public void testRangeSearch() {
        AVLTree<Integer, Integer> tree = new AVLTree<>();
        for (int i = 1; i <= 20; i++) tree.insert(i, i);
        List<Integer> results = tree.rangeSearch(5, 10);
        assertEquals(6, results.size());
        assertTrue(results.containsAll(List.of(5, 6, 7, 8, 9, 10)));
    }

    // ─── MovieTicketService Tests ─────────────────────────────────────────────

    private MovieTicketService buildService() {
        MovieTicketService svc = new MovieTicketService();
        svc.addMovie(new Movie(1, "Inception",    "Sci-Fi", 8.8, "Nolan",     2010, 100, 12.0));
        svc.addMovie(new Movie(2, "Parasite",     "Drama",  8.6, "Bong",      2019,  50, 10.0));
        svc.addMovie(new Movie(3, "Dark Knight",  "Action", 9.0, "Nolan",     2008,  80, 14.0));
        svc.addMovie(new Movie(4, "Get Out",      "Horror", 7.7, "Peele",     2017,  60, 10.5));
        svc.addMovie(new Movie(5, "Interstellar", "Sci-Fi", 8.6, "Nolan",     2014,  90, 13.0));
        return svc;
    }

    @Test
    public void testFindById() {
        MovieTicketService svc = buildService();
        assertNotNull(svc.findById(1));
        assertNull(svc.findById(999));
    }

    @Test
    public void testFindByGenre() {
        MovieTicketService svc = buildService();
        List<Movie> scifi = svc.findByGenre("Sci-Fi");
        assertEquals(2, scifi.size());
    }

    @Test
    public void testFindByDirector() {
        MovieTicketService svc = buildService();
        List<Movie> nolan = svc.findByDirector("Nolan");
        assertEquals(3, nolan.size());
    }

    @Test
    public void testFindByRatingRange() {
        MovieTicketService svc = buildService();
        List<Movie> high = svc.findByRatingRange(8.5, 9.5);
        assertTrue(high.size() >= 3);
    }

    @Test
    public void testBookTickets() {
        MovieTicketService svc = buildService();
        assertTrue(svc.bookTickets(1, 10));
        assertEquals(90, svc.findById(1).getAvailableSeats());
        assertFalse(svc.bookTickets(1, 200));  // not enough seats
        assertFalse(svc.bookTickets(999, 1));  // non-existent
    }

    @Test
    public void testRemoveMovie() {
        MovieTicketService svc = buildService();
        int before = svc.getTotalMovies();
        svc.removeMovie(2);
        assertEquals(before - 1, svc.getTotalMovies());
        assertNull(svc.findById(2));
    }

    @Test
    public void testDuplicateInsert() {
        MovieTicketService svc = buildService();
        assertThrows(IllegalArgumentException.class,
                () -> svc.addMovie(new Movie(1, "Duplicate", "Drama", 5.0, "X", 2020, 10, 5.0)));
    }
}