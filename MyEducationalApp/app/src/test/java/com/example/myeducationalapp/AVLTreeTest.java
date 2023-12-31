package com.example.myeducationalapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author u7469758 Geun Yun
 */


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

        //Test the inOrderTraversal()
        ArrayList<AVLTree.Node<Integer>> inOrder = tree.inOrderTraversal();
        for (AVLTree.Node<Integer> node : inOrder) {
            System.out.println(node.value);
        }
        int prev = -100;
        for (AVLTree.Node<Integer> node : inOrder) {
            if (node.value != null) {
                assertTrue(prev < node.value);
                prev = node.value;
            }
        }

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

        // Test whether stringToTree restores the non-empty original tree
        if (tree.size() != 0) {
            AVLTree<Integer> treeFromString = new AVLTree<>();
            treeFromString = treeFromString.stringToTree(tree.toString(), false);
            System.out.println(treeFromString.visualize());
            areTreesSame(tree.root, treeFromString.root);
        }

        // Test whether you can delete then insert consecutively
        int randomNumber = getRandomNumber(-99, 99);
        System.out.println("ranrdom number is: " + randomNumber);
        tree.insert(randomNumber);
        ArrayList<AVLTree.Node<Integer>> nodesAfterInsert = tree.preOrderTraversal();
        validAVLTest(nodesAfterInsert, tree);

        tree.delete(randomNumber);
        ArrayList<AVLTree.Node<Integer>> nodesAfterDel2 = tree.preOrderTraversal();
        validAVLTest(nodesAfterDel2, tree);

        // Update the preOrderValues after the insert/delete test above
        ArrayList<AVLTree.Node<Integer>> updatedNodes = tree.preOrderTraversal();
        preOrderValues.clear();
        for  (AVLTree.Node<Integer> node : updatedNodes) {
            preOrderValues.add(node.value);
        }

        // Randomize the order of the nodes to be deleted
        // local deletedNo in this test is initialized to 1, as delete() was already called once above.
        int deletedNo = 1;
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

    // Test whether two given trees are identical in terms of its values and their positions
    // 0 (1 null) != 1 (0 null) although they have identical values
    public void areTreesSame(AVLTree.Node<Integer> node1, AVLTree.Node<Integer> node2) {
        if (node1 == null && node2 == null) {
            return;
        } else if (node1 == null) {
            fail();
        }
        assertEquals(node1.value, node2.value);
        areTreesSame(node1.left, node2.left);
        areTreesSame(node1.right, node2.right);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
