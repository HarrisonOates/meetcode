package com.example.myeducationalapp;

import androidx.annotation.NonNull;

import com.example.myeducationalapp.User.ComparablePair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used to store the current user's liked messages, blocked user list and the successfully answered questions in UserLocalData
 * and also who has liked the message in Message.java.
 * @author u7469758 Geun Yun
 */
public class AVLTree<T extends Comparable<T>> {
    public int deletedNo = 0;
    public Node<T> root;
    public AVLTree() {this.root = null;}

    /**
     * Performs the right rotation at a given node
     * @param root of a subtree, not necessarily the root of the whole tree
     */
    private void rightRotate(Node<T> root) {
        //Temporary reference to the left child of the current root of the subtree
        Node<T> newRoot = root.left;
        root.left = newRoot.right;

        //Updates the parents of the nodes
        if (newRoot.right != null) {
            newRoot.right.parent = root;
        }
        newRoot.parent = root.parent;

        //Check whether the root is actual root of the whole tree (which should never happen in our specification),
        //then update this newRoot as the left or right child of the original root's parent
        if (root.parent == null) {
            this.root = newRoot;
        } else if (root == root.parent.right) {
            root.parent.right = newRoot;
        } else {
            root.parent.left = newRoot;
        }
        newRoot.right = root;
        root.parent = newRoot;
    }

    /**
     * Perform left rotation at a given node
     * @param root of a subtree, not necessarily the root of the whole tree
     */
    private void leftRotate(Node<T> root) {
        Node<T> newRoot = root.right;
        root.right = newRoot.left;

        //Updates the parents of the nodes
        if (newRoot.left != null) {
            newRoot.left.parent = root;
        }
        newRoot.parent = root.parent;

        //Check whether the root is actual root of the whole tree (which should never happen in our specification),
        //then update this newRoot as the left or right child of the original root's parent
        if (root.parent == null) {
            this.root = newRoot;
        } else if (root == root.parent.right) {
            root.parent.right = newRoot;
        } else {
            root.parent.left = newRoot;
        }
        newRoot.left = root;
        root.parent = newRoot;
    }

    /**
     * @param node of the AVLTree
     * @return balance factor at a given node
     */
    public int getBalance(Node<T> node) {
        if (node == null || node.value == null) {
            return 0;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return leftHeight - rightHeight;
    }

    private int getHeight(Node<T> node) {
        if (node == null || node.value == null) {
            return 0;
        }

        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }


    /**
     * Finds the imbalanced node that is closest to the node that has been inserted.
     * Such node will be the root of the subtree that is to be rebalanced.
     * @param leaf node that has been inserted
     * @return starting node of the imbalance
     */
    private Node<T> findImbalanceInsert(Node<T> leaf){
        if (Math.abs(getBalance(leaf)) > 1){
            return leaf;
        } else if (leaf.parent != null){
            return findImbalanceInsert(leaf.parent);
        }
        return null;
    }

    /**
     * Fixes the subtree to satisfy the AVLTree properties.
     * @param root of the subtree that needs rebalancing
     */
    private void balanceSubtree(Node<T> root) {
        int balanceFactor = getBalance(root);
        if (balanceFactor > 1) {
            if (getBalance(root.left) <= -1) {
                leftRotate(root.left);
            }
            rightRotate(root);
        } else if (balanceFactor < -1) {
            if (getBalance(root.right) >= 1) {
                rightRotate(root.right);
            }
            leftRotate(root);
        }
    }

    /**
     * Insert a node to the AVLTree as if it was an ordinary BST tree.
     * @param root of the AVLTree
     * @param curr node that is to be inserted
     */
    private void insertBeforeBalance(Node<T> root, Node<T> curr) {
        if (root == null || root.value == null) {
            root = curr;
            root.value = curr.value;
        }

        if (root.value.compareTo(curr.value) > 0) {
            if (root.left == null || root.left.value == null) {
                root.left = curr;
                curr.parent = root;
            } else {
                insertBeforeBalance(root.left, curr);
            }
        } else if (root.value.compareTo(curr.value) < 0) {
            if (root.right == null || root.right.value == null) {
                root.right = curr;
                curr.parent = root;
            } else {
                insertBeforeBalance(root.right, curr);
            }
        }
        // Do nothing if the tree already has a node with the same value.
    }

    /**
     * Insert the node while satisfying all the AVLTree properties.
     * @param newNode that is to be inserted
     */
    private void insert(Node<T> newNode) {
        if (this.root == null) {
            this.root = newNode;
        } else {
            insertBeforeBalance(this.root, newNode);
            Node<T> rootOfImbalance = findImbalanceInsert(newNode);
            if (rootOfImbalance != null) {
                balanceSubtree(rootOfImbalance);
            }
        }
    }

    /**
     * Insert a node with a given value to the AVLTree. No change if the value does not exist.
     * @param value that is to be inserted
     */
    public void insert(T value) {
        Node<T> node = new Node<>(value);
        insert(node);
    }

    /**
     * Finds the imbalanced node that is closest to the node that replaced the deleted node
     * Such node will be the root of the subtree that is to be rebalanced.
     * @param node that replaced the deleted node
     * @param nodes that has unbalanced factor which there should only be one
     */
    private void findImbalanceDelete(Node<T> node, ArrayList<Node<T>> nodes){
        if (node == null) {
            return;
        }

        if (Math.abs(getBalance(node)) > 1){
            nodes.add(node);
        }
        findImbalanceDelete(node.left, nodes);
        findImbalanceDelete(node.right, nodes);
    }

    /**
     * Delete a node with a given value in the AVLTree. No change if the value does not exist.
     * @param curr node
     * @param value that is to be deleted
     */
    private Node<T> deleteBeforeBalance(Node<T> curr, T value) {
        if (value.compareTo(curr.value) > 0) {
            curr.right = deleteBeforeBalance(curr.right, value);
        } else if (value.compareTo(curr.value) < 0) {
            curr.left = deleteBeforeBalance(curr.left, value);
        } else {
            if ((curr.left == null || curr.left.value == null) && (curr.right == null || curr.right.value == null)) {
                curr.value = null;
                if (curr == root) {
                    root = null;
                }
                curr = null;

            } else if (curr.left == null || curr.left.value == null) {
                curr.value = curr.right.value;
                curr.right = deleteBeforeBalance(curr.right, curr.value);
            } else if (curr.right == null || curr.right.value == null) {
                curr.value = curr.left.value;
                curr.left = deleteBeforeBalance(curr.left, curr.value);
            } else {
                // This is case where the node that is to be deleted has two children.
                // The inorder successor of the node needs to replace the deletedNode, since it is
                // guaranteed to be greater than the deletedNode's left subtree and smaller than its right subtree.
                Node<T> minNode = findSuccessor(curr.right);
                curr.value = minNode.value;
                curr.right = deleteBeforeBalance(curr.right, curr.value);
            }
        }
        return curr;
    }

    private Node<T> findSuccessor(Node<T> node) {
        while (node.left != null && node.left.value != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Delete a node with the given value and preserves all the properties of the AVLTree.
     * @param value that is to be deleted
     */
    public void delete(T value) {
        if (search(value) != null) {
            deletedNo++;
        }
        Node<T> deletedNode = deleteBeforeBalance(root, value);
        ArrayList<Node<T>> nodes = new ArrayList<>();
        findImbalanceDelete(deletedNode, nodes);
        // There can be cases where more than one balancing is needed after one deletion
        // https://cs.stackexchange.com/questions/128245/worst-case-for-avl-tree-balancing-after-deletion
        while (nodes.size() != 0) {
            balanceSubtree(nodes.get(0));
            nodes.clear();
            findImbalanceDelete(root, nodes);
        }
    }

    public int getDeletedNo(){
        return deletedNo;
    }

    /**
     * Find and return the node with a given value if there is any.
     * If no node with the value exists, return null;
     * @param node whose value is being checked
     * @param value that wants to be found
     * @return the node with a given value or null
     */
    public Node<T> find(Node<T> node, T value) {
        if (node == null || node.value == null) {
            return null;
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0)
            return find(node.left, value);
        else if (cmp > 0)
            return find(node.right, value);
        else
            return node;
    }

    /**
     * Search for a given value in the AVLTree
     * @param value that is to be searched
     * @return the node with the value or null
     */
    public Node<T> search(T value) {
        return find(this.root, value);
    }


    /**
     * @return All the nodes in the AVLTree in pre order as an array list
     */
    public ArrayList<Node<T>> preOrderTraversal(){
        ArrayList<Node<T>> nodes = new ArrayList<>();
        preOrderHelper(this.root, nodes);
        return nodes;
    }

    private void preOrderHelper(Node<T> curr, ArrayList<Node<T>> nodes) {
        if (curr != null && curr.value != null) {
            nodes.add(curr);
            preOrderHelper(curr.left, nodes);
            preOrderHelper(curr.right, nodes);
        }
    }

    /**
     * @return All the nodes in the AVLTree in orders an array list
     */
    public ArrayList<Node<T>> inOrderTraversal(){
        ArrayList<Node<T>> nodes = new ArrayList<>();
        inOrderHelper(this.root, nodes);
        return nodes;
    }

    private void inOrderHelper(Node<T> curr, ArrayList<Node<T>> nodes) {
        if (curr != null && curr.value != null) {
            inOrderHelper(curr.left, nodes);
            nodes.add(curr);
            inOrderHelper(curr.right, nodes);
        }
    }


    public ArrayList<T> levelOrderTraversal(){
        ArrayList<T> nodes = new ArrayList<>();
        int height = getHeight(root);
        for (int i = 1; i <= height; i++) {
            getCurrentLevel(root, i, nodes);
        }
        return nodes;
    }

    public void getCurrentLevel(Node<T> root, int level, ArrayList<T> nodes) {
        if (root == null) {
            return;
        }
        if (level == 1) {
            nodes.add(root.value);
        } else if (level > 1) {
            getCurrentLevel(root.left, level - 1, nodes);
            getCurrentLevel(root.right, level - 1, nodes);
        }
    }

    /**
     * AVLTree<T> treeFromString = tree.stringToTree(tree.toString());
     * treeFromString.visualize() should print the same structure with same values as tree.visualize().
     * @param levelOrderTraversal consisting of all the values in the tree in a level order
     *                            such that the structure is preserved
     * @param useAtSignToSplit    should be set when a T is of type ComparablePair
     * @return the AVLTree consisting of given values
     */
    public AVLTree<T> stringToTree(String levelOrderTraversal, boolean useAtSignToSplit) {
        AVLTree<T> tree = new AVLTree<>();
        if (levelOrderTraversal.length() == 0) {
            /*
             * Otherwise, we get values having an empty string, which causes it to add an
             * empty string to the tree, which may be of the wrong type (i.e. not T), so it
             * crashes.
             */
            return tree;
        }
        String[] values = levelOrderTraversal.split(",");
        ArrayList<String> valuesList = new ArrayList<>(Arrays.asList(values));
        ArrayList<String> strNull = new ArrayList<>();
        strNull.add("null");
        valuesList.removeAll(strNull);
        for (String value : valuesList) {
            if (value.contains("@") && useAtSignToSplit) {
                tree.insert((T) new ComparablePair<>(
                        Integer.parseInt(value.split("@")[0]),
                        Integer.parseInt(value.split("@")[1])
                ));

            } else {
                try {
                    Integer valueInt = Integer.valueOf(value);

                    // Checks whether T is Integer, which should be the case for most of the time.
                    tree.insert((T) valueInt);
                } catch (Exception e) {
                    // This is if we decide to store keys of QuestionIDs as AVLTree.
                    tree.insert((T) value);
                }
                // T can only be Integer or String in our app.
            }

        }
        return tree;
    }

    public int size() {
        return preOrderTraversal().size();
    }

    public String visualize() {
        StringBuilder sb = new StringBuilder();
        visualizeHelper(sb, "", "", root);
        return sb.toString();
    }

    private void visualizeHelper(StringBuilder sb, String padding, String pointer, Node<T> node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.value);
            sb.append("   (").append(node.value == null ? "null" : node.value.getClass().toString()).append(")");
            sb.append("\n");

            String paddingForBoth = padding + "|  ";
            String pointerRight = "R___";
            String pointerLeft = "L___";

            visualizeHelper(sb, paddingForBoth, pointerLeft, node.left);
            visualizeHelper(sb, paddingForBoth, pointerRight, node.right);
        }
    }

    @NonNull
    @Override
    public String toString() {
        if (size() == 0) {
            return "";
        }
        ArrayList<T> inOrder = levelOrderTraversal();
        StringBuilder sb = new StringBuilder();
        for (T value : inOrder) {
            sb.append(value).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static class Node<E extends Comparable<E>> {

        E value;
        Node<E> parent, left, right;

        public Node(E value) {
            this.value = value;
            this.parent = null;

            // Initialise children leaf nodes
            this.left = new Node<>();
            this.right = new Node<>();
            this.left.parent = this;
            this.right.parent = this;
        }

        public Node() {
            this.value = null;
        }
    }
}

