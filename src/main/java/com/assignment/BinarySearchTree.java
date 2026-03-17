package com.assignment;

public class BinarySearchTree {
    Node root;

    public void insertMovie(Movie movie) {
        root = insert(root, movie);
    }

    private Node insert(Node node, Movie movie) {
        if (node == null) return new Node(movie);

        int cmp = movie.getTitle().compareTo(node.movie.getTitle());
        if (cmp < 0)
            node.left = insert(node.left, movie);
        else if (cmp > 0)
            node.right = insert(node.right, movie);

        return node;
    }

    public void searchMovie(String title) {
        Node result = search(root, title);
    }

    private Node search(Node node, String title) {
        if (node == null || node.movie.getTitle().equals(title))
            return node;
        if (title.compareTo(node.movie.getTitle()) < 0)
            return search(node.left, title);
        return search(node.right, title);
    }
}
