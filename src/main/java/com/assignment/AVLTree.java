package com.assignment;

public class AVLTree {

    Node root;

    int height(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int getBalance(Node node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    Node rightRotate(Node y) {

        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {

        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    Node insert(Node node, Movie movie) {

        if (node == null)
            return new Node(movie);

        if (movie.getTitle().compareTo(node.movie.getTitle()) < 0)
            node.left = insert(node.left, movie);
        else if (movie.getTitle().compareTo(node.movie.getTitle()) > 0)
            node.right = insert(node.right, movie);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && movie.getTitle().compareTo(node.left.movie.getTitle()) < 0)
            return rightRotate(node);

        if (balance < -1 && movie.getTitle().compareTo(node.right.movie.getTitle()) > 0)
            return leftRotate(node);

        if (balance > 1 && movie.getTitle().compareTo(node.left.movie.getTitle()) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && movie.getTitle().compareTo(node.right.movie.getTitle()) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    Node search(Node node, String title) {

        if (node == null || node.movie.getTitle().equals(title))
            return node;

        if (title.compareTo(node.movie.getTitle()) < 0)
            return search(node.left, title);

        return search(node.right, title);
    }

    void inorder(Node node) {

        if (node != null) {
            inorder(node.left);
            System.out.println(node.movie);
            inorder(node.right);
        }
    }

    void insertMovie(Movie movie) {
        root = insert(root, movie);
    }

    void searchMovie(String title) {

        Node result = search(root, title);

        if (result != null)
            System.out.println("Found: " + result.movie);
        else
            System.out.println("Movie not found");
    }

    void printMovies() {
        inorder(root);
    }
}