package com.assignment;

public class Node {

    Movie movie;
    Node left;
    Node right;
    int height;

    public Node(Movie movie) {
        this.movie = movie;
        this.height = 1;
    }
}