package com.assignment;

public class SplayTree {
    Node root;

    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    private Node splay(Node root, String title) {
        if (root == null || root.movie.getTitle().equals(title))
            return root;

        int cmp = title.compareTo(root.movie.getTitle());

        if (cmp < 0) {
            if (root.left == null) return root;
            if (title.compareTo(root.left.movie.getTitle()) < 0) {
                root.left.left = splay(root.left.left, title);
                root = rightRotate(root);
            } else if (title.compareTo(root.left.movie.getTitle()) > 0) {
                root.left.right = splay(root.left.right, title);
                if (root.left.right != null)
                    root.left = leftRotate(root.left);
            }
            return (root.left == null) ? root : rightRotate(root);
        } else {
            if (root.right == null) return root;
            if (title.compareTo(root.right.movie.getTitle()) < 0) {
                root.right.left = splay(root.right.left, title);
                if (root.right.left != null)
                    root.right = rightRotate(root.right);
            } else if (title.compareTo(root.right.movie.getTitle()) > 0) {
                root.right.right = splay(root.right.right, title);
                root = leftRotate(root);
            }
            return (root.right == null) ? root : leftRotate(root);
        }
    }

    public void insertMovie(Movie movie) {
        if (root == null) {
            root = new Node(movie);
            return;
        }
        root = splay(root, movie.getTitle());
        int cmp = movie.getTitle().compareTo(root.movie.getTitle());
        if (cmp == 0) return;

        Node newNode = new Node(movie);
        if (cmp < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
    }

    public void searchMovie(String title) {
        root = splay(root, title);
    }
}