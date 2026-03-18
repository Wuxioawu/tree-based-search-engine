package com.assignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Self-Balancing AVL Tree.
 *
 * <p>Supports O(log n) insert, delete, and search operations.
 * The tree automatically maintains the AVL balance property:
 * |height(left) - height(right)| <= 1 at every node.
 *
 * <p>Operations and their complexities:
 * <ul>
 *   <li>insert  - O(log n)</li>
 *   <li>delete  - O(log n)</li>
 *   <li>search  - O(log n)</li>
 *   <li>inOrder - O(n)</li>
 *   <li>range   - O(log n + k) where k = results returned</li>
 * </ul>
 *
 * @param <T> the key type (must implement Comparable)
 * @param <V> the value type
 */
public class AVLTree<T extends Comparable<T>, V> {

    private AVLNode<T, V> root;
    private int size;

    // ─────────────────────────────────────────────────────────────────────────
    //  Public API
    // ─────────────────────────────────────────────────────────────────────────

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    /** Insert a key-value pair. Duplicate keys update the existing value. */
    public void insert(T key, V value) {
        root = insert(root, key, value);
    }

    /** Delete the node with the given key. No-op if key is absent. */
    public void delete(T key) {
        root = delete(root, key);
    }

    /**
     * Search for a value by key.
     *
     * @return the value, or {@code null} if not found.
     */
    public V search(T key) {
        AVLNode<T, V> node = searchNode(root, key);
        return (node != null) ? node.value : null;
    }

    /** @return {@code true} if the tree contains the given key */
    public boolean contains(T key) {
        return searchNode(root, key) != null;
    }

    /** In-order traversal — returns values sorted by key (ascending). */
    public List<V> inOrderValues() {
        List<V> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    /**
     * Range query — returns all values whose key is in [low, high] (inclusive).
     *
     * <p>Time complexity: O(log n + k) where k is the number of results.
     */
    public List<V> rangeSearch(T low, T high) {
        List<V> result = new ArrayList<>();
        rangeSearch(root, low, high, result);
        return result;
    }

    /** @return the number of elements in the tree */
    public int size() { return size; }

    /** @return {@code true} if the tree is empty */
    public boolean isEmpty() { return root == null; }

    /** @return the height of the tree */
    public int height() { return height(root); }

    // ─────────────────────────────────────────────────────────────────────────
    //  Private Helpers — Insert
    // ─────────────────────────────────────────────────────────────────────────

    private AVLNode<T, V> insert(AVLNode<T, V> node, T key, V value) {
        // 1. Standard BST insert
        if (node == null) {
            size++;
            return new AVLNode<>(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left  = insert(node.left,  key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Duplicate key — update value in-place
            node.value = value;
            return node;
        }

        // 2. Update height
        updateHeight(node);

        // 3. Rebalance
        return rebalance(node);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Private Helpers — Delete
    // ─────────────────────────────────────────────────────────────────────────

    private AVLNode<T, V> delete(AVLNode<T, V> node, T key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left  = delete(node.left,  key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            // Found the node to delete
            size--;
            if (node.left == null || node.right == null) {
                // 0 or 1 child
                return (node.left != null) ? node.left : node.right;
            }
            // 2 children: replace with in-order successor (min of right subtree)
            AVLNode<T, V> successor = minNode(node.right);
            node.key   = successor.key;
            node.value = successor.value;
            size++;  // will be decremented again in the recursive call
            node.right = delete(node.right, successor.key);
        }

        updateHeight(node);
        return rebalance(node);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Private Helpers — Search
    // ─────────────────────────────────────────────────────────────────────────

    private AVLNode<T, V> searchNode(AVLNode<T, V> node, T key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) return searchNode(node.left,  key);
        else if (cmp > 0) return searchNode(node.right, key);
        else              return node;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Private Helpers — Traversal
    // ─────────────────────────────────────────────────────────────────────────

    private void inOrder(AVLNode<T, V> node, List<V> result) {
        if (node == null) return;
        inOrder(node.left,  result);
        result.add(node.value);
        inOrder(node.right, result);
    }

    private void rangeSearch(AVLNode<T, V> node, T low, T high, List<V> result) {
        if (node == null) return;
        int cmpLow  = low.compareTo(node.key);
        int cmpHigh = high.compareTo(node.key);

        if (cmpLow  < 0) rangeSearch(node.left,  low, high, result);
        if (cmpLow <= 0 && cmpHigh >= 0) result.add(node.value);
        if (cmpHigh > 0) rangeSearch(node.right, low, high, result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  AVL Balancing
    // ─────────────────────────────────────────────────────────────────────────

    private AVLNode<T, V> rebalance(AVLNode<T, V> node) {
        int bf = balanceFactor(node);

        // Left-heavy
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                // Left-Right case
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);   // Left-Left case
        }

        // Right-heavy
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                // Right-Left case
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);    // Right-Right case
        }

        return node;  // already balanced
    }

    /** Left rotation — used for Right-Right imbalance */
    private AVLNode<T, V> rotateLeft(AVLNode<T, V> z) {
        AVLNode<T, V> y = z.right;
        AVLNode<T, V> T2 = y.left;
        y.left  = z;
        z.right = T2;
        updateHeight(z);
        updateHeight(y);
        return y;
    }

    /** Right rotation — used for Left-Left imbalance */
    private AVLNode<T, V> rotateRight(AVLNode<T, V> z) {
        AVLNode<T, V> y = z.left;
        AVLNode<T, V> T3 = y.right;
        y.right = z;
        z.left  = T3;
        updateHeight(z);
        updateHeight(y);
        return y;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Utility
    // ─────────────────────────────────────────────────────────────────────────

    private int height(AVLNode<T, V> node) {
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(AVLNode<T, V> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(AVLNode<T, V> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode<T, V> minNode(AVLNode<T, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }
}