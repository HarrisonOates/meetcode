package com.example.myeducationalapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
@RunWith(Parameterized.class)
public class AVLTreeTest {
    @Parameterized.Parameters
    public static Collection<Object[]> trees() {
        // For the sake of non-existing values used to test search(),
        // all values are in between -99 and 99 (inclusive)
        Integer[] tree1Values = new Integer[]{-3,-2,-1,0,1,2,3,4,5};
        Integer[] preOrder1 = new Integer[]{0,-2,-3,-1,2,1,4,3,5};

        Integer[] tree2Values = new Integer[]{4,2,3,0,5,6,10,9,7};
        Integer[] preOrder2 = new Integer[]{3,2,0,6,5,4,9,7,10};

        Integer[] tree3Values = new Integer[]{20,12,35,-21,0,-35,67,11,60,59,23,38,-99,99,42};
        Integer[] preOrder3 = new Integer[]{20,0,-35,-99,-21,12,11,59,35,23,38,42,67,60,99};


        return Arrays.asList(new Object[][]
                {{tree1Values, preOrder1}, {tree2Values, preOrder2}, {tree3Values, preOrder3}});
    }
    @Parameterized.Parameter(0)
    public Integer[] treeValues;
    @Parameterized.Parameter(1)
    public Integer[] preOrderValues;

    // Test preOrderTraversal(), search() and insert()
    @Test (timeout = 100)
    public void simpleAVLTreeTest() {
        AVLTree<Integer> tree = new AVLTree<>();
        Integer[] nodesValues = treeValues;
        for (Integer value : nodesValues) {
            tree.insert(value);
        }
        // Visual representation of the tree
        System.out.println(tree);

        // Test the implementation of preOrderTraversal()
        Integer[] preOrder = preOrderValues;
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
        int[] nonExValues = new int[]{-100,201,218,100,123,1519,845};
        for (int value : nonExValues) {
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