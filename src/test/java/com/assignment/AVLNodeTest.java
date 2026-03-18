package com.assignment;

import static org.junit.jupiter.api.Assertions.*;

class AVLNodeTest {

    @org.junit.jupiter.api.Test
    void testAVLNodeCreation() {
        AVLNode<Integer, String> node = new AVLNode<>(10, "ten");
        assertEquals(10, node.key);
        assertEquals("ten", node.value);
        assertNull(node.left);
        assertNull(node.right);
        assertEquals(1, node.height);
    }

}