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

        // Testing empty tree
        Integer[] emptyTree = new Integer[0];

        // Testing delete() with no child case
        Integer[] singleTree = new Integer[]{99};

        // Testing delete() with 1 right child case
        Integer[] trivialTree = new Integer[]{-1,1};

        // Testing delete() with 1 left child case
        Integer[] trivialTree2 = new Integer[]{1,-1};

        Integer[] tree1Values = new Integer[]{-3,-2,-1,0,1,2,3,4,5};
        Integer[] preOrder1 = new Integer[]{0,-2,-3,-1,2,1,4,3,5};

        Integer[] tree2Values = new Integer[]{4,2,3,0,5,6,10,9,7};
        Integer[] preOrder2 = new Integer[]{3,2,0,6,5,4,9,7,10};

        Integer[] tree3Values = new Integer[]{20,12,35,-21,0,-35,67,11,60,59,23,38,-99,99,42};
        Integer[] preOrder3 = new Integer[]{20,0,-35,-99,-21,12,11,59,35,23,38,42,67,60,99};


        return Arrays.asList(new Object[][]{
                        {emptyTree, emptyTree}, {singleTree, singleTree},{trivialTree, trivialTree},
                        {trivialTree2, trivialTree2}, {tree1Values, preOrder1}, {tree2Values, preOrder2},
                        {tree3Values, preOrder3}});
    }
    @Parameterized.Parameter(0)
    public Integer[] treeValues;
    @Parameterized.Parameter(1)
    public Integer[] preOrderValues;

    // Repeat the AVLTreeTest 100 times for each tree so that chance of having false positive result
    // from getting 'lucky' shuffle in the deletion test becomes negligible.
    @Test(timeout = 500)
    public void repeatAVLTreeTest() {
        for (int i = 0; i < 100; i++) {
            AVLTreeTest();
        }
    }

    // Sufficiently test everything in the AVLTree class (100% Class, Method and Line coverages)
    public void AVLTreeTest() {
        AVLTree<Integer> tree = new AVLTree<>();
        Integer[] nodesValues = treeValues;
        for (Integer value : nodesValues) {
            tree.insert(value);
        }
        // Visual representation of the tree
        System.out.println(tree.visualize());

        // Test the implementation of preOrderTraversal()
        Integer[] preOrder = preOrderValues;
        ArrayList<Integer> preOrderValues = new ArrayList<>();
        Collections.addAll(preOrderValues, preOrder);
        ArrayList<AVLTree.Node<Integer>> nodes = tree.preOrderTraversal();
        assertEquals("Size of values and nodes should be the same",
                treeValues.length, tree.size());
        for (int i = 0; i < preOrderValues.size(); i++) {
            assertEquals("Incorrect insertion", preOrderValues.get(i), nodes.get(i).value);
        }

        // Test the implementation of search()
        for (Integer value : preOrder) {
            assertNotNull("This value exists in the tree", tree.search(value));
        }
        int[] nonExValues = new int[]{-100,201,218,100,123,1519,845};
        for (int value : nonExValues) {
            assertNull("This value does not exist in the tree", tree.search(value));
        }

        // Test whether the tree is still a valid AVLTree after insert()
        validAVLTest(nodes, tree);

        // Randomize the order of the nodes to be deleted
        int deletedNo = 0;
        Collections.shuffle(preOrderValues);
        for (Integer valueToBeDeleted : preOrderValues) {
            tree.delete(valueToBeDeleted);
            // The deleted value should no longer exist in the tree
            assertNull("The value was deleted, but still exists", tree.search(valueToBeDeleted));
            System.out.println(tree.visualize());

            // Check whether the deletedNo in the AVLTree was updated
            deletedNo++;
            System.out.println(valueToBeDeleted + " just got deleted");
            System.out.println(tree.getDeletedNo() + " node/s were deleted in total from the original tree");
            assertEquals("deletedNode was not updated", deletedNo, tree.getDeletedNo());


            // Test whether the tree is still a valid AVLTree after each delete()
            ArrayList<AVLTree.Node<Integer>> nodesAfterDel = tree.preOrderTraversal();
            validAVLTest(nodesAfterDel, tree);
        }

        System.out.println("Test completed for the set of trees above \n _________________________________");
    }

    // Test whether the current tree is a valid AVLTree
    public void validAVLTest(ArrayList<AVLTree.Node<Integer>> nodes, AVLTree<Integer> tree) {
        for (AVLTree.Node<Integer> node : nodes) {
            // Test whether the height difference between left and right subtrees of a node is at most
            System.out.println("Node: " + node.value + ", Balance Factor: " + tree.getBalance(node));
            assertTrue("Balance factor condition is not met",
                    Math.abs(tree.getBalance(node)) <= 1);

            // Test whether the tree is a BST
            int left;
            int right;

            // If its left or right child is empty, then the child can be ignored and
            // the corresponding case can always be evaluated as true
            if (node.left == null || node.left.value == null) {
                // guarantees left < found.value to be true
                left = node.value - 1;
            } else {
                left = node.left.value;
            }

            if (node.right == null || node.right.value == null) {
                // guarantees found.value < right to be true
                right = node.value + 1;
            } else {
                right = node.right.value;
            }
            assertTrue("The value should be smaller than its left child and greater than its right child",
                    left < node.value && node.value < right);
        }
        System.out.println("");
    }
}
