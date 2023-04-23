package com.example.myeducationalapp;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;

public class AVLTreeTest {

    // Test preOrderTraversal(), search() and insert() on a simple AVLTree
    @Test (timeout = 100)
    public void simpleAVLTreeTest() {
        AVLTree<Integer> tree = new AVLTree<>();
        Integer[] nodesValues = new Integer[]{4,2,3,0,5,6,10,9,7};
        for (Integer value : nodesValues) {
            tree.insert(value);
        }
        // Visual representation of the tree
        System.out.println(tree);

        // Test the implementation of preOrderTraversal()
        Integer[] preOrder = new Integer[]{3,2,0,6,5,4,9,7,10};
        ArrayList<Integer> preOrderValues = new ArrayList<>();
        Collections.addAll(preOrderValues, preOrder);
        ArrayList<AVLTree.Node<Integer>> nodes = tree.preOrderTraversal();
        assertEquals("Size of values and nodes should be the same",
                preOrderValues.size(), nodes.size());
        for (int i = 0; i < preOrderValues.size(); i++) {
            assertEquals("Incorrect insertion", preOrderValues.get(i), nodes.get(i).value);
        }

        // Test the implementation of search()
        for (Integer value : preOrder) {
            assertNotNull("This value exists in the tree", tree.search(value));
        }
        int[] nonExVals = new int[]{-3,-1,8,11,-22,12,1};
        for (int value : nonExVals) {
            assertNull("This values does not exist in the tree", tree.search(value));
        }

        // Test whether the tree is still a valid AVLTree after insert()
        for (AVLTree.Node<Integer> node : nodes) {
            // Test whether the height difference between left and right subtrees of a node is at most
            assertTrue("Balance factor condition is not met",
                    Math.abs(tree.getBalance(node)) <= 1);

            // Test whether the tree is a BST
            int left;
            int right;

            // If its left or right child is empty, then the child can be ignored and
            // the corresponding case can always be evaluated as true
            if (node.left.value == null) {
                // guarantees left < found.value to be true
                left = node.value - 1;
            } else {
                left = node.left.value;
            }

            if (node.right.value == null) {
                // guarantees found.value < right to be true
                right = node.value + 1;
            } else {
                right = node.right.value;
            }
            assertTrue("The value should be smaller than its left child and greater than its right child",
                    left < node.value && node.value < right);
        }
    }
}
