package com.assignment;

/**
 * Generic AVL Tree Node.
 *
 * @param <T> the key type (must implement Comparable)
 * @param <V> the value type stored at each node
 */
public class AVLNode<T extends Comparable<T>, V> {

    T key;
    V value;
    AVLNode<T, V> left;
    AVLNode<T, V> right;
    int height;

    public AVLNode(T key, V value) {
        this.key    = key;
        this.value  = value;
        this.height = 1;
        this.left   = null;
        this.right  = null;
    }
}