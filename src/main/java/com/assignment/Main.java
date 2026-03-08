package com.assignment;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        AVLTree tree = new AVLTree();

        tree.insertMovie(new Movie("Inception", 2010, 8.8));
        tree.insertMovie(new Movie("Avatar", 2009, 7.9));
        tree.insertMovie(new Movie("Titanic", 1997, 7.8));
        tree.insertMovie(new Movie("Interstellar", 2014, 8.6));
        tree.insertMovie(new Movie("Gladiator", 2000, 8.5));

        System.out.println("Movies in sorted order:");
        tree.printMovies();

        System.out.println("\nSearching for movie:");
        tree.searchMovie("Inception");

        runExperiment();
    }

    public static void runExperiment() {

        AVLTree tree = new AVLTree();
        Random rand = new Random();

        int size = 10000;

        long startInsert = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {

            String title = "Movie" + rand.nextInt(100000);
            tree.insertMovie(new Movie(title, 2000 + rand.nextInt(20), rand.nextDouble() * 10));

        }

        long endInsert = System.currentTimeMillis();

        System.out.println("\nInserted " + size + " movies.");
        System.out.println("Insertion time: " + (endInsert - startInsert) + " ms");

        long startSearch = System.currentTimeMillis();

        tree.searchMovie("Movie500");

        long endSearch = System.currentTimeMillis();

        System.out.println("Search time: " + (endSearch - startSearch) + " ms");
    }
}