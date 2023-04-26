package com.example.myeducationalapp;

import java.util.ArrayList;

// The AVLTree data structure will be implemented for each of these data sets:
// - Past questions' ID
// - Liked message ID
// ...
public class AVLTree<T extends Comparable<T>> {
    // Keep a track of how many past questions have been deleted (e.g. wrong questions have been posted so need to be removed?)
    // delete() will be the last one to implement if we have time
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
        if (node == null) {
            return 0;
        }

        int leftHeight = (node.left != null) ? node.left.getHeight() : 0;
        int rightHeight = (node.right != null) ? node.right.getHeight() : 0;

        return leftHeight - rightHeight;
    }

    /**
     * Finds the imbalanced node that is closest to the node that has been inserted.
     * Such node will be the root of the subtree that is to be rebalanced.
     * @param leaf node that has been inserted
     * @return starting node of the imbalance
     */
    private Node<T> findImbalance(Node<T> leaf){
        if (root == null) {
            return null;
        }

        if (Math.abs(getBalance(leaf)) > 1){
            return leaf;
        } else if (leaf.parent != null){
            return findImbalance(leaf.parent);
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
        if (root.value.compareTo(curr.value) > 0) {
            if (root.left.value == null) {
                root.left = curr;
                curr.parent = root;
            } else {
                insertBeforeBalance(root.left, curr);
            }
        } else if (root.value.compareTo(curr.value) < 0) {
            if (root.right.value == null) {
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
            Node<T> rootOfImbalance = findImbalance(newNode);
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
     * Delete a node with a given value in the AVLTree. No change if the value does not exist.
     * @param curr node
     * @param value that is to be deleted
     */
    private Node<T> deleteBeforeBalance(Node<T> curr, T value) {
        if (curr == null) {
            return null;
        }
        if (value.compareTo(curr.value) > 0) {
            curr.right = deleteBeforeBalance(curr.right, value);
        } else if (value.compareTo(curr.value) < 0) {
            curr.left = deleteBeforeBalance(curr.left, value);
        } else {
            if (curr.left.value == null && curr.right.value == null) {
                curr = null;
            } else if (curr.left.value == null) {
                curr =  curr.right;
            } else if (curr.right.value == null) {
                curr = curr.left;
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
        while (node.left.value != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Delete a node with the given value and preserves all the properties of the AVLTree.
     * @param value that is to be deleted
     */
    public void delete(T value) {
        Node<T> deletedNode = deleteBeforeBalance(root, value);
        if (deletedNode != null) {
            deletedNo++;
        }
        Node<T> rootOfImbalance = findImbalance(deletedNode);
        if (rootOfImbalance != null) {
            balanceSubtree(rootOfImbalance);
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
        if (node.value == null) {
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

    public int size() {
        return preOrderTraversal().size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringHelper(sb, "", "", root);
        return sb.toString();
    }

    private void toStringHelper(StringBuilder sb, String padding, String pointer, Node<T> node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.value);
            sb.append("\n");

            String paddingForBoth = padding + "|  ";
            String pointerRight = "R___";
            String pointerLeft = "L___";

            toStringHelper(sb, paddingForBoth, pointerLeft, node.left);
            toStringHelper(sb, paddingForBoth, pointerRight, node.right);
        }
    }

    public static class Node<E extends Comparable<E>> {

        E value; // Node value - hash value of past questions?
        Node<E> parent, left, right; // this parent, this left and right nodes

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

        public int getHeight(){
            // Check whether leftNode or rightNode are null
            int leftNodeHeight = left == null ? 0 : 1 + left.getHeight();
            int rightNodeHeight = right == null ? 0 : 1 + right.getHeight();
            return Math.max(leftNodeHeight, rightNodeHeight);
        }

    }
}

